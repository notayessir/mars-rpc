package com.notayessir.common.spring.definition.parser;

import com.notayessir.common.spring.definition.bean.CommonConfig;
import com.notayessir.common.spring.definition.bean.base.ExecutorConfig;
import io.netty.util.NettyRuntime;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.*;

import java.util.Objects;

/**
 * 通用的 XML 解析器，当前只解析线程池配置
 */
public class CommonDefinitionParser extends BaseDefinitionParser{


    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        Document ownerDocument = element.getOwnerDocument();
        BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(CommonConfig.class);
        beanDefinition.addPropertyValue("executorConfig", buildExecutorConfig(ownerDocument.getElementsByTagName("mars:executor")));
        beanDefinition.addPropertyValue("config", true);
        return beanDefinition.getBeanDefinition();
    }

    /**
     * 解析线程池配置
     * @param nodeList  原始配置节点信息
     * @return          线程池配置
     */
    private ExecutorConfig buildExecutorConfig(NodeList nodeList){
        ExecutorConfig executorConfig = new ExecutorConfig();
        if (nodeList.getLength() == 0){
            return executorConfig;
        }
        Node item = nodeList.item(0);
        NamedNodeMap attributes = item.getAttributes();
        Node coreSizeNode = attributes.getNamedItem("coreSize");
        int coreSize = Objects.isNull(coreSizeNode) ? NettyRuntime.availableProcessors() * 2 : Integer.parseInt(coreSizeNode.getNodeValue());
        executorConfig.setCoreSize(coreSize);

        Node maximumSizeNode = attributes.getNamedItem("maximumSize");
        int maximumSize = Objects.isNull(coreSizeNode) ? NettyRuntime.availableProcessors() * 3 : Integer.parseInt(maximumSizeNode.getNodeValue());
        executorConfig.setMaximumSize(maximumSize);

        Node queueSizeNode = attributes.getNamedItem("queueSize");
        int queueSize = Objects.isNull(queueSizeNode) ? 1024 : Integer.parseInt(queueSizeNode.getNodeValue());
        executorConfig.setQueueSize(queueSize);
        return executorConfig;
    }

}
