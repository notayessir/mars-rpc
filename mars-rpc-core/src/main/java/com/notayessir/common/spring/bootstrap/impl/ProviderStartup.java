package com.notayessir.common.spring.bootstrap.impl;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.bootstrap.Startup;
import com.notayessir.common.spring.definition.bean.ProviderConfig;
import com.notayessir.common.spring.definition.bean.base.ProtocolConfig;
import com.notayessir.common.spring.definition.bean.base.RegistryConfig;
import com.notayessir.registry.api.Registry;
import com.notayessir.registry.api.bean.ServiceCache;
import com.notayessir.registry.zookeeper.ZKRegistry;
import com.notayessir.rpc.api.Server;
import com.notayessir.rpc.netty.remote.NettyServer;

import java.util.concurrent.CountDownLatch;

/**
 * 服务提供者启动入口
 */
public class ProviderStartup implements Startup {

    /**
     * 对外提供的服务
     */
    private final ServiceCache serviceCache;

    /**
     * 服务提供者的配置
     */
    private final ProviderConfig providerConfig;

    public ProviderStartup(ServiceCache serviceCache, ProviderConfig providerConfig) {
        this.serviceCache = serviceCache;
        this.providerConfig = providerConfig;
    }

    @Override
    public void start() throws Exception {
        if (serviceCache.getServices().isEmpty()){
            return;
        }
        // 启动服务
        ProtocolConfig protocolConfig = providerConfig.getProtocolConfig();
        RegistryConfig registryConfig = providerConfig.getRegistryConfig();
        Server server = new NettyServer(protocolConfig.getPort());
        CountDownLatch prepareLatch = new CountDownLatch(1);
        new Thread(() -> server.start(prepareLatch)).start();
        // netty 服务器启动完成之前，阻塞在这
        prepareLatch.await();
        MarsRPCContext.setServer(server);
        // 根据设置决定是否将服务注册到注册中心
        if (protocolConfig.isExpose()){
            Registry registry = new ZKRegistry(registryConfig.getHost(), registryConfig.getPort());
            registry.register(serviceCache);
            MarsRPCContext.setRegistry(registry);
        }

    }


}
