package com.notayessir.rpc.api.bean;

import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;

import java.util.concurrent.CountDownLatch;

/**
 * 响应
 */
public class FutureResponse {

    /**
     * 在得到响应之前，阻塞请求端
     */
    private final CountDownLatch latch;

    /**
     * 响应载体
     */
    private ResponseFrame responseFrame;

    /**
     * 请求 id
     */
    private final long requestId;

    /**
     * 记录调用的方法返回类型
     */
    private Class<?> methodReturnType;

    public FutureResponse(long requestId, CountDownLatch latch, Class<?> methodReturnType) {
        this.requestId = requestId;
        this.latch = latch;
        this.methodReturnType = methodReturnType;
    }

    public Class<?> getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(Class<?> methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public long getRequestId() {
        return requestId;
    }

    public CountDownLatch getLatch() {
        return latch;
    }


    public ResponseFrame getResponseFrame() {
        return responseFrame;
    }

    public void setResponseFrame(ResponseFrame responseFrame) {
        this.responseFrame = responseFrame;
    }
}
