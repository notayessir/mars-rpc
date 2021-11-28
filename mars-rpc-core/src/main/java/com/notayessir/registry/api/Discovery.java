package com.notayessir.registry.api;

import com.notayessir.rpc.api.bean.ServiceUnit;

import java.util.List;

/**
 * 注册中心 - 发现
 */
public interface Discovery {

    /**
     * 从服务中心拉取对应的服务
     * @param interfaceNames    消费端需要的服务
     * @return                  服务提供者的信息
     */
    List<ServiceUnit> discover(List<String> interfaceNames);


    /**
     * 监听服务提供者的变化
     * @param interfaceNames    需要监听的服务
     */
    void watch(List<String> interfaceNames);

    /**
     * 关闭客户端
     */
    void close();
}
