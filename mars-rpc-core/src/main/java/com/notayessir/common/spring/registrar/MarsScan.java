package com.notayessir.common.spring.registrar;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


/**
 * 仅仅对用 ComponentScan 进行包装，用于扫描该框架组件
 */
@Configuration
@ComponentScan("com.notayessir")
@ImportResource("classpath:mars.xml")
public class MarsScan {}
