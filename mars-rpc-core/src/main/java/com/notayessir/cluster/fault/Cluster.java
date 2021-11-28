package com.notayessir.cluster.fault;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.RPCException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 集群容错抽象类
 */
public abstract class Cluster {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    /**
     * 根据策略对 invoker 进行集群调用
     * @param invokers      服务提供者
     * @param invocation    调用信息
     * @param loadBalance   负载均衡
     * @return              调用结果
     * @throws RPCException 当服务提供者不存在或调用过程出错时抛出
     */
    public abstract Object invoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws RPCException;

    /**
     * 选择可用的服务提供者
     * @param invokerList   服务提供者列表
     * @return              可用的服务提供者
     * @throws RPCException 当服务提供者数量为空时抛出
     */
    protected List<Invoker> selectAvailable(List<Invoker> invokerList) throws RPCException {
        if (invokerList.isEmpty()) {
            throw new RPCException(RPCException.Message.CLIENT_EMPTY_INVOKERS_BEFORE_SELECT);
        }
        LOG.debug("size:{}, invokerList:{}", invokerList.size(), JSONObject.toJSONString(invokerList));
        List<Invoker> invokers = new ArrayList<>(invokerList.size());
        for (Invoker invoker : invokerList) {
            if (invoker.isEnable()){
                invokers.add(invoker);
            }
        }
        if (invokers.isEmpty()){
            throw new RPCException(RPCException.Message.CLIENT_EMPTY_INVOKERS_AFTER_SELECT);
        }
        return invokers;
    }

    /**
     * 集群调用策略
     */
    public enum Strategy {

        /**
         * 在调用失败时，会自动切换 Invoker 进行重试
         */
        FAILOVER,

        /**
         * 当调用过程中出现异常时，仅会打印异常，而不会抛出异常
         */
        FAILSAFE,

        /**
         * 在运行时通过线程池创建多个线程，并发调用多个服务提供者，只要有一个服务提供者成功返回了结果，其他的并发调用就取消
         */
        FORKING,

        /**
         * 只会进行一次调用，失败后立即抛出异常
         */
        FAIL_FAST,


        /**
         * 逐个调用每个服务提供者，如果其中一台报错，在循环调用结束后，会抛出异常
         */
        BROADCAST,

        NULL;

        public static Strategy getByValue(String value){
            Strategy[] values = Strategy.values();
            for (Strategy strategy: values){
                if (StringUtils.equalsIgnoreCase(value, strategy.name())){
                    return strategy;
                }
            }
            return FAILOVER;
        }

    }
}
