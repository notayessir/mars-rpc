package com.notayessir.cluster.fault.impl;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.RPCException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 逐个调用每个服务提供者，如果其中一台报错，在循环调用结束后，会抛出异常
 */
@Component("BROADCAST")
@Lazy
public class BroadcastCluster extends Cluster {



    @Override
    public Object invoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws RPCException {
        invokers = selectAvailable(invokers);
        Object result = null;
        RPCException rpcEx = null;
        for (Invoker invoker : invokers) {
            try {
                // 进行远程调用
                result = invoker.invoke(invocation);
            } catch (RPCException e) {
                rpcEx = e;
                LOG.warn(e.getMessage(), e);
            } catch (Throwable e) {
                rpcEx = new RPCException(RPCException.Message.UN_CLASSIFY_EXCEPTION.getCode(), e.toString());
                LOG.warn(e.getMessage(), e);
            }
        }
        if (!Objects.isNull(rpcEx)) {
            throw rpcEx;
        }
        return result;
    }
}
