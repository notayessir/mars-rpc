package com.notayessir.rpc.netty.remote;

import com.notayessir.rpc.api.bean.InvokerMeta;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.service.handler.ClientHandler;
import com.notayessir.rpc.netty.remote.service.initializer.ClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty 客户端
 */
public class NettyClient {

    /**
     * 服务端监听端口
     */
    private final int port;

    /**
     * 服务端 ip
     */
    private final String host;

    /**
     * 工作线程
     */
    private final EventLoopGroup group;

    /**
     * 服务端处理器
     */
    private final ClientHandler clientHandler;

    public NettyClient(InvokerMeta invokerMeta) {
        port = invokerMeta.getPort();
        host = invokerMeta.getHost();
        group = new NioEventLoopGroup();
        clientHandler = new ClientHandler(invokerMeta);
    }

    /**
     * 唯一标识
     * @return  唯一标识
     */
    public int getChannelId(){
        return clientHandler.getChannelId();
    }

    /**
     * 向服务端写入请求
     * @param requestFrame  请求帧
     */
    public void writeRequest(RequestFrame requestFrame){
        clientHandler.writeRequest(requestFrame);
    }

    /**
     * 启动连接
     * @throws Exception    连接异常时抛出
     */
    public void start() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientInitializer(clientHandler));
        ChannelFuture f = bootstrap.connect(this.host, this.port).sync();
//            f.channel().closeFuture().sync();
    }

    /**
     * 停止客户端端
     */
    public void close() {
        group.shutdownGracefully();
    }




}
