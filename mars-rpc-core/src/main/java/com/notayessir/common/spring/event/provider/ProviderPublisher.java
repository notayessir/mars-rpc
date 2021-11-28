package com.notayessir.common.spring.event.provider;

import com.notayessir.common.spring.event.bean.ProviderRemoveEvent;
import com.notayessir.rpc.api.bean.InvokerMeta;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * 服务提供者事件发布服务
 */
@Component
public class ProviderPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    /**
     * 移除 invoker
     * @param invokerMeta   invoker 元信息
     */
    public void publishRemoveEvent(InvokerMeta invokerMeta) {
        publisher.publishEvent(new ProviderRemoveEvent(this, invokerMeta));

    }

}
