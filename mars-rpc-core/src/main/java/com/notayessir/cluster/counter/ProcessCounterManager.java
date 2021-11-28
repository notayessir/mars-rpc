package com.notayessir.cluster.counter;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 服务提供者请求数管理器
 */
public class ProcessCounterManager {

    /**
     * 服务提供者 id 对应的当前处理数信息
     */
    private static final ConcurrentHashMap <Integer, ProcessCounter> map = new ConcurrentHashMap<>(64);

    /**
     * 根据 channelId 获取对应的处理数信息
     * @param channelId     netty channel id
     * @return              处理数信息
     */
    public static ProcessCounter get(int channelId){
        ProcessCounter processCounter = map.get(channelId);
        if (Objects.isNull(processCounter)){
            map.putIfAbsent(channelId, new ProcessCounter());
            processCounter = map.get(channelId);
        }
        return processCounter;
    }

}
