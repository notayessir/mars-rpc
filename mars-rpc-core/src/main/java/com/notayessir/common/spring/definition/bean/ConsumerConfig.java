package com.notayessir.common.spring.definition.bean;

import com.notayessir.common.spring.definition.bean.base.BaseConfig;
import com.notayessir.common.spring.definition.bean.base.ClusterConfig;
import com.notayessir.common.spring.definition.bean.base.RegistryConfig;
import com.notayessir.common.spring.definition.bean.base.SerializeConfig;

/**
 * 服务消费者配置
 */
public class ConsumerConfig extends BaseConfig {

    /**
     * 序列化配置
     */
    private SerializeConfig serializeConfig;

    /**
     * 集群配置
     */
    private ClusterConfig clusterConfig;

    /**
     * 注册中心配置
     */
    private RegistryConfig discoveryConfig;

    public RegistryConfig getDiscoveryConfig() {
        return discoveryConfig;
    }

    public void setDiscoveryConfig(RegistryConfig discoveryConfig) {
        this.discoveryConfig = discoveryConfig;
    }

    public SerializeConfig getSerializeConfig() {
        return serializeConfig;
    }

    public void setSerializeConfig(SerializeConfig serializeConfig) {
        this.serializeConfig = serializeConfig;
    }

    public ClusterConfig getClusterConfig() {
        return clusterConfig;
    }

    public void setClusterConfig(ClusterConfig clusterConfig) {
        this.clusterConfig = clusterConfig;
    }
}
