package com.notayessir.cluster.fault.impl;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.RPCException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在调用失败时，会自动切换 Invoker 进行重试
 */
@Component("FAILOVER")
public class FailoverCluster extends Cluster {


    @Override
    public Object invoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws RPCException {
        int retry = invocation.getClusterMeta().getRetry();
        for (int i = 0; i < retry; i++) {
            invokers = selectAvailable(invokers);
            Invoker invoker = loadBalance.balance(invokers, invocation);
            try {
                return invoker.invoke(invocation);
            } catch (RPCException e) {
                if (e.isBusiness()) {
                    throw e;
                }
            }
        }
        throw new RPCException(RPCException.Message.CLIENT_FAILOVER_CLUSTER_FAIL);
    }
}
