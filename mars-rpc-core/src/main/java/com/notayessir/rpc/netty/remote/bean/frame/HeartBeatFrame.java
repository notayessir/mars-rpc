package com.notayessir.rpc.netty.remote.bean.frame;

/**
 * 心跳帧
 */
public class HeartBeatFrame {


    private boolean isRequest;


    public HeartBeatFrame(boolean isRequest) {
        this.isRequest = isRequest;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }
}
