package com.notayessir.common.spring.registrar;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * 用于扫描该框架组件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MarsScan.class)
public @interface EnableMars {
}
