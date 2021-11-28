package com.notayessir.cluster.fault.impl;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.RPCException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 当调用过程中出现异常时，仅会打印异常，而不会抛出异常
 */
@Component("FAILSAFE")
@Lazy
public class FailsafeCluster extends Cluster {

    @Override
    public Object invoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws RPCException {
        try {
            invokers = selectAvailable(invokers);
            Invoker invoker = loadBalance.balance(invokers, invocation);
            return invoker.invoke(invocation);
        }catch (Throwable e){
            LOG.error("failsafe ignore exception: " + e.getMessage(), e);
            return new EmptyRPCResult();
        }
    }


    public static class EmptyRPCResult {}
}
