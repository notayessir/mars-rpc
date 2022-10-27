package com.notayessir.rpc.netty;

import com.notayessir.cluster.counter.ProcessCounter;
import com.notayessir.cluster.counter.ProcessCounterManager;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.response.FutureRespNotifier;
import com.notayessir.common.spring.event.response.FutureRespPublisher;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.*;
import com.notayessir.rpc.api.util.InvocationUtil;
import com.notayessir.rpc.netty.remote.NettyClient;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * netty 实现的 invoker
 */
public class NettyInvoker implements Invoker {

    private final Logger LOG = LogManager.getLogger(NettyInvoker.class);

    /**
     * netty 客户端
     */
    private final NettyClient nettyClient;

    /**
     * 是否可用
     */
    private volatile boolean enable;


    /**
     * invoker 元信息
     */
    private final InvokerMeta invokerMeta;


    public NettyInvoker(NettyClient nettyClient, InvokerMeta invokerMeta, boolean enable) {
        this.nettyClient = nettyClient;
        this.invokerMeta = invokerMeta;
        this.enable = enable;
    }

    /**
     * 调用前准备逻辑
     */
    private void beforeInvoke() {
        // 统计正在进行调用的任务数 +1，当前用于给负载均衡组件 LeastActiveLoadBalance 收集信息
        ProcessCounter statistic = ProcessCounterManager.get(nettyClient.getChannelId());
        statistic.incrProcessingCount();
    }


    /**
     * 调用后逻辑
     */
    private void afterInvoke(){
        // 统计正在进行调用的任务数 -1，当前用于给负载均衡组件 LeastActiveLoadBalance 收集信息
        ProcessCounter statistic = ProcessCounterManager.get(nettyClient.getChannelId());
        statistic.decrProcessingCount();
    }


    @Override
    public Object invoke(Invocation invocation) throws RPCException {
        if (!enable){
            throw new RPCException(RPCException.Message.CLIENT_UNAVAILABLE_INVOKER);
        }
        beforeInvoke();

        // 封装请求帧、FutureTask
        RequestFrame requestFrame = InvocationUtil.toRequestFrame(invocation);
        RequestTask requestTask = new RequestTask(requestFrame, nettyClient);
        FutureTask<Void> future = new FutureTask<>(requestTask);
        FutureRespPublisher publisher = MarsRPCContext.getBean(FutureRespPublisher.class);
        // 发布请求事件
        publisher.publishCreateEvent(nettyClient.getChannelId(), invocation, requestTask.getLatch());
        try {
            // 提交 FutureTask 任务，并阻塞等待结果（设置超时）
            MarsRPCContext.getTaskExecutor().submit(future);
            future.get(invocation.getClusterMeta().getTimeout(), TimeUnit.MILLISECONDS);
            // channel 可能会因为 decode 异常提前释放，需要防止 NPE
            FutureResponse response = MarsRPCContext.getBean(FutureRespNotifier.class).getResponse(nettyClient.getChannelId(), requestFrame.getRequestId());
            return InvocationUtil.toResponseObject(response);
        } catch (Throwable e){
            LOG.error("exception happened when get result from requestTask, requestId:{}", requestFrame.getRequestId(), e);
            // 调用失败出现异常时，需要释放相关资源，避免无效的实例对象占用内存，造成 OOM
            publisher.publishReleaseEvent(nettyClient.getChannelId(), invocation.getRequestId());
            if (e instanceof RejectedExecutionException){
                throw new RPCException(RPCException.Message.CLIENT_THREAD_POOL_BUSY);
            }
            if (e instanceof TimeoutException){
                throw new RPCException(RPCException.Message.SERVER_OR_CLIENT_TIME_OUT);
            }
            if (e instanceof RPCException){
                throw (RPCException) e;
            }
            throw new RPCException(RPCException.Message.UN_CLASSIFY_EXCEPTION.getCode(), e.toString());
        } finally {
            // 释放资源
            publisher.publishRemoveEvent(nettyClient.getChannelId(), invocation.getRequestId());
            afterInvoke();
        }
    }


    @Override
    public InvokerMeta getInvokerMeta() {
        return invokerMeta;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int getId() {
        return nettyClient.getChannelId();
    }


    @Override
    public void close() {
        this.enable = false;
        nettyClient.close();
    }
}
