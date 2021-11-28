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
 * 只会进行一次调用，失败后立即抛出异常
 */
@Component("FAIL_FAST")
@Lazy
public class FailFastCluster extends Cluster {


    @Override
    public Object invoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws RPCException {
        invokers = selectAvailable(invokers);
        Invoker invoker = loadBalance.balance(invokers, invocation);
        return invoker.invoke(invocation);
    }







}
