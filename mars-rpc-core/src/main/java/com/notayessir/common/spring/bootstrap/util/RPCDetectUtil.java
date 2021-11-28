package com.notayessir.common.spring.bootstrap.util;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.fault.ClusterMeta;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.common.spring.definition.bean.base.ClusterConfig;
import com.notayessir.common.spring.definition.bean.base.ProtocolConfig;
import com.notayessir.registry.api.bean.Reference;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.rpc.api.annotation.RPCCustomization;
import com.notayessir.rpc.api.annotation.RPCReference;
import com.notayessir.rpc.api.annotation.RPCService;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * RPC 注解检测工具类
 */
public class RPCDetectUtil {


    /**
     * 根据注解、集群配置创建集群信息
     * @param rpcReference      服务消费者携带的注解信息
     * @param clusterConfig     公共的集群配置
     * @return                  集群信息
     */
    private static ClusterMeta buildClusterMeta(RPCReference rpcReference, ClusterConfig clusterConfig){
        ClusterMeta clusterMeta = new ClusterMeta();
        clusterMeta.setBalanceStrategy(rpcReference.balanceStrategy());
        if (rpcReference.balanceStrategy() == LoadBalance.Strategy.NULL){
            clusterMeta.setBalanceStrategy(clusterConfig.getBalanceStrategy());
        }
        clusterMeta.setStrategy(rpcReference.clusterStrategy());
        if (rpcReference.clusterStrategy() == Cluster.Strategy.NULL){
            clusterMeta.setStrategy(clusterConfig.getClusterStrategy());
        }
        clusterMeta.setTimeout(rpcReference.timeout());
        if (rpcReference.timeout() == -1L){
            clusterMeta.setTimeout(clusterConfig.getTimeout());
        }
        clusterMeta.setRetry(rpcReference.retry());
        if (rpcReference.retry() == -1){
            clusterMeta.setRetry(clusterConfig.getRetry());
        }
        clusterMeta.setForkingNumber(rpcReference.forkingNumber());
        if (rpcReference.forkingNumber() == -1){
            clusterMeta.setForkingNumber(clusterConfig.getForkingNumber());
        }
        clusterMeta.setServiceName(rpcReference.serviceName());
        return clusterMeta;
    }

    /**
     * 服务消费者检测
     * @param bean              Spring 容器管理的 bean
     * @param clusterConfig     公共的集群信息
     * @return                  该 bean 中所需的服务消费者信息
     */
    public static ArrayList<Reference> referenceDetect(Object bean, ClusterConfig clusterConfig) {
        ArrayList<Reference> references = new ArrayList<>();
        Class<?> beanClass = bean.getClass();
        // 扫描每个声明的字段
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 收集携带 RPCReference 注解的字段
            RPCReference rpcReference = declaredField.getAnnotation(RPCReference.class);
            if (!Objects.isNull(rpcReference)) {
                ClusterMeta clusterMeta = buildClusterMeta(rpcReference, clusterConfig);
                // 查看字段上是否对某个方法进行定制化集群调用
                RPCCustomization[] rpcCustomizations = declaredField.getAnnotationsByType(RPCCustomization.class);
                Map<String, ClusterMeta> clusterMetaMap = new HashMap<>();
                if (rpcCustomizations.length != 0){
                    for (RPCCustomization rpcCustomization : rpcCustomizations){
                        if (StringUtils.isEmpty(rpcCustomization.methodName())){
                            continue;
                        }
                        String methodName = rpcCustomization.methodName();
                        Cluster.Strategy strategy = Cluster.Strategy.NULL == rpcCustomization.clusterStrategy() ? clusterMeta.getStrategy() : rpcCustomization.clusterStrategy();
                        LoadBalance.Strategy balanceStrategy = LoadBalance.Strategy.NULL == rpcCustomization.balanceStrategy() ? clusterMeta.getBalanceStrategy() : rpcCustomization.balanceStrategy();
                        int retry = -1 == rpcCustomization.retry() ? clusterMeta.getRetry() : rpcCustomization.retry();
                        int forkingNumber = -1 == rpcCustomization.forkingNumber() ? clusterMeta.getForkingNumber() : rpcCustomization.forkingNumber();
                        long timeout = -1L == rpcCustomization.timeout() ? clusterMeta.getTimeout() : rpcCustomization.timeout();
                        ClusterMeta customizedClusterMeta = new ClusterMeta(methodName, strategy, balanceStrategy, timeout, retry, forkingNumber, clusterMeta.getServiceName());
                        clusterMetaMap.put(methodName, customizedClusterMeta);
                    }
                }
                references.add(new Reference(bean, declaredField, clusterMeta, clusterMetaMap));
            }
        }
        return references;
    }

    /**
     * 服务提供者检测
     * @param bean              Spring 容器管理的 bean
     * @param protocolConfig    协议配置
     * @return                  服务提供者信息
     */
    public static Service serviceDetect(Object bean, ProtocolConfig protocolConfig) {
        Class<?> beanClass = bean.getClass();
        RPCService annotation = beanClass.getAnnotation(RPCService.class);
        if (!Objects.isNull(annotation)) {
            Class<?> rpcInterface = annotation.rpcInterface();
            Method[] declaredMethods = rpcInterface.getDeclaredMethods();
            Set<String> methods = new HashSet<>();
            for (Method declaredMethod : declaredMethods) {
                int modifiers = declaredMethod.getModifiers();
                if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                    methods.add(declaredMethod.getName());
                }
            }
            if (!methods.isEmpty()) {
                HashSet<String> serviceNames = new HashSet<>();
                serviceNames.add(bean.getClass().getName());
                Service service = new Service();
                service.setMethods(methods);
                service.setInterfaceName(rpcInterface.getName());
                service.setServiceNames(serviceNames);
                service.setHost(protocolConfig.getHost());
                service.setPort(protocolConfig.getPort());
                service.setProtocol(protocolConfig.getProtocol());
                service.setWeight(protocolConfig.getWeight());
                return service;
            }
        }
        return null;
    }


}
