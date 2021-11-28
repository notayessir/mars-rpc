package com.notayessir.cluster.fault;

import com.notayessir.cluster.loadbalance.LoadBalance;

/**
 * 针对方法定制的集群配置信息
 */
public class ClusterMeta {


    private String serviceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 集群策略
     */
    private Cluster.Strategy strategy;

    /**
     * 负载均衡策略
     */
    private LoadBalance.Strategy balanceStrategy;


    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 重试次数
     */
    private int retry;

    /**
     * 并发发起调用的请求个数
     */
    private int forkingNumber;

    public ClusterMeta(){}

    public ClusterMeta(String methodName, Cluster.Strategy strategy, LoadBalance.Strategy balanceStrategy, long timeout, int retry, int forkingNumber, String serviceName) {
        this.methodName = methodName;
        this.strategy = strategy;
        this.balanceStrategy = balanceStrategy;
        this.timeout = timeout;
        this.retry = retry;
        this.forkingNumber = forkingNumber;
        this.serviceName = serviceName;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getForkingNumber() {
        return forkingNumber;
    }

    public void setForkingNumber(int forkingNumber) {
        this.forkingNumber = forkingNumber;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Cluster.Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Cluster.Strategy strategy) {
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
