package com.notayessir.common.spring.event.provider;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.bean.ProviderRemoveEvent;
import com.notayessir.rpc.api.bean.InvokerMeta;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProviderNotifier {


    @EventListener
    public void process(ProviderRemoveEvent removeEvent) {
        InvokerMeta invokerMeta = removeEvent.getInvokerMeta();
        InvokerManager invokerManager = MarsRPCContext.getInvokerService();
        invokerManager.removeInvoker(invokerMeta);
    }



}
