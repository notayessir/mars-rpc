package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;


/**
 * Netty channel exception 事件
 */
public class ChannelRemoveEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    public ChannelRemoveEvent(Object source, int channelId) {
        super(source);
        this.channelId = channelId;
    }


    public int getChannelId() {
        return channelId;
    }
}
