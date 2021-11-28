package com.notayessir.rpc.api.annotation;


import java.lang.annotation.*;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RPCCustomizationArr {

    RPCCustomization[] value();

}
