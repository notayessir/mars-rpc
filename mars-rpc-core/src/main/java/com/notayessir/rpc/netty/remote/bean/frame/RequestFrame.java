package com.notayessir.rpc.netty.remote.bean.frame;


import java.io.Serializable;

/**
 * 请求帧
 */
public class RequestFrame extends BaseFrame implements Serializable {

    private static final long serialVersionUID = 7229813112089476343L;

    /**
     * 请求 body
     */
    private RequestBody requestBody;


    public RequestFrame() {
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }
}
