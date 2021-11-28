package com.notayessir.serialize.api;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;

/**
 * netty 管道输出流
 */
public class ChannelOutputStream extends OutputStream {

    private final ByteBuf byteBuf;

    private final int startIndex;

    public ChannelOutputStream(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        this.startIndex = byteBuf.writerIndex();
    }

    @Override
    public void write(int b) throws IOException {
        byteBuf.writeByte((byte) b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        byteBuf.writeBytes(b);
    }

    public int writtenBytes() {
        return byteBuf.writerIndex() - startIndex;
    }
}
