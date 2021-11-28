package com.notayessir.rpc.api.bean;

import com.notayessir.cluster.fault.ClusterMeta;

import java.lang.reflect.Method;

/**
 * 消费端调用信息
 */
public class Invocation {

    /**
     * 调用的方法
     */
    private Method method;

    /**
     * 方法参数
     */
    private Object[] args;

    /**
     * 序列化 ID
     */
    private byte serializationId;

    /**
     * 唯一请求 id
     */
    private long requestId;

    /**
     * 集群配置信息
     */
    private ClusterMeta clusterMeta;

    public ClusterMeta getClusterMeta() {
        return clusterMeta;
    }

    public void setClusterMeta(ClusterMeta clusterMeta) {
        this.clusterMeta = clusterMeta;
    }

    public byte getSerializationId() {
        return serializationId;
    }

    public void setSerializationId(byte serializationId) {
        this.serializationId = serializationId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
