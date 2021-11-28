package com.notayessir.common.proxy;

import com.notayessir.registry.api.bean.Reference;
import com.notayessir.rpc.api.bean.LocalMethod;
import com.notayessir.rpc.api.bean.RPCException;
import com.notayessir.rpc.api.dispatcher.ReqDispatcher;

import java.lang.reflect.Method;
import java.util.Objects;

public class Delegator {

    protected Reference reference;

    public Delegator(Reference reference) {
        this.reference = reference;
    }


    protected Object invoke(Object proxy, Method method, Object[] args, Reference reference) throws Throwable{
        LocalMethod localMethod = LocalMethod.getByMethodName(method.getName());
        if (Objects.isNull(localMethod)) {
            return ReqDispatcher.dispatch(method, args, reference);
        } else if (localMethod.getInvokable()) {
            switch (localMethod) {
                case HASHCODE:
                    return System.identityHashCode(proxy);
                case GET_CLASS:
                    return proxy.getClass();
                case EQUALS:
                    return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
                case TO_STRING:
                    return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
            }
        }
        throw new RPCException(RPCException.Message.CLIENT_NOT_SUPPORT_METHOD);
    }



}
