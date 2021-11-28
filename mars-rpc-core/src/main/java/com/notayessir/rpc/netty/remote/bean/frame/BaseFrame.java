package com.notayessir.rpc.netty.remote.bean.frame;

/**
 * 基础帧
 */
public class BaseFrame  {


    /**
     * 请求 ID
     */
    protected long requestId;

    /**
     * 序列化 ID
     */
    protected byte serializationId;

    public byte getSerializationId() {
        return serializationId;
    }

    public void setSerializationId(byte serializationId) {
        this.serializationId = serializationId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
