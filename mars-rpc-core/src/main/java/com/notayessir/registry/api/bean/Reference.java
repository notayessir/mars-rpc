package com.notayessir.registry.api.bean;

import com.notayessir.cluster.fault.ClusterMeta;
import com.notayessir.rpc.api.annotation.RPCReference;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 引用信息，保存 spring 中声明的远程引用信息
 */
public class Reference {

    /**
     * spring 管理的 bean
     */
    private final Object bean;

    /**
     * 由 {@link RPCReference} 注解的字段
     */
    private final Field field;

    /**
     *  集群调用信息
     */
    private final ClusterMeta clusterMeta;

    /**
     * 针对方法的定制化集群调用信息
     */
    private final Map<String, ClusterMeta> clusterMetaMap;

    public Reference(Object bean, Field field, ClusterMeta clusterMeta, Map<String, ClusterMeta> clusterMetaMap) {
        this.bean = bean;
        this.field = field;
        this.clusterMeta = clusterMeta;
        this.clusterMetaMap = clusterMetaMap;
    }

    public Object getBean() {
        return bean;
    }

    public Field getField() {
        return field;
    }

    public ClusterMeta getClusterMeta() {
        return clusterMeta;
    }

    public Map<String, ClusterMeta> getClusterMetaMap() {
        return clusterMetaMap;
    }

}
