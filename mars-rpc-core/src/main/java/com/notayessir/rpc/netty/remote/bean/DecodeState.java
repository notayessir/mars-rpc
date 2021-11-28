package com.notayessir.rpc.netty.remote.bean;

/**
 * 解码状态
 */
public enum DecodeState {


    /**
     * 读取魔数
     */
    READ_MAGIC,

    /**
     * 魔数错误等原因引起的错误帧
     */
    BAD_FRAME,

    /**
     * 读取帧头部
     */
    READ_HEADER,

    /**
     * 读取请求 - 响应帧
     */
    READ_REQUEST_RESPONSE_FRAME,

    /**
     * 读取心跳帧
     */
    READ_HEARTBEAT_FRAME

}
