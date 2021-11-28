package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;

/**
 * 用于重置心跳计数器，当 Netty 读到心跳帧时创建
 */
public class KeepAliveResetEvent extends ApplicationEvent {

    private final int channelId;

    public KeepAliveResetEvent(Object source, int channelId) {
        super(source);
        this.channelId = channelId;
    }

    public int getChannelId() {
        return channelId;
    }

}
