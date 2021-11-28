package com.notayessir.cluster.loadbalance.impl;

import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.InvokerMeta;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 仿 Nginx 的加权轮询
 */
@Component(value = "ROUND_ROBIN")
@Lazy
public class RoundRobinLoadBalance implements LoadBalance {


    private final Map<String, WeightedRoundRobin> map = new ConcurrentHashMap<>();

    private int identityHashCode = Integer.MAX_VALUE;

    private final AtomicBoolean updateLock = new AtomicBoolean();


    @Override
    public Invoker balance(List<Invoker> invokerList, Invocation invocation) {
        if (invokerList.size() == 1){
            return invokerList.get(0);
        }

        if (identityHashCode != invokerList.hashCode()){
            if (updateLock.compareAndSet(false, true)){
                map.clear();
                identityHashCode = invokerList.hashCode();
                updateLock.set(false);
            }
            return MarsRPCContext.getLoadBalance(Strategy.RANDOM.name()).balance(invokerList, invocation);
        }

        int totalWeight = 0;
        long maxCurrentWeight = Long.MIN_VALUE;
        Invoker selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;
        for (Invoker invoker: invokerList) {
            InvokerMeta invokerMeta = invoker.getInvokerMeta();
            int weight = invokerMeta.getWeight();
            String key = invokerMeta.getHost()  + ":" + invokerMeta.getPort();
            WeightedRoundRobin roundRobin = map.get(key);
            if (Objects.isNull(roundRobin)){
                map.putIfAbsent(key, new WeightedRoundRobin(weight));
                roundRobin = map.get(key);
            }
            int currentWeight = roundRobin.increaseCurrentWeight();
            if (currentWeight > maxCurrentWeight){
                maxCurrentWeight = currentWeight;
                selectedInvoker = invoker;
                selectedWRR = roundRobin;
            }
            totalWeight += weight;
        }
        if (!Objects.isNull(selectedInvoker)) {
            selectedWRR.setCurrentWeight(totalWeight);
            return selectedInvoker;
        }
        return MarsRPCContext.getLoadBalance(Strategy.RANDOM.name()).balance(invokerList, invocation);
    }

    static class WeightedRoundRobin {

        private final int weight;

        private final AtomicInteger currentWeight = new AtomicInteger(0);

        public WeightedRoundRobin(int weight) {
            this.weight = weight;
            currentWeight.set(0);
        }

        public int increaseCurrentWeight() {
            return currentWeight.addAndGet(weight);
        }
        public void setCurrentWeight(int total) {
            currentWeight.addAndGet(-1 * total);
        }
    }
}
