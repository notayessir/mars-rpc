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
import java.util.concurrent.locks.ReentrantLock;

/**
 * 仿 Nginx 的加权轮询.
 * 比如有权重分别为 2，3，4 的三个 invoker a，b，c，
 * 进行 9 次调用，调用的顺序是 cba，cbc，abc，
 * 即 a 进行了 2 次调用，b 进行 3 次调用，c 进行 4 次调用
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

        // 通过哈希码判断 invoker 列表是否有变化
        if (identityHashCode != invokerList.hashCode()){
            // 借助 CAS 清除相关记录
            if (updateLock.compareAndSet(false, true)){
                identityHashCode = invokerList.hashCode();
                map.clear();
                updateLock.set(false);
            }
            // invoker 列表变动期间，均使用随机负载均衡调用
            return MarsRPCContext.getLoadBalance(Strategy.RANDOM.name())
                    .balance(invokerList, invocation);
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
                roundRobin = new WeightedRoundRobin(weight);
                map.putIfAbsent(key, roundRobin);
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
        return MarsRPCContext.getLoadBalance(Strategy.RANDOM.name())
                .balance(invokerList, invocation);
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
