package com.notayessir.common.spring.definition.bean.base;

/**
 * 基础配置
 */
public class BaseConfig {

    /**
     * 该配置项是否在配置文件中配置
     */
    protected boolean config;

    public boolean isConfig() {
        return config;
    }

    public void setConfig(boolean config) {
        this.config = config;
    }
}
