package com.notayessir.common.spring;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.cluster.router.Router;
import com.notayessir.cluster.router.impl.StandardRouter;
import com.notayessir.common.executor.TaskExecutor;
import com.notayessir.common.spring.definition.bean.CommonConfig;
import com.notayessir.common.spring.definition.bean.ConsumerConfig;
import com.notayessir.common.spring.definition.bean.ProviderConfig;
import com.notayessir.common.spring.event.provider.InvokerManager;
import com.notayessir.common.uniqueId.IdGenerator;
import com.notayessir.common.uniqueId.impl.ReqIdGenerator;
import com.notayessir.registry.api.Discovery;
import com.notayessir.registry.api.Registry;
import com.notayessir.registry.api.bean.ReferenceCache;
import com.notayessir.registry.api.bean.ServiceCache;
import com.notayessir.rpc.api.Server;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * RPC 框架上下文，包括全局信息
 */
public class MarsRPCContext {

    /**
     * Spring 容器上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 服务消费者、提供者任务线程池
     */
    private static TaskExecutor taskExecutor;

    /**
     * 服务提供者管理器
     */
    private static InvokerManager invokerManager;

    /**
     * 路由
     */
    private static final Router router;

    /**
     * 服务提供者信息缓存
     */
    private static final ServiceCache serviceCache;

    /**
     * 服务消费者信息缓存
     */
    private static final ReferenceCache referenceCache;

    /**
     * 请求 id 生成器
     */
    private static final IdGenerator idGenerator;

    /**
     * 服务提供者配置
     */
    private static final ProviderConfig defaultProviderConfig;

    /**
     * 服务消费者配置
     */
    private static final ConsumerConfig defaultConsumerConfig;

    /**
     * 一些通用配置
     */
    private static final CommonConfig defaultCommonConfig;

    /**
     * 注册中心 - 发现服务
     */
    private static Discovery discovery;

    /**
     * 注册中心 - 注册服务
     */
    private static Registry registry;

    /**
     * Netty server
     */
    private static Server server;

    static {
        serviceCache = new ServiceCache();
        referenceCache = new ReferenceCache();
        idGenerator = new ReqIdGenerator();
        router = new StandardRouter();

        defaultProviderConfig = new ProviderConfig();
        defaultConsumerConfig = new ConsumerConfig();
        defaultCommonConfig = new CommonConfig();
    }

    public static Discovery getDiscovery() {
        return discovery;
    }

    public static void setDiscovery(Discovery discovery) {
        MarsRPCContext.discovery = discovery;
    }

    public static Registry getRegistry() {
        return registry;
    }

    public static void setRegistry(Registry registry) {
        MarsRPCContext.registry = registry;
    }

    /**
     * 根据负载均衡名称获取负载组件实例
     * @param name  负载均衡名称
     * @return      负载均衡实例
     */
    public static LoadBalance getLoadBalance(String name) {
        return applicationContext.getBean(name, LoadBalance.class);
    }

    /**
     * 根据集群容错名称获取集群容错实例
     * @param name  集群容错名称
     * @return      集群容错实例
     */
    public static Cluster getClusterInvoker(String name) {
        return applicationContext.getBean(name, Cluster.class);
    }

    public static TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public static void setTaskExecutor(TaskExecutor taskExecutor) {
        MarsRPCContext.taskExecutor = taskExecutor;
    }

    public static CommonConfig getCommonConfig(){
        try {
            return applicationContext.getBean(CommonConfig.class);
        }catch (BeansException e){
            return defaultCommonConfig;
        }
    }

    public static ProviderConfig getProviderConfig(){
        try {
            return applicationContext.getBean(ProviderConfig.class);
        }catch (BeansException e){
            return defaultProviderConfig;
        }
    }

    public static ConsumerConfig getConsumerConfig(){
        try {
            return applicationContext.getBean(ConsumerConfig.class);
        }catch (BeansException e){
            return defaultConsumerConfig;
        }
    }

    public static Long genRequestId() {
        return idGenerator.gen();
    }

    public static Router getRouter() {
        return router;
    }

    public static ServiceCache getServiceCache() {
        return serviceCache;
    }

    public static ReferenceCache getReferenceCache() {
        return referenceCache;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        MarsRPCContext.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    public static Object getBean(String serviceName) {
        return applicationContext.getBean(serviceName);
    }

    public static InvokerManager getInvokerService() {
        return invokerManager;
    }

    public static void setInvokerService(InvokerManager invokerManager) {
        MarsRPCContext.invokerManager = invokerManager;
    }


    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        MarsRPCContext.server = server;
    }
}
