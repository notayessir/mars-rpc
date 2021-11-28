package com.notayessir.serialize.api;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;

/**
 * netty 管道读入流
 */
public class ChannelInputStream extends InputStream {

    private final ByteBuf byteBuf;

    public ChannelInputStream(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public int read() throws IOException {
        if (!byteBuf.isReadable()){
            return -1;
        }
        return byteBuf.readByte();
    }
}
