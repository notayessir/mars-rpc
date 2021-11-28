package com.notayessir.registry.zookeeper;

import com.notayessir.common.executor.RegistryExecutor;
import com.notayessir.registry.api.Discovery;
import com.notayessir.registry.api.bean.RegistryConst;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.registry.api.util.URLUtil;
import com.notayessir.registry.zookeeper.listener.ZKProviderChangedListener;
import com.notayessir.rpc.api.bean.ServiceUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZKDiscovery implements Discovery {

    private final CuratorFramework client;


    private final Logger LOG = LogManager.getLogger(this.getClass());

    public ZKDiscovery(String registryHost, int registryPort) {
        String connectString = registryHost + RegistryConst.HOST_SPLIT + registryPort;
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(connectString, retry);
        client.start();
    }

    @Override
    public List<ServiceUnit> discover(List<String> interfaceNames) {
        List<ServiceUnit> list = new ArrayList<>(interfaceNames.size());
        for (String interfaceName : interfaceNames) {
            String path = RegistryConst.PATH_ROOT + RegistryConst.PATH_SLASH + interfaceName + RegistryConst.PROVIDERS;
            List<String> providers;
            try {
                providers = client.getChildren().forPath(path);
            } catch (Exception e) {
                continue;
            }
            if (!providers.isEmpty()) {
                List<Service> services = new ArrayList<>(providers.size());
                for (String provider : providers) {
                    Service service = URLUtil.jsonToBean(provider);
                    if (Objects.isNull(service)) {
                        LOG.info("can't convert serviceInfo to url, skip this provider: {}", provider);
                        continue;
                    }
                    services.add(service);
                }
                list.add(new ServiceUnit(services));
            }
        }
        return list;
    }

    @Override
    public void watch(List<String> interfaceNames) {
        for (String interfaceName : interfaceNames) {
            String providerPath = RegistryConst.PATH_ROOT + RegistryConst.PATH_SLASH + interfaceName + RegistryConst.PROVIDERS;
            CuratorCache providerNodeCache = CuratorCache.build(client, providerPath);
            providerNodeCache.listenable()
                    .addListener(new ZKProviderChangedListener(providerPath), RegistryExecutor.getExecutor());
            providerNodeCache.start();
        }
    }

    @Override
    public void close() {
        client.close();
    }

}
