package com.notayessir.registry.zookeeper.listener;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.provider.InvokerManager;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.registry.api.util.URLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZKProviderChangedListener implements CuratorCacheListener {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    private final String providerPath;

    public ZKProviderChangedListener(String providerPath) {
        this.providerPath = providerPath;
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        LOG.info("event from zk, type :{}, oldData:{}, data:{}", type, oldData, data);
//        System.out.println("type:"+JSON.toJSONString(type));
//        System.out.println("oldData:"+JSON.toJSONString(oldData));
//        System.out.println("data:"+JSON.toJSONString(data));
//        System.out.println("providerPath:"+providerPath);
        InvokerManager invokerManager = MarsRPCContext.getInvokerService();
        Service service;
        String provider;
        String path;
        switch (type) {
            case NODE_CREATED:
                path = data.getPath();
                if (StringUtils.equals(providerPath, path)){
                    break;
                }
                provider = path.substring(providerPath.length() + 1);
                service = URLUtil.jsonToBean(provider);
                invokerManager.addProvider(service);
                break;
            case NODE_DELETED:
                path = oldData.getPath();
                if (StringUtils.equals(providerPath, path)){
                    break;
                }
                provider = path.substring(providerPath.length() + 1);
                service = URLUtil.jsonToBean(provider);
                invokerManager.removeInvoker(service);
                break;
        }
    }


}
