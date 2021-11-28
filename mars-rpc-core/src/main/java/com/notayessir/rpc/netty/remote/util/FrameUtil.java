package com.notayessir.rpc.netty.remote.util;

import com.notayessir.rpc.netty.remote.bean.FrameConst;

/**
 * 帧工具类
 */
public class FrameUtil {

    /**
     * 是否是心跳帧
     * @param frameByte 帧字节
     * @return          是否是心跳帧
     */
    public static boolean isHeartBeat(byte frameByte){
        return (frameByte & FrameConst.FLAG_HEART_BEAT) == FrameConst.FLAG_HEART_BEAT;
    }

    /**
     * 是否响应帧
     * @param frameByte 帧字节
     * @return          是否响应帧
     */
    public static boolean isResponse(byte frameByte){
        return (frameByte & FrameConst.FLAG_REQUEST) != FrameConst.FLAG_REQUEST;
    }

    /**
     * 帧长度是否在范围内
     * @param contentLen    帧长度
     * @return              帧长度是否在范围内
     */
    public static boolean isAllowContentLen(int contentLen) {
        return contentLen < FrameConst.DEFAULT_CONTENT_LEN;
    }

    /**
     * 是否是正确的魔数
     * @param magicNum  魔术帧字节
     * @return          是否是正确的魔数
     */
    public static boolean isCorrectMagic(byte magicNum) {
        return (magicNum & FrameConst.MAGIC) == FrameConst.MAGIC;
    }

    /**
     * 消费端所使用的序列化框架
     * @param frameByte     帧字节
     * @return              序列化框架
     */
    public static byte parseSerializationId(byte frameByte){
        return (byte)(frameByte & FrameConst.FLAG_SERIALIZE_MASK);
    }

    /**
     * 根据帧类型、是否双向、序列化种类三个参数得出帧类型字节
     * @param serializeId   序列化种类
     * @return              帧类型字节
     */
    public static byte toReqFrameByte(int serializeId){
        byte frameByte =  (byte) serializeId;
        return (byte) (frameByte | FrameConst.FLAG_REQUEST);
    }

    /**
     * 根据帧类型、是否双向、序列化种类三个参数得出帧类型字节
     * @param serializeId   序列化种类
     * @return              帧类型字节
     */
    public static byte toRespFrameByte(int serializeId, byte respStatus){
        byte frameByte =  (byte) serializeId;
        frameByte = (byte) (frameByte | FrameConst.FLAG_RESPONSE);
        return (byte) (frameByte | respStatus);
    }
    
}
