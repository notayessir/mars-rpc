package com.notayessir.common.spring.event.context;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.event.provider.InvokerManager;
import com.notayessir.registry.api.Discovery;
import com.notayessir.registry.api.Registry;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.Server;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Spring 容器停止事件处理，释放相关资源
 */
@Component
public class ContextStoppedNotifier {


    @EventListener
    public void process(ContextStoppedEvent stoppedEvent) {
        // 服务提供者注销服务/关闭 netty 服务器
        Registry registry = MarsRPCContext.getRegistry();
        if (!Objects.isNull(registry)){
            List<Service> serviceList = MarsRPCContext.getServiceCache().getServices();
            registry.unregister(serviceList);
            registry.close();
        }

        // 服务消费者关闭注册中心监听/断开连接
        Discovery discovery = MarsRPCContext.getDiscovery();
        if (!Objects.isNull(discovery)){
            discovery.close();
        }

        // 关闭 invoker
        InvokerManager invokerService = MarsRPCContext.getInvokerService();
        if (!Objects.isNull(invokerService)){
            List<Invoker> invokers = invokerService.getInvokers();
            for (Invoker invoker : invokers) {
                invoker.close();
            }
        }

        // 关闭 server
        Server server = MarsRPCContext.getServer();
        if (!Objects.isNull(server)){
            server.close();
        }

    }

}
