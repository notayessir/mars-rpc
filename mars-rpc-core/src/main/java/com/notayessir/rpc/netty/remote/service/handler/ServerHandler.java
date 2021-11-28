package com.notayessir.rpc.netty.remote.service.handler;

import com.notayessir.rpc.api.dispatcher.ReqDispatcher;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 服务提供者处理器
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final Logger LOG = LogManager.getLogger(this.getClass());


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info("server channel channelActive:{}", ctx.channel().hashCode());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestFrame requestFrame = (RequestFrame) msg;
        ResponseFrame responseFrame = ReqDispatcher.dispatch(requestFrame);
        ctx.writeAndFlush(responseFrame);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("server channel exceptionCaught:{}, now close channel, channel id:{}", cause, ctx.channel().hashCode());
        ctx.pipeline().channel().close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("server channel channelInactive:{} now close channel.", ctx.channel().hashCode());
        ctx.pipeline().channel().close();
    }
}
