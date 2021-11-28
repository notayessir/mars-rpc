package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;

/**
 * 心跳保活计数，当触发 Netty IdleStateEvent 时创建
 */
public class KeepAliveCountEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    public KeepAliveCountEvent(Object source, int channelId) {
        super(source);
        this.channelId = channelId;
    }

    public int getChannelId() {
        return channelId;
    }

}
