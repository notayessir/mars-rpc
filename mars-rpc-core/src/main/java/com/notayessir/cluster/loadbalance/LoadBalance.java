package com.notayessir.cluster.loadbalance;


import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {

    /**
     * 通过负载均衡逻辑从多个服务提供者中选择一个
     * @param invokerList   服务提供者列表
     * @param invocation    调用信息
     * @return              被选中的服务提供者
     */
    Invoker balance(List<Invoker> invokerList, Invocation invocation);

    /**
     * 负载均衡策略
     */
    enum Strategy {

        /**
         * 带权重的轮询
         */
        ROUND_ROBIN,

        /**
         * 随机
         */
        RANDOM,

        /**
         * 一致性哈希
         */
        CONSISTENT_HASH,

        /**
         * 最少活跃数
         */
        LEAST_ACTIVE,

        NULL;


        public static Strategy getByValue(String value){
            Strategy[] values = Strategy.values();
            for (Strategy strategy: values){
                if (StringUtils.equalsIgnoreCase(value, strategy.name())){
                    return strategy;
                }
            }
            return RANDOM;
        }

    }
}
