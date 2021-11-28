package com.notayessir.rpc.api.bean;


import com.notayessir.registry.api.bean.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * invoker 信息
 */
public class InvokerMeta {

    /**
     * ip
     */
    private final String host;

    /**
     * 端口
     */
    private final Integer port;


    /**
     * 权重
     */
    private final Integer weight;

    /**
     * 服务信息
     */
    private final Map<String, Service> map;


    public InvokerMeta(ConnectionMeta connectionMeta) {
        host = connectionMeta.getHost();
        port = connectionMeta.getPort();
        List<Service> services = connectionMeta.getServiceInfos();
        weight = services.get(0).getWeight();
        map = buildIndexMap(services);
    }


    private Map<String, Service> buildIndexMap(List<Service> services){
        Map<String, Service> map = new HashMap<>();
        for (Service service : services){
            map.put(service.getInterfaceName(), service);
        }
        return map;
    }


    public String getHost() {
        return host;
    }


    public Integer getPort() {
        return port;
    }


    public Integer getWeight() {
        return weight;
    }


    public boolean contain(String interfaceName) {
        return map.containsKey(interfaceName);
    }

    public void put(Service service){
        map.putIfAbsent(service.getInterfaceName(), service);
    }

}
