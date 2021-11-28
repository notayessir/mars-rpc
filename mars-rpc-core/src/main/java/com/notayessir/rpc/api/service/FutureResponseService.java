package com.notayessir.rpc.api.service;

import com.notayessir.rpc.api.bean.FutureResponse;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 请求/响应管理对象
 */
public class FutureResponseService {

    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Long, FutureResponse>> map = new ConcurrentHashMap<>();

    /**
     * 获取响应对象
     * @param channelId     channel id
     * @param requestId     请求 id
     * @return              响应对象
     */
    public static FutureResponse getResponse(Integer channelId, Long requestId){
        // channel 可能会因为 decode 异常提前释放，需要防止 NPE
        ConcurrentHashMap<Long, FutureResponse> channelMap = map.get(channelId);
        if (Objects.isNull(channelMap)){
            return null;
        }
        return map.get(channelId).get(requestId);
    }

    /**
     * 当服务端成功返回响应时，移除响应对象
     * @param channelId channel id
     * @param requestId 请求 id
     */
    public static void removeResponse(Integer channelId, Long requestId) {
        map.get(channelId).remove(requestId);
    }

    /**
     * 创建请求对象
     * @param channelId         channel id
     * @param futureResponse    预响应结果
     */
    public static void addResponse(Integer channelId, FutureResponse futureResponse){
        map.get(channelId).put(futureResponse.getRequestId(), futureResponse);
    }

    /**
     * 服务端返回响应时，更新响应结果
     * @param channelId         channel id
     * @param responseFrame     响应帧
     */
    public static void updateResponse(Integer channelId, ResponseFrame responseFrame){
        FutureResponse futureResponse = map.get(channelId).get(responseFrame.getRequestId());
        if (Objects.isNull(futureResponse)){
            return;
        }
        futureResponse.setResponseFrame(responseFrame);
        futureResponse.getLatch().countDown();
    }

    /**
     * 调用异常时，释放请求资源
     * @param channelId     channel id
     * @param requestId     请求 id
     */
    public static void releaseResponse(Integer channelId, Long requestId){
        // channel 可能会因为 decode 异常提前释放，需要防止 NPE
        ConcurrentHashMap<Long, FutureResponse> channelMap = map.get(channelId);
        if (Objects.isNull(channelMap)){
            return;
        }
        FutureResponse futureResponse = channelMap.get(requestId);
        futureResponse.getLatch().countDown();
        removeResponse(channelId, requestId);
    }

    /**
     * netty channel 创建时，创建请求容器
     * @param channelId     创建的 channel id
     */
    public static void createResponseMap(int channelId){
        map.put(channelId, new ConcurrentHashMap<>());
    }

    /**
     * channel 异常关闭时，释放关于该 channel 所有的请求对象
     * @param channelId     被关闭的 channel id
     */
    public static void removeResponseMap(int channelId){
        ConcurrentHashMap<Long, FutureResponse> responseMap = map.get(channelId);
        if (Objects.isNull(responseMap) || responseMap.isEmpty()){
            return;
        }
        Collection<FutureResponse> responses = responseMap.values();
        for (FutureResponse response: responses){
            CountDownLatch latch = response.getLatch();
            if (latch.getCount() > 0){
                latch.countDown();
            }
        }
        map.remove(channelId);
    }

}
