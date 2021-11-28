package com.notayessir.rpc.netty.remote.service.codec;

import com.notayessir.rpc.netty.remote.bean.FrameConst;
import com.notayessir.rpc.netty.remote.bean.frame.HeartBeatFrame;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import com.notayessir.rpc.netty.remote.util.FrameUtil;
import com.notayessir.serialize.api.ChannelOutputStream;
import com.notayessir.serialize.api.ObjectOutput;
import com.notayessir.serialize.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 字节流出，将响应帧/心跳帧编码成字节
 */
public class FrameEncoder extends MessageToByteEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof HeartBeatFrame){
            HeartBeatFrame requestFrame = (HeartBeatFrame) msg;
            writeHeartbeat(out, requestFrame.isRequest());
            return;
        }
        //  帧编码
        byte frameByte;
        long requestId;
        Object body;
        if (msg instanceof RequestFrame){
            RequestFrame requestFrame = (RequestFrame) msg;
            requestId = requestFrame.getRequestId();
            body = requestFrame.getRequestBody();
            frameByte = FrameUtil.toReqFrameByte(requestFrame.getSerializationId());
        }else {
            ResponseFrame responseFrame = (ResponseFrame) msg;
            requestId = responseFrame.getRequestId();
            body = responseFrame.getInvocationResult();
            frameByte = FrameUtil.toRespFrameByte(responseFrame.getSerializationId(), responseFrame.getStatus());
        }
        // 移动写索引，先写 body，再写 header
        int headerIndex = out.writerIndex();
        out.writerIndex(headerIndex + FrameConst.HEADER_LEN);
        ChannelOutputStream outputStream = new ChannelOutputStream(out);
        Serialization serialization = Serialization.getSerialization(FrameUtil.parseSerializationId(frameByte));
        ObjectOutput objectOutput = serialization.serialize(outputStream);
        objectOutput.writeObject(body);
        objectOutput.flushBuffer();
        int len = outputStream.writtenBytes();
        // 重置 header 写索引
        out.writerIndex(headerIndex);
        // 魔数
        out.writeByte(FrameConst.MAGIC);
        // frameByte
        out.writeByte(frameByte);
        // requestId
        out.writeLong(requestId);
        // bodyLength
        out.writeInt(len);
        // header/body 写入完成，重置写索引
        out.writerIndex(headerIndex + len + FrameConst.HEADER_LEN);

    }

    /**
     * 返回心跳帧
     * @param out           字节缓存区
     * @param isRequest     心跳帧类型，true 代表请求心跳帧， false 代表响应心跳帧
     */
    private void writeHeartbeat(ByteBuf out, boolean isRequest){
        // 设置心跳是请求还是响应
        byte frameByte;
        if (isRequest){
            frameByte = (byte)(FrameConst.FLAG_REQUEST | FrameConst.FLAG_HEART_BEAT);
        }else {
            frameByte = (byte)(FrameConst.FLAG_RESPONSE | FrameConst.FLAG_HEART_BEAT);
        }
        out.writeByte(FrameConst.MAGIC);
        out.writeByte(frameByte);
    }

}
