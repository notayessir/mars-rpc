package com.notayessir.common.executor;

import com.notayessir.common.spring.definition.bean.base.ExecutorConfig;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;


/**
 * 任务线程池，用于执行客户端、服务端提交的请求、响应任务
 */
public class TaskExecutor {


    private static final Logger LOG = LogManager.getLogger(TaskExecutor.class);

    /**
     * 线程池
     */
    private final ExecutorService executor;


    public TaskExecutor(ExecutorConfig executorConfig) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(executorConfig.getQueueSize());
        executor = new ThreadPoolExecutor(executorConfig.getCoreSize(), executorConfig.getMaximumSize(), 30L,
                TimeUnit.SECONDS, queue, new RejectedHandler());
    }


    public static class RejectedHandler implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            LOG.error("queue of TaskExecutor is full, size:{}, reject task.",executor.getQueue().size());
        }
    }


    /**
     * 提交一个任务
     * @param task  Runnable 类型的任务，无返回值
     */
    public void submit(Runnable task){
        executor.submit(task);
    }

    /**
     * 提交一个任务
     * @param task  Callable 类型的任务
     * @return      返回 future
     */
    public Future<ResponseFrame> submit(Callable<ResponseFrame> task){
        return executor.submit(task);
    }



}
