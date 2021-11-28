package com.notayessir.rpc.api.bean;

import com.notayessir.registry.api.bean.Service;

import java.util.List;

/**
 * 保存服务信息的容器
 */
public class ServiceUnit {

    private List<Service> services;

    public ServiceUnit(List<Service> services) {
        this.services = services;
    }

    public List<Service> getServiceInfos() {
        return services;
    }

    public void setServiceInfos(List<Service> services) {
        this.services = services;
    }
}
