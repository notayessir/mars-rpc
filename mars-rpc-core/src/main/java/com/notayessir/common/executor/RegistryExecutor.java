package com.notayessir.common.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 单线程任务线程池，用于串行处理注册中心的监听事件
 */
public class RegistryExecutor {

    private final static ExecutorService executor = new ThreadPoolExecutor(1, 1, 30L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), new ThreadPoolExecutor.AbortPolicy());


    public static ExecutorService getExecutor() {
        return executor;
    }
}
