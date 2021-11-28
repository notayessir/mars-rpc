package com.notayessir.common.spring.definition.bean.base;

import com.notayessir.registry.api.bean.RegistryConst;

/**
 * 服务消费者、提供者的注册中心配置
 */
public class RegistryConfig {
    /**
     * 注册中心
     */
    protected RegistryConst.Registry registry;

    /**
     * 注册中心 host
     */
    protected String host;

    /**
     * 注册中心端口
     */
    protected int port;

    public RegistryConst.Registry getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConst.Registry registry) {
        this.registry = registry;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
