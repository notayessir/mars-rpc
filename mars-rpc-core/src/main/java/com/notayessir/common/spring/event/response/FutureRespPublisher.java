package com.notayessir.common.spring.event.response;

import com.notayessir.common.spring.event.bean.FutureRespCreateEvent;
import com.notayessir.common.spring.event.bean.FutureRespReleaseEvent;
import com.notayessir.common.spring.event.bean.FutureRespRemoveEvent;
import com.notayessir.common.spring.event.bean.FutureRespUpdateEvent;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


/**
 * 请求/响应事件发布服务
 */
@Component
public class FutureRespPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    public void publishCreateEvent(int channelId, Invocation invocation, CountDownLatch latch) {
        publisher.publishEvent(new FutureRespCreateEvent(this, channelId, invocation, latch));
    }

    public void publishRemoveEvent(int channelId, long requestId) {
        publisher.publishEvent(new FutureRespRemoveEvent(this, channelId, requestId));
    }

    public void publishUpdateEvent(int channelId, ResponseFrame responseFrame) {
        publisher.publishEvent(new FutureRespUpdateEvent(this, channelId, responseFrame));
    }

    public void publishReleaseEvent(int channelId, long requestId) {
        publisher.publishEvent(new FutureRespReleaseEvent(this, channelId, requestId));
    }



}
