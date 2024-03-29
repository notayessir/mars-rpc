package com.notayessir.common.spring.bootstrap.listener;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.common.spring.bootstrap.util.RPCDetectUtil;
import com.notayessir.common.spring.definition.bean.ConsumerConfig;
import com.notayessir.common.spring.definition.bean.ProviderConfig;
import com.notayessir.registry.api.bean.Reference;
import com.notayessir.registry.api.bean.ReferenceCache;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.registry.api.bean.ServiceCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;


/**
 * RPC 特定注解检测，依赖 Spring 监听器，对每个注册在 Spring 容器中的 bean 进行检测
 */
@Order(value = 10)  // 值越小优先级越高，优先执行
@Component
public class RPCDetectListener implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        MarsRPCContext.setApplicationContext(applicationContext);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        // 服务提供者检测
        ProviderConfig providerConfig = MarsRPCContext.getProviderConfig();
        if (providerConfig.isConfig()){
            for (String beanName: beanNames){
                Object bean = applicationContext.getBean(beanName);
                // 扫描 Spring 管理的 bean，将需要暴露的服务筛选出来
                Service service = RPCDetectUtil.serviceDetect(bean, providerConfig.getProtocolConfig());
                if (!Objects.isNull(service)){
                    ServiceCache serviceCache = MarsRPCContext.getServiceCache();
                    serviceCache.addService(service);
                }
            }
        }

        // 服务消费者检测
        ConsumerConfig consumerConfig = MarsRPCContext.getConsumerConfig();
        if (consumerConfig.isConfig()){
            for (String beanName: beanNames){
                // 扫描 Spring 管理的 bean，将需要引用的服务筛选出来
                Object bean = applicationContext.getBean(beanName);
                // bean 里的每个 RPC 字段（即服务）用一个 Reference 表示
                ArrayList<Reference> references = RPCDetectUtil.referenceDetect(bean, consumerConfig.getClusterConfig());
                if (!references.isEmpty()){
                    ReferenceCache referenceCache = MarsRPCContext.getReferenceCache();
                    referenceCache.putReferences(references);
                }
            }
        }
    }
}
