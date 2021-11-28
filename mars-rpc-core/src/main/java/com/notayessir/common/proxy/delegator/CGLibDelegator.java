package com.notayessir.common.proxy.delegator;

import com.notayessir.common.proxy.Delegator;
import com.notayessir.registry.api.bean.Reference;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 基于 CGLib 实现的代理
 */
public class CGLibDelegator extends Delegator implements MethodInterceptor {

    public CGLibDelegator(Reference reference) {
        super(reference);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return super.invoke(proxy, method, args, reference);
    }
}
