package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;

/**
 * channel 无法保持心跳帧且达到一定重试次数时创建，用于释放 channel 资源
 */
public class KeepAliveRemoveEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    public KeepAliveRemoveEvent(Object source, int channelId) {
        super(source);
        this.channelId = channelId;
    }

    public int getChannelId() {
        return channelId;
    }
}
