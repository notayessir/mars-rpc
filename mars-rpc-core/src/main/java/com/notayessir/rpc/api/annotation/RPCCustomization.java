package com.notayessir.rpc.api.annotation;


import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;

import java.lang.annotation.*;


/**
 * 客户端使用的注解，用于指定在某个方法上使用特定的调用策略
 */
@Repeatable(RPCCustomizationArr.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RPCCustomization {

    /**
     * 方法名
     */
    String methodName();

    /**
     * 集群策略
     */
    Cluster.Strategy clusterStrategy() default Cluster.Strategy.NULL;

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
     * 并发调用个数
     */
    int forkingNumber() default -1;

}
