package com.notayessir.rpc.api.util;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.cluster.fault.ClusterMeta;
import com.notayessir.common.spring.event.provider.PingPongService;
import com.notayessir.registry.api.bean.Reference;
import com.notayessir.rpc.api.bean.FutureResponse;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.RPCException;
import com.notayessir.rpc.netty.remote.bean.ResponseStatus;
import com.notayessir.rpc.netty.remote.bean.frame.RequestBody;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 调用信息工具类
 */
public class InvocationUtil {

    /**
     * 构建请求帧
     * @param invocation    调用信息
     * @return              请求帧
     */
    public static RequestFrame toRequestFrame(Invocation invocation){
        RequestFrame frame = new RequestFrame();
        RequestBody requestBody = new RequestBody();
        requestBody.setInterfaceName(invocation.getMethod().getDeclaringClass().getName());
        requestBody.setMethodName(invocation.getMethod().getName());
        requestBody.setServiceName(invocation.getClusterMeta().getServiceName());
        Object[] args = invocation.getArgs();
        List<String> paramNames = new ArrayList<>(args.length);
        List<Object> paramValues = new ArrayList<>(args.length);
        for (Object arg : args) {
            paramNames.add(arg.getClass().getName());
            paramValues.add(arg);
        }
        requestBody.setParamNames(paramNames);
        requestBody.setParamValues(paramValues);
        frame.setRequestBody(requestBody);
        frame.setSerializationId(invocation.getSerializationId());
        frame.setRequestId(invocation.getRequestId());
        return frame;
    }

    /**
     * 构建调用结果
     * @param futureResponse    响应帧
     * @return                  调用结果
     * @throws RPCException     调用过程异常时抛出
     */
    public static Object toResponseObject(FutureResponse futureResponse) throws RPCException{
        ResponseFrame responseFrame = futureResponse.getResponseFrame();
        byte status = responseFrame.getStatus();
        ResponseStatus responseStatus = ResponseStatus.getByValue(status);
        switch (responseStatus){
            case SERVER_OK:
                return JSONObject.parseObject(JSONObject.toJSONString(responseFrame.getInvocationResult()), futureResponse.getMethodReturnType());
            case SERVER_BUSINESS_ERROR:
                throw new RPCException(RPCException.Message.SERVER_BUSINESS_ERROR);
            case SERVER_THREAD_POOL_BUSY:
                throw new RPCException(RPCException.Message.SERVER_THREAD_POOL_BUSY);
        }
        throw new RPCException(RPCException.Message.UNKNOWN_RESPONSE_STATUS);
    }

    /**
     * 构建响应帧
     * @param requestFrame  请求帧
     * @return              响应帧
     */
    public static ResponseFrame toResponseFrame(RequestFrame requestFrame){
        ResponseFrame responseFrame = new ResponseFrame();
        responseFrame.setRequestId(requestFrame.getRequestId());
        responseFrame.setSerializationId(requestFrame.getSerializationId());
        responseFrame.setStatus(ResponseStatus.SERVER_OK.getValue());
        return responseFrame;
    }

    /**
     * 根据调用方法/参数构建调用对象
     * @param method            调用的方法
     * @param args              调用的参数
     * @param reference         引用
     * @param requestId         请求 id
     * @param serializationId   序列化 id
     * @return                  调用对象
     */
    public static Invocation buildInvocation(Method method, Object[] args, Reference reference, long requestId, byte serializationId){
        Invocation invocation = new Invocation();
        invocation.setArgs(args);
        invocation.setMethod(method);
        invocation.setRequestId(requestId);
        invocation.setSerializationId(serializationId);
        Map<String, ClusterMeta> clusterMetaMap = reference.getClusterMetaMap();
        ClusterMeta clusterMeta = clusterMetaMap.get(method.getName());
        if (Objects.isNull(clusterMeta)){
            clusterMeta = reference.getClusterMeta();
        }
        invocation.setClusterMeta(clusterMeta);
        return invocation;
    }

    /**
     * 构建可用性检测调用对象
     * @param requestId         请求 id
     * @param serializationId   序列化 id
     * @return                  调用对象
     * @throws NoSuchMethodException    方法不存在时抛出
     */
    public static Invocation buildPingPongInvocation(long requestId, byte serializationId) throws NoSuchMethodException {
        Invocation invocation = new Invocation();
        Method method = PingPongService.class.getDeclaredMethod("ping");
        invocation.setMethod(method);
        invocation.setArgs(new Object[0]);
        invocation.setSerializationId(serializationId);
        invocation.setRequestId(requestId);
        ClusterMeta clusterMeta = new ClusterMeta();
        clusterMeta.setTimeout(6000L);
        invocation.setClusterMeta(clusterMeta);
        return invocation;
    }

}
