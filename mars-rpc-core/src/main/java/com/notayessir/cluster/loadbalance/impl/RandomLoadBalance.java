package com.notayessir.cluster.loadbalance.impl;

import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 根据服务提供者的权重大小，随机选择一个服务提供者，默认策略
 */
@Component(value = "RANDOM")
public class RandomLoadBalance implements LoadBalance {


    @Override
    public Invoker balance(List<Invoker> invokerList, Invocation invocation) {
        if (invokerList.size() == 1){
            return invokerList.get(0);
        }
        List<Integer> weights = new ArrayList<>();
        for (int i = 0; i < invokerList.size(); i++){
            Integer weight = invokerList.get(i).getInvokerMeta().getWeight();
            for (int j = 0; j < weight; j++){
                weights.add(i);
            }
        }
        int i = new Random().nextInt(weights.size());
        return invokerList.get(i);
    }
}
