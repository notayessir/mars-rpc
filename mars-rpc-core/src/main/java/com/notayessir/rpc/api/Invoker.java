package com.notayessir.rpc.api;

import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.InvokerMeta;
import com.notayessir.rpc.api.bean.RPCException;

/**
 * 服务消费者的 tcp 引用
 */
public interface Invoker extends Closable{

    /**
     * 远程调用
     * @param invocation        调用信息
     * @return                  调用结果
     * @throws RPCException     调用异常时抛出
     */
    Object invoke(Invocation invocation) throws RPCException;

    /**
     * 服务信息
     * @return  invoker 的元信息
     */
    InvokerMeta getInvokerMeta();


    /**
     * 是否可用
     * @return  是否可用
     */
    boolean isEnable();

    /**
     * 设置该 invoker 是否可用
     * @param enable    true/false
     */
    void setEnable(boolean enable);

    /**
     * 获取唯一标识
     * @return      唯一标识
     */
    int getId();
}
