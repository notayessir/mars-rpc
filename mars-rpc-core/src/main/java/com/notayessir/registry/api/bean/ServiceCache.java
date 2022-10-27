package com.notayessir.registry.api.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存在服务提供者的服务列表
 */
public class ServiceCache {

    /**
     * 服务列表
     */
    private final List<Service> services;

    public ServiceCache() {
        services = new ArrayList<>(64);
    }

    public void addService(Service service){
        boolean exist = false;
        for (Service s : services){
            // 一个接口可能有多个实现，当遇到相同的接口实现时，只更新具体实现类
            if (StringUtils.equalsIgnoreCase(s.getInterfaceName(), service.getInterfaceName())){
                s.getServiceNames().addAll(service.getServiceNames());
                exist = true;
                break;
            }
        }
        if (!exist){
            services.add(service);
        }
    }

    public List<Service> getServices() {
        return services;
    }
}
