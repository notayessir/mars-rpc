package com.notayessir.rpc.netty.remote.service.initializer;

import com.notayessir.rpc.netty.remote.service.codec.FrameDecoder;
import com.notayessir.rpc.netty.remote.service.codec.FrameEncoder;
import com.notayessir.rpc.netty.remote.service.handler.ClientHandler;
import com.notayessir.rpc.netty.remote.service.handler.HeartbeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {


    private final ClientHandler clientHandler;

    public ClientInitializer(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 客户端 5 秒内没有读写请求，就开始心跳
        pipeline.addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
        pipeline.addLast("codecEncoder", new FrameEncoder());
        pipeline.addLast("codecDecoder", new FrameDecoder());
        pipeline.addLast("clientHandler", clientHandler);
        pipeline.addLast("heartBeatHandler", new HeartbeatHandler());
    }
}
