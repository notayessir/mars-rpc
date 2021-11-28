package com.notayessir.rpc.netty.remote.bean;

/**
 * 服务端响应码
 */
public enum ResponseStatus {

    /**
     * 请求处理成功
     */
    SERVER_OK((byte) 0x01),

    /**
     * 服务端逻辑异常
     */
    SERVER_BUSINESS_ERROR((byte) 0x03),

    /**
     * 服务端线程池异常
     */
    SERVER_THREAD_POOL_BUSY((byte) 0x05),

    /**
     * 未定义异常
     */
    UNKNOWN_STATUS((byte) 0x04);

    private final byte value;

    ResponseStatus(byte value) {
        this.value = value;
    }

    public static ResponseStatus getByValue(byte value){
        ResponseStatus[] values = ResponseStatus.values();
        for (ResponseStatus responseStatus : values){
            if (responseStatus.getValue() == value){
                return responseStatus;
            }
        }
        return UNKNOWN_STATUS;
    }

    public byte getValue() {
        return value;
    }
}
