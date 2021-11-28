package com.notayessir.rpc.api.annotation;

import java.lang.annotation.*;


/**
 * 服务端使用的注解，在 spring 容器的的基础上，声明为远程服务
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RPCService {

    /**
     * 该服务基于哪个接口实现
     */
    Class<?> rpcInterface();

}
