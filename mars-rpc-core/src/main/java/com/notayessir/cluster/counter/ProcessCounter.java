package com.notayessir.cluster.counter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 记录服务提供者当前正在处理的请求数
 */
public class ProcessCounter {

    private final AtomicInteger processingCount;

    public ProcessCounter() {
        this.processingCount = new AtomicInteger(0);
    }

    public AtomicInteger getProcessingCount() {
        return processingCount;
    }

    public void incrProcessingCount(){
        processingCount.incrementAndGet();
    }

    public void decrProcessingCount(){
        processingCount.decrementAndGet();
    }


}
