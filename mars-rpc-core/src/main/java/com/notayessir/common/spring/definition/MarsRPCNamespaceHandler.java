package com.notayessir.common.spring.definition;

import com.notayessir.common.spring.definition.parser.CommonDefinitionParser;
import com.notayessir.common.spring.definition.parser.ConsumerDefinitionParser;
import com.notayessir.common.spring.definition.parser.ProviderDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 自定义 Spring 命名空间，注册服务消费者、提供者解析器
 */
public class MarsRPCNamespaceHandler extends NamespaceHandlerSupport  {


    public void init() {
        registerBeanDefinitionParser("common", new CommonDefinitionParser());
        registerBeanDefinitionParser("provider", new ProviderDefinitionParser());
        registerBeanDefinitionParser("consumer", new ConsumerDefinitionParser());
    }
}
