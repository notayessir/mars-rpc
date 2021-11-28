package com.notayessir.common.spring.definition.parser;

import com.notayessir.cluster.fault.Cluster;
import com.notayessir.cluster.loadbalance.LoadBalance;
import com.notayessir.common.spring.definition.bean.ConsumerConfig;
import com.notayessir.common.spring.definition.bean.base.ClusterConfig;
import com.notayessir.common.spring.definition.bean.base.SerializeConfig;
import com.notayessir.serialize.api.Serialization;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.*;

import java.util.Objects;


/**
 * 服务消费者配置解析类
 */
public class ConsumerDefinitionParser extends BaseDefinitionParser {


    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        Document ownerDocument = element.getOwnerDocument();
        BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(ConsumerConfig.class);
        beanDefinition.addPropertyValue("clusterConfig", buildClusterConfig(ownerDocument.getElementsByTagName("mars:cluster")));
        beanDefinition.addPropertyValue("serializeConfig", buildSerializeConfig(ownerDocument.getElementsByTagName("mars:serialize")));
        beanDefinition.addPropertyValue("discoveryConfig", buildRegistryConfig(ownerDocument.getElementsByTagName("mars:discovery")));
        beanDefinition.addPropertyValue("config", true);
        return beanDefinition.getBeanDefinition();
    }


    /**
     * 解析集群配置
     * @param nodeList  原始配置节点信息
     * @return          集群配置
     */
    private ClusterConfig buildClusterConfig(NodeList nodeList){
        ClusterConfig clusterConfig = new ClusterConfig();
        if (nodeList.getLength() == 0){
            return clusterConfig;
        }
        Node item = nodeList.item(0);
        NamedNodeMap attributes = item.getAttributes();
        Node clusterStrategyNode = attributes.getNamedItem("clusterStrategy");
        Cluster.Strategy strategy = Objects.isNull(clusterStrategyNode) ? Cluster.Strategy.FAIL_FAST : Cluster.Strategy.getByValue(clusterStrategyNode.getNodeValue());
        clusterConfig.setClusterStrategy(strategy);

        Node balanceStrategyNode = attributes.getNamedItem("balanceStrategy");
        LoadBalance.Strategy balanceStrategy = Objects.isNull(balanceStrategyNode) ? LoadBalance.Strategy.RANDOM : LoadBalance.Strategy.getByValue(balanceStrategyNode.getNodeValue());
        clusterConfig.setBalanceStrategy(balanceStrategy);

        Node timeoutNode = attributes.getNamedItem("timeout");
        long timeout = Objects.isNull(timeoutNode) ? 3000L : Long.parseLong(timeoutNode.getNodeValue());
        clusterConfig.setTimeout(timeout);

        Node retryNode = attributes.getNamedItem("retry");
        int retry = Objects.isNull(retryNode) ? 2 : Integer.parseInt(retryNode.getNodeValue());
        clusterConfig.setRetry(retry);

        Node forkingNumNode = attributes.getNamedItem("forkingNumber");
        int forkingNumber = Objects.isNull(forkingNumNode) ? 3 : Integer.parseInt(forkingNumNode.getNodeValue());
        clusterConfig.setForkingNumber(forkingNumber);
        return clusterConfig;
    }

    /**
     * 解析序列化配置
     * @param nodeList  原始配置节点信息
     * @return          序列化配置
     */
    private SerializeConfig buildSerializeConfig(NodeList nodeList) {
        SerializeConfig serializeConfig = new SerializeConfig();
        if (nodeList.getLength() == 0){
            return serializeConfig;
        }
        Node item = nodeList.item(0);
        NamedNodeMap attributes = item.getAttributes();
        Node serializationNode = attributes.getNamedItem("serialization");
        Serialization.Type serialization = Objects.isNull(serializationNode) ? Serialization.Type.FASTJSON : Serialization.Type.getByValue(serializationNode.getNodeValue());
        serializeConfig.setSerialization(serialization);
        return serializeConfig;
    }

}
