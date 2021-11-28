package com.notayessir.common.spring.event.bean;

import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import org.springframework.context.ApplicationEvent;

/**
 * Netty channel read 事件，用于通知客户端，响应以接受完成
 */
public class FutureRespUpdateEvent extends ApplicationEvent {

    /**
     * channel hashcode
     */
    private final int channelId;

    /**
     * 响应帧
     */
    private final ResponseFrame responseFrame;

    public FutureRespUpdateEvent(Object source, int channelId, ResponseFrame responseFrame) {
        super(source);
        this.channelId = channelId;
        this.responseFrame = responseFrame;
    }

    public int getChannelId() {
        return channelId;
    }

    public ResponseFrame getResponseFrame() {
        return responseFrame;
    }
}
