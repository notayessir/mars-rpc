package com.notayessir.common.spring.definition.bean.base;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;

/**
 * 服务消费者的集群配置
 */
public class ClusterConfig {

    /**
     *  集群容错策略
     */
    private Cluster.Strategy strategy;

    /**
     *  负载均衡策略
     */
    private LoadBalance.Strategy balanceStrategy;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 重试次数，集群容错 FAILOVER 策略使用
     */
    private int retry;

    /**
     * 并发数，集群容错中 ForkingCluster 策略使用
     */
    private int forkingNumber;

    public ClusterConfig() {
        this.strategy = Cluster.Strategy.FAILOVER;
        this.balanceStrategy = LoadBalance.Strategy.RANDOM;
        this.timeout = 3000L;
        this.retry = 2;
        this.forkingNumber = 3;
    }

    public int getForkingNumber() {
        return forkingNumber;
    }

    public void setForkingNumber(int forkingNumber) {
        this.forkingNumber = forkingNumber;
    }

    public Cluster.Strategy getClusterStrategy() {
        return strategy;
    }

    public void setClusterStrategy(Cluster.Strategy strategy) {
        this.strategy = strategy;
    }

    public LoadBalance.Strategy getBalanceStrategy() {
        return balanceStrategy;
    }

    public void setBalanceStrategy(LoadBalance.Strategy balanceStrategy) {
        this.balanceStrategy = balanceStrategy;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
