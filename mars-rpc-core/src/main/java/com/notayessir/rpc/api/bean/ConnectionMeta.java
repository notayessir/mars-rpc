package com.notayessir.rpc.api.bean;

import com.notayessir.registry.api.bean.Service;

import java.util.List;

/**
 * 连接信息
 */
public class ConnectionMeta {

    /**
     * ip
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 服务信息
     */
    private List<Service> services;

    public ConnectionMeta(String host, Integer port, List<Service> services) {
        this.host = host;
        this.port = port;
        this.services = services;
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

    public List<Service> getServiceInfos() {
        return services;
    }

    public void setServiceInfos(List<Service> services) {
        this.services = services;
    }
}
