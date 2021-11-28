package com.notayessir.common.spring.definition.parser;

import com.notayessir.common.spring.definition.bean.ProviderConfig;
import com.notayessir.common.spring.definition.bean.base.ProtocolConfig;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.*;

import java.util.Objects;

/**
 * 服务提供者解析器
 */
public class ProviderDefinitionParser extends BaseDefinitionParser {


    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        Document ownerDocument = element.getOwnerDocument();
        BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(ProviderConfig.class);
        beanDefinition.addPropertyValue("registryConfig", buildRegistryConfig(ownerDocument.getElementsByTagName("mars:registry")));
        beanDefinition.addPropertyValue("protocolConfig", buildProtocolConfig(ownerDocument.getElementsByTagName("mars:protocol")));
        beanDefinition.addPropertyValue("config", true);
        return beanDefinition.getBeanDefinition();
    }


    /**
     * 极细协议配置
     * @param nodeList  原始配置节点信息
     * @return          协议配置
     */
    private ProtocolConfig buildProtocolConfig(NodeList nodeList){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        if (nodeList.getLength() == 0){
            return protocolConfig;
        }
        Node item = nodeList.item(0);
        NamedNodeMap attributes = item.getAttributes();
        String protocol = attributes.getNamedItem("protocol").getNodeValue();
        String host = attributes.getNamedItem("host").getNodeValue();
        String port = attributes.getNamedItem("port").getNodeValue();
        Node weight = attributes.getNamedItem("weight");
        protocolConfig.setWeight(Objects.isNull(weight) ? 1 : Integer.parseInt(weight.getNodeValue()));
        Node isExpose = attributes.getNamedItem("isExpose");
        protocolConfig.setExpose(Objects.isNull(isExpose) || Boolean.getBoolean(isExpose.getNodeValue()));
        protocolConfig.setProtocol(protocol);
        protocolConfig.setHost(host);
        protocolConfig.setPort(Integer.parseInt(port));
        return protocolConfig;
    }

}
