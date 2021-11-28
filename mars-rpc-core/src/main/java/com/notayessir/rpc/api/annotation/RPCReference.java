package com.notayessir.rpc.api.annotation;


import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;

import java.lang.annotation.*;


/**
 * 客户端使用的注解，用于引用远程服务
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RPCReference {

    /**
     * 远程服务器中，spring 容器管理的服务名称，当有多个实现时，按需要指定服务名，否则会使用接口去获取默认的服务
     */
    String serviceName() default "";

    /**
     * 集群策略
     */
    Cluster.Strategy clusterStrategy() default Cluster.Strategy.FAIL_FAST;


    /**
     * 负载均衡策略
     */
    LoadBalance.Strategy balanceStrategy() default LoadBalance.Strategy.NULL;

    /**
     * 超时时间
     */
    long timeout() default -1L;

    /**
     * 重试次数
     */
    int retry() default -1;


    /**
     * 并发发起调用的请求个数
     */
    int forkingNumber() default -1;


}
