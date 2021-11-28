package com.notayessir.common.spring.bootstrap;

/**
 * 该 RPC 框架的启动入口，开始服务消费者、提供者逻辑
 */
public interface Startup {

    /**
     * 开始对应的服务订阅、服务暴露等逻辑
     * @throws Exception    服务订阅、暴露遇到异常时抛出
     */
    void start() throws Exception;
}
