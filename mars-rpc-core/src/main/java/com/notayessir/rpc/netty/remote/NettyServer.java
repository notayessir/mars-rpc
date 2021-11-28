package com.notayessir.rpc.netty.remote;

import com.notayessir.rpc.api.Server;
import com.notayessir.rpc.netty.remote.service.initializer.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;


/**
 * netty 服务端
 */
public class NettyServer implements Server {

    private final static Logger LOG = LogManager.getLogger(NettyServer.class);

    /**
     * 监听端口
     */
    private final int port;

    /**
     * 主线程
     */
    private final EventLoopGroup bossGroup;

    /**
     * worker 线程
     */
    private final EventLoopGroup workerGroup;

    public NettyServer(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
    }



    @Override
    public void start(CountDownLatch prepareLatch) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerInitializer());
        try {
            ChannelFuture channelFuture = bootstrap.bind(this.port).sync();
            prepareLatch.countDown();
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            LOG.error("error happened in NettyServer, ", e);
            e.printStackTrace();
        }

    }


    @Override
    public void close(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


}
