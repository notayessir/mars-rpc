package com.notayessir.common.spring.event.response;


import com.notayessir.common.spring.event.bean.FutureRespCreateEvent;
import com.notayessir.common.spring.event.bean.FutureRespReleaseEvent;
import com.notayessir.common.spring.event.bean.FutureRespRemoveEvent;
import com.notayessir.common.spring.event.bean.FutureRespUpdateEvent;
import com.notayessir.rpc.api.bean.FutureResponse;
import com.notayessir.rpc.api.service.FutureResponseService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 请求/响应相关事件处理服务
 */
@Component
public class FutureRespNotifier {

    @EventListener
    public void process(FutureRespCreateEvent futureRespCreateEvent){
        FutureResponse response = new FutureResponse(futureRespCreateEvent.getRequestId(), futureRespCreateEvent.getCountDownLatch(), futureRespCreateEvent.getMethodReturnType());
        FutureResponseService.addResponse(futureRespCreateEvent.getChannelId(), response);
    }

    @EventListener
    public void process(FutureRespRemoveEvent futureRespRemoveEvent){
        FutureResponseService.removeResponse(futureRespRemoveEvent.getChannelId(), futureRespRemoveEvent.getRequestId());
    }

    @EventListener
    public void process(FutureRespUpdateEvent futureRespUpdateEvent){
        FutureResponseService.updateResponse(futureRespUpdateEvent.getChannelId(), futureRespUpdateEvent.getResponseFrame());
    }


    @EventListener
    public void process(FutureRespReleaseEvent futureRespReleaseEvent){
        FutureResponseService.releaseResponse(futureRespReleaseEvent.getChannelId(), futureRespReleaseEvent.getRequestId());
    }


    public FutureResponse getResponse(int channelId, long requestId){
        return FutureResponseService.getResponse(channelId, requestId);
    }

}
