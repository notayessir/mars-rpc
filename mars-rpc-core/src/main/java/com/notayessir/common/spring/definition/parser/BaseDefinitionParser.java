package com.notayessir.common.spring.definition.parser;

import com.notayessir.common.spring.definition.bean.base.RegistryConfig;
import com.notayessir.registry.api.bean.RegistryConst;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 根据 Spring XML 拓展的抽象解析类
 */
public abstract class BaseDefinitionParser extends AbstractBeanDefinitionParser {

    /**
     * 配置文件中注册中心的配置信息
     * @param nodeList      原始配置节点信息
     * @return              注册中心配置
     */
    protected RegistryConfig buildRegistryConfig(NodeList nodeList) {
        RegistryConfig registryConfig = new RegistryConfig();
        if (nodeList.getLength() == 0){
            return registryConfig;
        }
        Node item = nodeList.item(0);
        NamedNodeMap attributes = item.getAttributes();
        String registry = attributes.getNamedItem("registry").getNodeValue();
        String host = attributes.getNamedItem("host").getNodeValue();
        String port = attributes.getNamedItem("port").getNodeValue();
        registryConfig.setRegistry(RegistryConst.Registry.getByValue(registry));
        registryConfig.setHost(host);
        registryConfig.setPort(Integer.parseInt(port));
        return registryConfig;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
