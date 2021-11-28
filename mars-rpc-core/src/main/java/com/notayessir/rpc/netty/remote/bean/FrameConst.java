package com.notayessir.rpc.netty.remote.bean;

/**
 * 帧常量
 */
public interface FrameConst {


    /**
     * 魔数
     * 字节索引：1
     */
    byte MAGIC = 0x7F;

    /**
     * 数据类型、序列化类型：REQ/RESP(1 bit) - EVENT(1 bit) - SERIALIZE(3 bit) - STATUS(3 bit)
     * 字节索引：2
     */
    byte FLAG_REQUEST = (byte) 0x80;
    byte FLAG_RESPONSE = (byte) 0x00;
    byte FLAG_HEART_BEAT = (byte) 0x40;
    byte FLAG_SERIALIZE_MASK = (byte) 0x38;
    byte FLAG_STATUS_MASK = (byte) 0x07;

    /**
     * 帧头部长度
     */
    int HEADER_LEN = 14;

    /**
     * 请求 body 允许长度
     */
    int DEFAULT_CONTENT_LEN = 1024 * 1024 * 8;

}
