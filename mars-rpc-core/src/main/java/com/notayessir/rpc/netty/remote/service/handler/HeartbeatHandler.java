package com.notayessir.rpc.netty.remote.service.handler;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.keepalive.KeepAliveNotifier;
import com.notayessir.common.spring.event.keepalive.KeepAlivePublisher;
import com.notayessir.rpc.netty.remote.bean.frame.HeartBeatFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * 保活处理器
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 次数统计
        if (evt instanceof IdleStateEvent){
            LOG.info("sending heartbeat frame to check alive {}", new Date().toLocaleString());
            KeepAliveNotifier aliveNotifier = MarsRPCContext.getBean(KeepAliveNotifier.class);
            KeepAlivePublisher alivePublisher = MarsRPCContext.getBean(KeepAlivePublisher.class);
            int channelId = ctx.pipeline().channel().hashCode();
            if (aliveNotifier.isExist(channelId) && aliveNotifier.isExceed(channelId)){
                LOG.info("lose connection, publishRemoveEvent and close channel, channelId:{}", channelId);
                alivePublisher.publishRemoveEvent(channelId);
                ctx.pipeline().channel().close();
                return;
            }
            alivePublisher.publishCountEvent(channelId);
            ctx.writeAndFlush(new HeartBeatFrame(true));

        }
    }
}
