package com.notayessir.common.spring.event.keepalive;

import com.notayessir.common.spring.event.bean.KeepAliveCountEvent;
import com.notayessir.common.spring.event.bean.KeepAliveRemoveEvent;
import com.notayessir.common.spring.event.bean.KeepAliveResetEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * 心跳事件发布服务
 */
@Component
public class KeepAlivePublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    /**
     * 移除保活计数器
     * @param channelId     channel id
     */
    public void publishRemoveEvent(int channelId) {
        publisher.publishEvent(new KeepAliveRemoveEvent(this, channelId));
    }

    /**
     * 心跳计数
     * @param channelId     channel id
     */
    public void publishCountEvent(int channelId) {
        publisher.publishEvent(new KeepAliveCountEvent(this, channelId));
    }


    /**
     * 重置心跳
     * @param channelId    channel id
     */
    public void publishResetEvent(int channelId) {
        publisher.publishEvent(new KeepAliveResetEvent(this, channelId));
    }
}
