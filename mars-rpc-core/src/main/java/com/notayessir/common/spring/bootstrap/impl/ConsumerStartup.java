package com.notayessir.common.spring.bootstrap.impl;

import com.notayessir.common.proxy.Proxy;
import com.notayessir.common.proxy.impl.CGLibProxy;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.bootstrap.Startup;
import com.notayessir.common.spring.definition.bean.ConsumerConfig;
import com.notayessir.common.spring.event.provider.InvokerManager;
import com.notayessir.registry.api.Discovery;
import com.notayessir.registry.api.bean.ReferenceCache;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.registry.zookeeper.ZKDiscovery;
import com.notayessir.rpc.api.Connector;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.ConnectionMeta;
import com.notayessir.rpc.api.bean.ServiceUnit;
import com.notayessir.rpc.netty.NettyConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务消费者启动入口
 */
public class ConsumerStartup implements Startup {

    /**
     * 需要订阅的服务
     */
    private final ReferenceCache referenceCache;

    /**
     * 服务消费者的配置信息
     */
    private final ConsumerConfig consumerConfig;


    public ConsumerStartup(ReferenceCache referenceCache, ConsumerConfig consumerConfig) {
        this.referenceCache = referenceCache;
        this.consumerConfig = consumerConfig;
    }

    @Override
    public void start() throws Exception {
        if (referenceCache.isEmpty()) {
            return;
        }
        // 为接口创建代理
        Proxy proxy = new CGLibProxy();
        proxy.gen(referenceCache);
        // 从注册中心获取所需的服务提供者信息
        Discovery discovery = new ZKDiscovery(consumerConfig.getDiscoveryConfig().getHost(), consumerConfig.getDiscoveryConfig().getPort());
        List<String> interfaceNames = referenceCache.getInterfaceNames();
        List<ServiceUnit> serviceUnits = discovery.discover(interfaceNames);
        List<ConnectionMeta> connectionMetas = mergeServiceInfo(serviceUnits);
        // 创建连接
        Connector connector = new NettyConnector();
        List<Invoker> invokers = connector.connect(connectionMetas);
        // 创建并设置连接管理器
        MarsRPCContext.setInvokerService(new InvokerManager(invokers));
        // 监听服务提供者上下线信息
        discovery.watch(interfaceNames);
        MarsRPCContext.setDiscovery(discovery);

    }

    /**
     * 根据 ip 和端口合并多个服务，为后续创建连接去重
     * @param serviceUnits      从注册中心获取下来的服务信息
     * @return                  去重后的待连接信息
     */
    private List<ConnectionMeta> mergeServiceInfo(List<ServiceUnit> serviceUnits) {
        Map<String, List<Service>> map = new HashMap<>();
        for (ServiceUnit serviceUnit : serviceUnits) {
            List<Service> services = serviceUnit.getServiceInfos();
            for (Service service : services) {
                String key = service.getHost() + ":" + service.getPort();
                if (map.containsKey(key)) {
                    map.get(key).add(service);
                    continue;
                }
                List<Service> list = new ArrayList<>(32);
                list.add(service);
                map.put(key, list);
            }
        }
        List<ConnectionMeta> connectionMetas = new ArrayList<>(map.size());
        for (Map.Entry<String, List<Service>> entry : map.entrySet()) {
            String[] hostPort = entry.getKey().split(":");
            connectionMetas.add(new ConnectionMeta(hostPort[0], Integer.parseInt(hostPort[1]), entry.getValue()));
        }
        return connectionMetas;
    }


}
