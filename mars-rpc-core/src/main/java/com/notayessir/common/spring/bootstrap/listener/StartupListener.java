package com.notayessir.common.spring.bootstrap.listener;

import com.notayessir.common.executor.TaskExecutor;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.bootstrap.Startup;
import com.notayessir.common.spring.bootstrap.impl.ConsumerStartup;
import com.notayessir.common.spring.bootstrap.impl.ProviderStartup;
import com.notayessir.common.spring.definition.bean.CommonConfig;
import com.notayessir.common.spring.definition.bean.ConsumerConfig;
import com.notayessir.common.spring.definition.bean.ProviderConfig;
import com.notayessir.common.spring.definition.bean.base.ExecutorConfig;
import com.notayessir.registry.api.bean.ReferenceCache;
import com.notayessir.registry.api.bean.ServiceCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * RPC 启动入口，依赖 Spring 监听器，对服务消费者、提供者进行初始化
 */
@Order(value = 20)  // 值越小优先级越高，优先执行
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 初始化服务提供者
        ProviderConfig providerConfig = MarsRPCContext.getProviderConfig();
        if (providerConfig.isConfig()){
            // 将 RPCDetectListener 扫描出来的服务提供者，启动 netty 并注册到注册中心
            ServiceCache serviceCache = MarsRPCContext.getServiceCache();
            Startup exposeStartup = new ProviderStartup(serviceCache, providerConfig);
            try {
                exposeStartup.start();
            } catch (Exception e) {
                LOG.error("ProviderStartup error, e:", e);
                e.printStackTrace();
                return;
            }
        }

        // 初始化服务消费者
        ConsumerConfig consumerConfig = MarsRPCContext.getConsumerConfig();
        if (consumerConfig.isConfig()){
            // 为 RPCDetectListener 扫描出来的服务消费者，拉取注册中心中的提供者信息并使用动态代理技术生成代理
            ReferenceCache referenceCache = MarsRPCContext.getReferenceCache();
            Startup referStartup = new ConsumerStartup(referenceCache, consumerConfig);
            try {
                referStartup.start();
            } catch (Exception e) {
                LOG.error("ConsumerStartup error, e:", e);
                e.printStackTrace();
                return;
            }
        }

        // 初始化共有的任务线程池，这里可能会成为瓶颈点
        if (consumerConfig.isConfig() || providerConfig.isConfig()){
            CommonConfig commonConfig = MarsRPCContext.getCommonConfig();
            // 默认配置的线程池
            if (!commonConfig.isConfig()){
                MarsRPCContext.setTaskExecutor(new TaskExecutor(new ExecutorConfig()));
                return;
            }
            MarsRPCContext.setTaskExecutor(new TaskExecutor(commonConfig.getExecutorConfig()));
        }
    }
}
