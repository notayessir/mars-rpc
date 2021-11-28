package com.notayessir.cluster.loadbalance.impl;

import com.notayessir.cluster.counter.ProcessCounterManager;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 最少活跃数，通过客户端的统计，找出活跃数最小的服务提供者进行调用
 */
@Component(value = "LEAST_ACTIVE")
@Lazy
public class LeastActiveLoadBalance implements LoadBalance {


    @Override
    public Invoker balance(List<Invoker> invokerList, Invocation invocation) {
        int size = invokerList.size();
        if (size == 1) {
            return invokerList.get(0);
        }
        int minActiveValue = Integer.MAX_VALUE;
        int minActiveIndex = 0;
        int minActiveWeight = 0;
        int [] minActiveIndexes = new int[size];
        int [] weights = new int[size];
        int activeSameCount = 0;
        int weightSum = 0;
        for (int i = 0; i < size; i++) {
            Invoker invoker = invokerList.get(i);
            int invokerActive = ProcessCounterManager.get(invoker.getId()).getProcessingCount().get();
            if (invokerActive < minActiveValue){
                minActiveIndex = i;
                minActiveValue = invokerActive;
                minActiveWeight = invoker.getInvokerMeta().getWeight();
            }else if (invokerActive == minActiveValue){
                minActiveIndexes[activeSameCount] = i;
                Integer weight = invoker.getInvokerMeta().getWeight();
                weights[activeSameCount] = weight;
                weightSum += weight;
                activeSameCount++;
            }
        }
        if (activeSameCount == 0) {
            return invokerList.get(minActiveIndex);
        }

        // 补充：将最小的权重、索引加入到数组中
        minActiveIndexes[activeSameCount] = minActiveIndex;
        weights[activeSameCount] = minActiveWeight;
        activeSameCount++;
        weightSum += minActiveWeight;

        int [] invokerIndexes = new int[weightSum];
        int count = 0;
        for (int i = 0; i < activeSameCount; i++) {
            int w = weights[i];
            for (int j = 0; j < w; j++) {
                invokerIndexes[count++] = minActiveIndexes[i];
            }
        }
        int index = new Random().nextInt(invokerIndexes.length);
        return invokerList.get(invokerIndexes[index]);
    }


}
