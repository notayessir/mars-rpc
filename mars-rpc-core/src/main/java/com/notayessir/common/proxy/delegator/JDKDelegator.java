package com.notayessir.common.proxy.delegator;


import com.notayessir.common.proxy.Delegator;
import com.notayessir.registry.api.bean.Reference;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 基于 JDK 实现的代理
 */
public class JDKDelegator extends Delegator implements InvocationHandler {

    public JDKDelegator(Reference reference) {
        super(reference);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args, reference);
    }


}
