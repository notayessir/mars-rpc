package com.notayessir.rpc.api.bean;

import com.notayessir.rpc.netty.remote.NettyClient;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 消费端请求任务
 */
public class RequestTask implements Callable<Void>{

    /**
     * 阻塞消费端，直至得到响应
     */
    private final CountDownLatch latch;

    /**
     * 远程调用持有的 netty 客户端
     */
    private final NettyClient nettyClient;

    /**
     * 请求帧
     */
    private final RequestFrame requestFrame;

    public CountDownLatch getLatch() {
        return latch;
    }

    public RequestTask(RequestFrame requestFrame, NettyClient nettyClient) {
        this.latch = new CountDownLatch(1);
        this.nettyClient = nettyClient;
        this.requestFrame = requestFrame;
    }

    @Override
    public Void call() throws Exception {
        nettyClient.writeRequest(requestFrame);
        latch.await();
        return null;
    }

}
