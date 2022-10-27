package com.notayessir.registry.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.registry.api.Registry;
import com.notayessir.registry.api.bean.RegistryConst;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.registry.api.bean.ServiceCache;
import com.notayessir.registry.api.util.URLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Objects;

public class ZKRegistry implements Registry {

    private final CuratorFramework client;

    private final Logger LOG = LogManager.getLogger(this.getClass());


    public ZKRegistry(String registryHost, int registryPort) {
        String connectString = registryHost + RegistryConst.HOST_SPLIT + registryPort;
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(connectString, retry);
        client.start();
    }

    @Override
    public void register(ServiceCache serviceCache){
        for (Service service : serviceCache.getServices()) {
            //  TODO 这里依赖框架，来组织服务提供者的信息并不可靠，需要换一种数据格式
            String serviceInfoString = URLUtil.beanToJSON(service);
            if (StringUtils.isBlank(serviceInfoString)){
                continue;
            }
            String path = String.format(RegistryConst.SERVICE_INFO_PATH, service.getInterfaceName(), serviceInfoString);
            try {
                // 创建节点，若已存在，先删除再重新创建
                Stat stat = client.checkExists().forPath(path);
                if (!Objects.isNull(stat)){
                    client.delete().forPath(path);
                }
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL) // 创建临时的节点
                        .forPath(path);
            } catch (Exception e) {
                LOG.error("unexpected exception happened when register service, ignored and continue, message:" , e);
            }
        }


    }

    @Override
    public void unregister(List<Service> services){
        for (Service service : services) {
            String serviceInfoString = JSONObject.toJSONString(service);
            String path = String.format(RegistryConst.SERVICE_INFO_PATH, service.getInterfaceName(), serviceInfoString);
            try {
                Stat stat = client.checkExists().forPath(path);
                if (!Objects.isNull(stat)){
                    client.delete().forPath(path);
                }
            } catch (Exception e) {
                LOG.error("unexpected exception happened when register service, ignored and continue, message:" , e);
            }
        }
    }

    @Override
    public void close() {
        client.close();
    }
}
