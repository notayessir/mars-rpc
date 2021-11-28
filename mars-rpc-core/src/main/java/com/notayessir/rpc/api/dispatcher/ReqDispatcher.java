package com.notayessir.rpc.api.dispatcher;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.registry.api.bean.Reference;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.ResponseTask;
import com.notayessir.rpc.api.util.InvocationUtil;
import com.notayessir.rpc.netty.remote.bean.ResponseStatus;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * 请求分发
 */
public class ReqDispatcher {


    private final static Logger LOG = LogManager.getLogger(ReqDispatcher.class);

    /**
     * 服务消费者将客户端请求派发至服务提供者
     * @param method        调用的方法
     * @param args          调用的参数
     * @param reference     提供者引用信息
     * @return              调用结果
     * @throws Throwable    调用失败时抛出
     */
    public static Object dispatch(Method method, Object[] args, Reference reference) throws Throwable {
        Invocation invocation = InvocationUtil.buildInvocation(method, args, reference,
                MarsRPCContext.genRequestId(), MarsRPCContext.getConsumerConfig().getSerializeConfig().getSerialization().getId());
        Cluster cluster = MarsRPCContext.getClusterInvoker(invocation.getClusterMeta().getStrategy().name());
        LoadBalance loadBalance = MarsRPCContext.getLoadBalance(invocation.getClusterMeta().getBalanceStrategy().name());
        List<Invoker> invokers = MarsRPCContext.getRouter().route(invocation.getMethod().getDeclaringClass().getName());
        return cluster.invoke(invokers, invocation, loadBalance);
    }

    /**
     * 服务提供者接收请求并执行返回结果
     * @param requestFrame      请求帧
     * @return                  执行结果
     */
    public static ResponseFrame dispatch(RequestFrame requestFrame) {
        ResponseTask responseTask = new ResponseTask(requestFrame);
        ResponseFrame responseFrame;
        Future<ResponseFrame> futureTask;

        try {
            futureTask = MarsRPCContext.getTaskExecutor().submit(responseTask);
        } catch (RejectedExecutionException e) {
            // 线程池繁忙
            responseFrame = responseTask.getResponseFrame();
            responseFrame.setStatus(ResponseStatus.SERVER_THREAD_POOL_BUSY.getValue());
            responseFrame.setInvocationResult(e.getMessage());
            LOG.error("exception happened when submit task to server thread pool, requestId:{}, e:{}", responseFrame.getRequestId(), e);
            return responseFrame;
        }

        try {
            responseFrame = futureTask.get();
        } catch (Throwable e) {
            // 业务异常
            responseFrame = responseTask.getResponseFrame();
            responseFrame.setStatus(ResponseStatus.SERVER_BUSINESS_ERROR.getValue());
            responseFrame.setInvocationResult(e.getMessage());
            LOG.error("exception happened when get result from responseTask, requestId:{}, e:{}", responseFrame.getRequestId(), e);
        }
        return responseFrame;
    }

}
