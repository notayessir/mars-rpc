package com.notayessir.cluster.fault.impl;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.common.executor.TaskExecutor;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.RPCException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 在运行时通过线程池创建多个线程，并发调用多个服务提供者，只要有一个服务提供者成功返回了结果，其他的并发调用就取消
 */
@Component("FORKING")
@Lazy
public class ForkingCluster extends Cluster {


    @Override
    public Object invoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws RPCException {
        invokers = selectAvailable(invokers);
        final List<Invoker> selected;
        int forkingNumber = invocation.getClusterMeta().getForkingNumber();
        // 如果 forkingNumber 填错，修正一下
        if (forkingNumber <=0 || forkingNumber >= invokers.size()){
            selected = invokers;
        }else {
            selected = new ArrayList<>(forkingNumber);
            // TODO 这里可能会选到重复的，导致实际执行的 invoker 数量与 forkingNumber 不相等，应该引入记录功能 ？
            for (int i = 0; i < forkingNumber; i++) {
                Invoker invoker = loadBalance.balance(invokers, invocation);
                if (!selected.contains(invoker)){
                    selected.add(invoker);
                }

            }
        }
        final AtomicInteger counter = new AtomicInteger();
        final BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<>();
        for (Invoker invoker : selected) {
            // TODO 受共享线程池限制，可能需要等待线程资源而进入等待队列
            TaskExecutor executor = MarsRPCContext.getTaskExecutor();
            executor.submit(() ->{
                try {
                    Object result = invoker.invoke(invocation);
                    resultQueue.add(result);
                } catch (Throwable e) {
                    int value = counter.incrementAndGet();
                    if (value >= selected.size()) {
                        resultQueue.add(e);
                    }
                }
            });
        }
        try {
            // 阻塞等待队列结果，若有结果即刻返回，其他线程调用结果忽略
            Object result = resultQueue.poll(invocation.getClusterMeta().getTimeout(), TimeUnit.MILLISECONDS);
            if (result instanceof Throwable) {
                throw (RPCException) result;
            }
            return result;
        } catch (InterruptedException e) {
            throw new RPCException(RPCException.Message.UN_CLASSIFY_EXCEPTION.getCode(), e.toString());
        }
    }
}
