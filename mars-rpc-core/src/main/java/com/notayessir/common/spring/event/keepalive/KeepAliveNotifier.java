package com.notayessir.common.spring.event.keepalive;

import com.notayessir.common.spring.event.bean.KeepAliveCountEvent;
import com.notayessir.common.spring.event.bean.KeepAliveRemoveEvent;
import com.notayessir.common.spring.event.bean.KeepAliveResetEvent;
import com.notayessir.rpc.netty.remote.counter.KeepAliveCounter;
import com.notayessir.rpc.netty.remote.counter.KeepAliveCounterManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 心跳事件处理服务
 */
@Component
public class KeepAliveNotifier {


    @EventListener
    public void process(KeepAliveRemoveEvent removeEvent) {
        KeepAliveCounterManager.removeCounter(removeEvent.getChannelId());
    }


    @EventListener
    public void process(KeepAliveCountEvent countEvent) {
        KeepAliveCounter counter = KeepAliveCounterManager.getCounter(countEvent.getChannelId());
        if (Objects.isNull(counter)){
            counter = new KeepAliveCounter();
            KeepAliveCounterManager.addCounter(countEvent.getChannelId(), counter);
            return;
        }
        counter.incr();
    }

    @EventListener
    public void process(KeepAliveResetEvent resetEvent) {
        KeepAliveCounter counter = KeepAliveCounterManager.getCounter(resetEvent.getChannelId());
        if (Objects.isNull(counter)){
            return;
        }
//        System.out.println("reset counter:"+counter.getCount());
        counter.reset();
    }


    public boolean isExist(int channelId){
        KeepAliveCounter counter = KeepAliveCounterManager.getCounter(channelId);
        return !Objects.isNull(counter);
    }

    public boolean isExceed(int channelId){
        KeepAliveCounter counter = KeepAliveCounterManager.getCounter(channelId);
        return counter.getCount() > 3;
    }


}
