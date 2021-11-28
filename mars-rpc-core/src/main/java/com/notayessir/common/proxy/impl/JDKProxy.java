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
                Field field = reference.getField();
                field.setAccessible(true);
                Object proxyInstance = java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{field.getType()}, new JDKDelegator(reference));
                field.set(reference.getBean(), proxyInstance);
            }
        }
    }
}
