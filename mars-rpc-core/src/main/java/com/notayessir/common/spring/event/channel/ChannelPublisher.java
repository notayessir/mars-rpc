package com.notayessir.common.spring.event.channel;

import com.notayessir.common.spring.event.bean.ChannelActiveEvent;
import com.notayessir.common.spring.event.bean.ChannelRemoveEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * channel 相关事件发布服务
 */
@Component
public class ChannelPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    public void publishActiveEvent(int channelId) {
        publisher.publishEvent(new ChannelActiveEvent(this, channelId));
    }

    /**
     * 移除该 channel 相关的 response 容器
     * @param channelId     channel id
     */
    public void publishRemoveEvent(int channelId) {
        publisher.publishEvent(new ChannelRemoveEvent(this, channelId));
    }


}
