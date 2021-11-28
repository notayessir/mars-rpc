package com.notayessir.rpc.netty.remote.service.initializer;

import com.notayessir.rpc.netty.remote.service.codec.FrameDecoder;
import com.notayessir.rpc.netty.remote.service.codec.FrameEncoder;
import com.notayessir.rpc.netty.remote.service.handler.HeartbeatHandler;
import com.notayessir.rpc.netty.remote.service.handler.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final static EventExecutorGroup group = new DefaultEventExecutorGroup(8);

    public ServerInitializer() {
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 0, 10, TimeUnit.SECONDS));
        pipeline.addLast("codecEncoder", new FrameEncoder());
        pipeline.addLast("codecDecoder", new FrameDecoder());
        pipeline.addLast(group, "serverHandler", new ServerHandler());
        pipeline.addLast("heartBeatHandler", new HeartbeatHandler());


    }
}
