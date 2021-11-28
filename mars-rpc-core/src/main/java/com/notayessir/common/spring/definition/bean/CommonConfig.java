package com.notayessir.common.spring.definition.bean;

import com.notayessir.common.spring.definition.bean.base.BaseConfig;
import com.notayessir.common.spring.definition.bean.base.ExecutorConfig;

/**
 * 通用的配置
 */
public class CommonConfig extends BaseConfig {

    private ExecutorConfig executorConfig;

    public ExecutorConfig getExecutorConfig() {
        return executorConfig;
    }

    public void setExecutorConfig(ExecutorConfig executorConfig) {
        this.executorConfig = executorConfig;
    }

}
