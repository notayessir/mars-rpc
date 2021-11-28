package com.notayessir.common.spring.event.bean;

import com.notayessir.rpc.api.bean.Invocation;
import org.springframework.context.ApplicationEvent;

import java.util.concurrent.CountDownLatch;


/**
 * RPC 请求被创建时创建该事件，用于获取响应
 */
public class FutureRespCreateEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    /**
     * 请求 id
     */
    private final long requestId;

    /**
     * 记录调用的方法返回类型
     */
    private final Class<?> methodReturnType;

    /**
     * 用于阻塞请求，等待响应完成
     */
    private final CountDownLatch countDownLatch;


    public FutureRespCreateEvent(Object source, int channelId, Invocation invocation, CountDownLatch countDownLatch) {
        super(source);
        this.channelId = channelId;
        this.requestId = invocation.getRequestId();
        this.methodReturnType = invocation.getMethod().getReturnType();
        this.countDownLatch = countDownLatch;
    }

    public Class<?> getMethodReturnType() {
        return methodReturnType;
    }

    public int getChannelId() {
        return channelId;
    }

    public long getRequestId() {
        return requestId;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}
