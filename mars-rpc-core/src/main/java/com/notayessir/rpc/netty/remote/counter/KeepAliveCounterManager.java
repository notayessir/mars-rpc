package com.notayessir.rpc.netty.remote.counter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 保活管理器
 */
public class KeepAliveCounterManager {

    private static final ConcurrentHashMap<Integer, KeepAliveCounter> map = new ConcurrentHashMap<>();

    public static KeepAliveCounter getCounter(int channelId){
        return map.get(channelId);
    }

    public static void addCounter(int channelId, KeepAliveCounter counter){
        map.put(channelId, counter);
    }

    public static void removeCounter(int channelId){
        map.remove(channelId);
    }


}
