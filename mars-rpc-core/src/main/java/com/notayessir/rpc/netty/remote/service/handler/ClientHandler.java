package com.notayessir.rpc.netty.remote.service.handler;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.channel.ChannelPublisher;
import com.notayessir.common.spring.event.keepalive.KeepAlivePublisher;
import com.notayessir.common.spring.event.provider.ProviderPublisher;
import com.notayessir.common.spring.event.response.FutureRespPublisher;
import com.notayessir.rpc.api.bean.InvokerMeta;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务消费端处理器
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    private ChannelHandlerContext ctx;

    /**
     * invoker 元信息
     */
    private final InvokerMeta invokerMeta;

    /**
     * channel hashcode
     */
    private int channelId;

    public ClientHandler(InvokerMeta invokerMeta) {
        this.invokerMeta = invokerMeta;
    }

    /**
     * 写入请求
     * @param requestFrame  请求帧
     */
    public void writeRequest(RequestFrame requestFrame) {
        ctx.writeAndFlush(requestFrame);
    }

    /**
     * 获取 channel hashcode
     * @return  channel hashcode
     */
    public int getChannelId() {
        return channelId;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        channelId = ctx.pipeline().channel().hashCode();
        MarsRPCContext.getBean(ChannelPublisher.class).publishActiveEvent(channelId);
        LOG.info("channelActive invoked, channelId: {}", channelId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseFrame responseFrame = (ResponseFrame) msg;
        MarsRPCContext.getBean(FutureRespPublisher.class).publishUpdateEvent(channelId, responseFrame);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("client channel exceptionCaught:{}, now close channel, channelId:{}", cause, ctx.channel().hashCode());
        MarsRPCContext.getBean(ProviderPublisher.class).publishRemoveEvent(invokerMeta);
        MarsRPCContext.getBean(ChannelPublisher.class).publishRemoveEvent(channelId);
        MarsRPCContext.getBean(KeepAlivePublisher.class).publishRemoveEvent(channelId);
        ctx.pipeline().channel().close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("client channel channelInactive, channelId:{}, now close channel.", channelId);
        MarsRPCContext.getBean(ProviderPublisher.class).publishRemoveEvent(invokerMeta);
        MarsRPCContext.getBean(ChannelPublisher.class).publishRemoveEvent(channelId);
        MarsRPCContext.getBean(KeepAlivePublisher.class).publishRemoveEvent(channelId);
        ctx.pipeline().channel().close();
    }


}
