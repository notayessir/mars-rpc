package com.notayessir.rpc.netty.remote.bean.frame;



import java.io.Serializable;

/**
 * 响应帧
 */
public class ResponseFrame extends BaseFrame implements Serializable {

    private static final long serialVersionUID = -4992817405132823668L;

    /**
     * 响应码
     */
    private byte status;

    /**
     * 响应 body
     */
    private Object invocationResult;


    public ResponseFrame() {
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }


    public Object getInvocationResult() {
        return invocationResult;
    }

    public void setInvocationResult(Object invocationResult) {
        this.invocationResult = invocationResult;
    }

}
