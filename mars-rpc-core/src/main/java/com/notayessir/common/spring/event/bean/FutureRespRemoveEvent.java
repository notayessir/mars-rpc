package com.notayessir.common.spring.event.bean;

import org.springframework.context.ApplicationEvent;

/**
 * RPC 调用正常结束时创建该事件，用于释放资源
 */
public class FutureRespRemoveEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    /**
     * 请求 id
     */
    private final long requestId;

    public FutureRespRemoveEvent(Object source, int channelId, long requestId) {
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
