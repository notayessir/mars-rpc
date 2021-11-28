package com.notayessir.registry.api.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.Set;

/**
 * 服务提供者暴露的服务信息
 */
public class Service {

    /**
     * 协议，目前只有 mars
     */
    @JSONField(ordinal = 1)
    private String protocol;

    /**
     * ip
     */
    @JSONField(ordinal = 2)
    private String host;

    /**
     * 端口
     */
    @JSONField(ordinal = 3)
    private Integer port;

    /**
     * 接口名称
     */
    @JSONField(ordinal = 4)
    private String interfaceName;

    /**
     * 权重，负载均衡中使用
     */
    @JSONField(ordinal = 5)
    private Integer weight;

    /**
     * 实现类服务名
     */
    @JSONField(ordinal = 6)
    private Set<String> serviceNames;

    /**
     * 提供的方法
     */
    @JSONField(ordinal = 7)
    private Set<String> methods;


    public Service() {
    }


    public Set<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(Set<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}
