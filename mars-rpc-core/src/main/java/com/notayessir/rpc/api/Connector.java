package com.notayessir.rpc.api;

import com.notayessir.rpc.api.bean.ConnectionMeta;

import java.util.List;

/**
 * 远程连接器，用于创建持久连接
 */
public interface Connector {

    /**
     * 创建单个连接
     * @param connectionMeta    连接信息
     * @return                  invoker
     */
    Invoker connect(ConnectionMeta connectionMeta);

    /**
     * 创建多个连接
     * @param connectionMetas   连接信息
     * @return                  invokers
     */
    List<Invoker> connect(List<ConnectionMeta> connectionMetas);
}
