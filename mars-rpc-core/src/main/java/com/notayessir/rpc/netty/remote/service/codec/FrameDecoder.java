package com.notayessir.rpc.netty.remote.service.codec;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.keepalive.KeepAlivePublisher;
import com.notayessir.rpc.netty.remote.bean.DecodeState;
import com.notayessir.rpc.netty.remote.bean.frame.HeartBeatFrame;
import com.notayessir.rpc.netty.remote.bean.frame.RequestBody;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import com.notayessir.rpc.netty.remote.util.FrameUtil;
import com.notayessir.serialize.api.ChannelInputStream;
import com.notayessir.serialize.api.ObjectInput;
import com.notayessir.serialize.api.Serialization;
import com.notayessir.rpc.netty.remote.bean.FrameConst;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 字节流入，将字节解析为请求帧/心跳帧
 */
public class FrameDecoder extends ByteToMessageDecoder {


    private DecodeState decodeState;

    public FrameDecoder() {
        this.decodeState = DecodeState.READ_MAGIC;
    }

    private void checkState(DecodeState state){
        this.decodeState = state;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (this.decodeState){
            case READ_MAGIC:
                if (in.readableBytes() < 2){
                    return;
                }
                byte magic = in.readByte();
                if (!FrameUtil.isCorrectMagic(magic)){
                    checkState(DecodeState.BAD_FRAME);
                    return;
                }
                checkState(DecodeState.READ_HEADER);
            case READ_HEADER:
                byte frameByte = in.readByte();
                in.readerIndex(in.readerIndex() - 1);
                if (FrameUtil.isHeartBeat(frameByte)){
                    checkState(DecodeState.READ_HEARTBEAT_FRAME);
                    break;
                }
                if (in.readableBytes() < FrameConst.HEADER_LEN - 1){
                    return;
                }
                // 非心跳帧处理，读索引回设到 frame byte 位置
                in.markReaderIndex();
                in.skipBytes(9);
                // 读取数据长度
                int contentLen = in.readInt();
                if (!FrameUtil.isAllowContentLen(contentLen)){
                    throw new RuntimeException("content length too long.");
                }
                int frameLen = FrameConst.HEADER_LEN - 1 + contentLen;
                in.resetReaderIndex();
                if (in.readableBytes() < frameLen){
                    return;
                }
                checkState(DecodeState.READ_REQUEST_RESPONSE_FRAME);
                break;
            case BAD_FRAME:
                System.out.println("BAD_FRAME");
                // 释放相关资源并关闭通道
                in.skipBytes(actualReadableBytes());
                ctx.close();
                return;
        }
        byte frameByte = in.readByte();
        switch (this.decodeState){
            case READ_HEARTBEAT_FRAME:
                // 处理心跳帧，实现为双向心跳
                if (!FrameUtil.isResponse(frameByte)){
                    ctx.writeAndFlush(new HeartBeatFrame(false));
                }else {
                    KeepAlivePublisher alivePublisher = MarsRPCContext.getBean(KeepAlivePublisher.class);
                    alivePublisher.publishResetEvent(ctx.pipeline().channel().hashCode());
                }
                checkState(DecodeState.READ_MAGIC);
                break;
            case READ_REQUEST_RESPONSE_FRAME:
                // 读取非心跳帧
                long requestId = in.readLong();
                int contentLen = in.readInt();
                ByteBuf contentByteBuf = in.readBytes(contentLen);
                ChannelInputStream inputStream = new ChannelInputStream(contentByteBuf);
                byte serializationId = FrameUtil.parseSerializationId(frameByte);
                Serialization serialization = Serialization.getSerialization(serializationId);
                ObjectInput objectInput = serialization.deserialize(inputStream);
                if (FrameUtil.isResponse(frameByte)){
                    // response
                    Object invocationResult = objectInput.readObject(Object.class);
                    ResponseFrame responseFrame = new ResponseFrame();
                    byte status = (byte) (frameByte & FrameConst.FLAG_STATUS_MASK);
                    responseFrame.setStatus(status);
                    responseFrame.setRequestId(requestId);
                    responseFrame.setInvocationResult(invocationResult);
                    out.add(responseFrame);
                }else {
                    // request
                    RequestBody requestBody = objectInput.readObject(RequestBody.class);
                    RequestFrame requestFrame = new RequestFrame();
                    requestFrame.setSerializationId(serializationId);
                    requestFrame.setRequestId(requestId);
                    requestFrame.setRequestBody(requestBody);
                    out.add(requestFrame);
                }
                contentByteBuf.release();
                break;
        }
        // 帧读取完毕，将 decode 状态置为 READ_MAGIC
        checkState(DecodeState.READ_MAGIC);
    }

}
