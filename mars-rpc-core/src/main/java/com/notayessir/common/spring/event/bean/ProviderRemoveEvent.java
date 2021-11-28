package com.notayessir.common.spring.event.bean;

import com.notayessir.common.spring.event.provider.ProviderPublisher;
import com.notayessir.rpc.api.bean.InvokerMeta;
import org.springframework.context.ApplicationEvent;

/**
 * 服务提供者移除事件，在 Netty channel 异常时创建，用于移除 invoker
 */
public class ProviderRemoveEvent extends ApplicationEvent {

    /**
     * 该 channel 携带的服务提供者信息
     */
    private final InvokerMeta invokerMeta;

    public ProviderRemoveEvent(ProviderPublisher source, InvokerMeta invokerMeta) {
        super(source);
        this.invokerMeta = invokerMeta;
    }

    public InvokerMeta getInvokerMeta() {
        return invokerMeta;
    }
}
