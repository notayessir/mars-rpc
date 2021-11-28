package com.notayessir.cluster.router.impl;

import com.notayessir.cluster.router.Router;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.InvokerMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的路由器，遍历服务列表找出所有符合条件且可用的服务提供者
 */
public class StandardRouter implements Router {


    @Override
    public List<Invoker> route(String interfaceName) {
        List<Invoker> invokers = MarsRPCContext.getInvokerService().getInvokers();
        List<Invoker> matchedInvokers = new ArrayList<>();
        for (Invoker invoker: invokers){
            InvokerMeta invokerMeta = invoker.getInvokerMeta();
            if (invokerMeta.contain(interfaceName) && invoker.isEnable()){
                matchedInvokers.add(invoker);
            }
        }
        return matchedInvokers;
    }
}
