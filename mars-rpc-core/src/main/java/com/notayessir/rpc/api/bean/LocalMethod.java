package com.notayessir.rpc.api.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * 若消费端调用这些方法，则只进行本地调用
 */
public enum LocalMethod {

    TO_STRING("toString", true),

    EQUALS("equals", true),

    HASHCODE("hashCode", true),

    GET_CLASS("getClass", true),

    NOTIFY("notify", false),

    NOTIFY_ALL("notifyAll", false),

    WAIT("wait", false);

    private final String methodName;
    
    private final Boolean invokable;

    public String getMethodName() {
        return methodName;
    }

    public Boolean getInvokable() {
        return invokable;
    }

    LocalMethod(String methodName, Boolean invokable) {
        this.invokable= invokable;
        this.methodName = methodName;
    }

    public static LocalMethod getByMethodName(String methodName){
        LocalMethod[] unInvokableMethods = LocalMethod.values();
        for (LocalMethod method: unInvokableMethods){
            if (StringUtils.equals(methodName, method.getMethodName())){
                return method;
            }
        }
        return null;
    }
}
