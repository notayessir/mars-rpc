package com.notayessir.cluster.router;

import com.notayessir.rpc.api.Invoker;

import java.util.List;

/**
 * 服务提供者的路由器
 */
public interface Router {

    /**
     * 根据接口名找出对应的服务提供者
     * @param interfaceName     接口名
     * @return                  服务提供者
     */
    List<Invoker> route(String interfaceName);

}
