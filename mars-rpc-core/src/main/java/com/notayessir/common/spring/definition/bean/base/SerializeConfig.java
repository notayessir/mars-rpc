package com.notayessir.common.spring.definition.bean.base;

import com.notayessir.serialize.api.Serialization;

/**
 * 服务消费者使用的序列化配置
 */
public class SerializeConfig {

    private Serialization.Type serialization;

    public SerializeConfig() {
        this.serialization = Serialization.Type.FASTJSON;
    }


    public Serialization.Type getSerialization() {
        return serialization;
    }

    public void setSerialization(Serialization.Type serialization) {
        this.serialization = serialization;
    }
}
