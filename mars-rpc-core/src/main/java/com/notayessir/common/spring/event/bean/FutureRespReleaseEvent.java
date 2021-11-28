package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;

/**
 * RPC 调用过程中出现异常时创建该事件，用于释放资源
 */
public class FutureRespReleaseEvent extends ApplicationEvent {

    private final int channelId;

    private final long requestId;

    public FutureRespReleaseEvent(Object source, int channelId, long requestId) {
        super(source);
        this.channelId = channelId;
        this.requestId = requestId;
    }


    public int getChannelId() {
        return channelId;
    }

    public long getRequestId() {
        return requestId;
    }
}