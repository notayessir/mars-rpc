package com.notayessir.common.proxy.impl;

import com.notayessir.common.proxy.Proxy;
import com.notayessir.common.proxy.delegator.CGLibDelegator;
import com.notayessir.registry.api.bean.Reference;
import com.notayessir.registry.api.bean.ReferenceCache;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.util.List;


/**
 * CGLib 代理类
 */
public class CGLibProxy implements Proxy {

    @Override
    public void gen(ReferenceCache referInfoList) throws Exception {
        for (List<Reference> references : referInfoList.getReferenceMap().values()){
            for (Reference reference : references) {
                Field field = reference.getField();
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(field.getType());
                enhancer.setCallback(new CGLibDelegator(reference));
                Object proxyInstance = enhancer.create();
                field.setAccessible(true);
                field.set(reference.getBean(), proxyInstance);
            }
        }
    }
}
