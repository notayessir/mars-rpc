package com.notayessir.common.spring.definition.bean.base;

/**
 * 服务消费者、提供者的线程池配置
 */
public class ExecutorConfig {


    /**
     * 线程初始个数
     */
    private int coreSize;

    /**
     * 线程最大个数
     */
    private int maximumSize;

    /**
     * 有界队列长度
     */
    private int queueSize;

    public ExecutorConfig() {
        this.coreSize = 4;
        this.maximumSize = 8;
        this.queueSize = 1024;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
