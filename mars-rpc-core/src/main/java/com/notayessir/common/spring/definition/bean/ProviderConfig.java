package com.notayessir.common.spring.definition.bean;

import com.notayessir.common.spring.definition.bean.base.BaseConfig;
import com.notayessir.common.spring.definition.bean.base.ProtocolConfig;
import com.notayessir.common.spring.definition.bean.base.RegistryConfig;


/**
 * 服务提供者配置
 */
public class ProviderConfig extends BaseConfig {


    /**
     * 协议配置
     */
    private ProtocolConfig protocolConfig;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig;

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }
}
