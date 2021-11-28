package com.notayessir.common.spring.event.channel;

import com.notayessir.common.spring.event.bean.ChannelActiveEvent;
import com.notayessir.common.spring.event.bean.ChannelRemoveEvent;
import com.notayessir.rpc.api.service.FutureResponseService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * channel 相关事件处理服务
 */
@Component
public class ChannelNotifier {



    @EventListener
    public void process(ChannelActiveEvent channelActiveEvent){
        FutureResponseService.createResponseMap(channelActiveEvent.getChannelId());
    }


    @EventListener
    public void process(ChannelRemoveEvent channelRemoveEvent){
        FutureResponseService.removeResponseMap(channelRemoveEvent.getChannelId());
    }


}
