package com.notayessir.common.proxy.impl;

import com.notayessir.common.proxy.Proxy;
import com.notayessir.common.proxy.delegator.JDKDelegator;
import com.notayessir.registry.api.bean.Reference;
import com.notayessir.registry.api.bean.ReferenceCache;

import java.lang.reflect.Field;
import java.util.List;

/**
 * JDK 代理类
 */
public class JDKProxy implements Proxy {


    @Override
    public void gen(ReferenceCache referenceCache) throws Exception {
        for (List<Reference> references : referenceCache.getReferenceMap().values()){
            for (Reference reference : references) {
                // 获取 RPCReference 标注的字段
                Field field = reference.getField();
                field.setAccessible(true);
                Object proxyInstance = java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{field.getType()}, new JDKDelegator(reference));
                // 设置代理类，这个方法的意思是，给这个 bean 的 field 赋值
                field.set(reference.getBean(), proxyInstance);
            }
        }
    }
}
