package com.notayessir.registry.api;

import com.notayessir.registry.api.bean.Service;
import com.notayessir.registry.api.bean.ServiceCache;

import java.util.List;

/**
 * 注册中心 - 注册
 */
public interface Registry {

    /**
     * 将提供的服务注册到注册中心
     * @param serviceCache   要提供的服务
     */
    void register(ServiceCache serviceCache)  ;

    /**
     * 将提供的服务从注册中心注销
     * @param services      要注销的服务
     */
    void unregister(List<Service> services)  ;

    /**
     * 关闭客户端
     */
    void close();
}
