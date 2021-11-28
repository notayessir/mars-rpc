package com.notayessir.cluster.loadbalance.impl;

import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.Invocation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 根据一致性哈希算法选择服务服务提供者
 */
@Component(value = "CONSISTENT_HASH")
@Lazy
public class ConsistentHashLoadBalance implements LoadBalance {

    /**
     * 保存接口名对应的虚拟散列节点
     */
    private final ConcurrentMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();


    @Override
    public Invoker balance(List<Invoker> invokerList, Invocation invocation) {
        if (invokerList.size() == 1){
            return invokerList.get(0);
        }
        String interfaceKey = invocation.getMethod().getDeclaringClass().getName();
        ConsistentHashSelector selector = selectors.get(interfaceKey);
        int hashCode = invokerList.hashCode();
        if (Objects.isNull(selector) || selector.identityHashCode != hashCode) {
            selectors.put(interfaceKey, new ConsistentHashSelector(invokerList));
            selector = selectors.get(interfaceKey);
        }
        return selector.select(invocation);
    }


    /**
     * 虚拟的哈希散列节点
     */
    private static final class ConsistentHashSelector {

        private final TreeMap<Long, Invoker> virtualInvokers;

        /**
         * 根据服务提供者生成的 hashcode，为了简化逻辑，当改值变化时，重新创建实例
         */
        private final int identityHashCode;

        /**
         * 参与 hash 的参数索引
         */
        private final int[] argumentIndex = new int[]{0};


        public ConsistentHashSelector(List<Invoker> invokerList) {
            virtualInvokers = new TreeMap<>();
            identityHashCode = invokerList.hashCode();
            for (Invoker invoker : invokerList) {
                String address = invoker.getInvokerMeta().getHost() + ":" + invoker.getInvokerMeta().getPort();
                int replicaNumber = 160;
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(address + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        public Invoker select(Invocation invocation) {
            String key = toKey(invocation.getArgs());
            byte[] digest = md5(key);
            return selectForKey(hash(digest, 0));
        }

        private String toKey(Object[] args) {
            StringBuilder buf = new StringBuilder();
            for (int i : argumentIndex) {
                if (i >= 0 && i < args.length) {
                    buf.append(args[i]);
                }
            }
            return buf.toString();
        }

        private Invoker selectForKey(long hash) {
            Map.Entry<Long, Invoker> entry = virtualInvokers.ceilingEntry(hash);
            if (Objects.isNull(entry)) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }

        /**
         * KETAMA_HASH
         */
        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        private byte[] md5(String value) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.reset();
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            md5.update(bytes);
            return md5.digest();
        }


    }



}
