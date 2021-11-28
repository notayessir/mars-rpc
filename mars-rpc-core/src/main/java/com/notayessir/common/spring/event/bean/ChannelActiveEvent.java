package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;

/**
 * Netty channel active 事件
 */
public class ChannelActiveEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    public ChannelActiveEvent(Object source, int channelId) {
        super(source);
        this.channelId = channelId;
    }

    public int getChannelId() {
        return channelId;
    }
}
