package com.notayessir.registry.api.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * 注册中心常量
 */
public interface RegistryConst {

    /**
     * 所在的 zk 根节点
     */
    String PATH_ROOT = "/mars";

    /**
     * 提供者节点
     */
    String PROVIDERS = "/providers";

    String PATH_SLASH = "/";

    String HOST_SPLIT = ":";

    String SERVICE_INFO_PATH = PATH_ROOT + "/%s" + PROVIDERS + "/%s";

    /**
     * 注册中心，未来可能会整合更多注册中心
     */
    enum Registry{

        ZOOKEEPER("zookeeper");

        private final String value;

        Registry(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Registry getByValue(String registry){
            Registry[] registries = Registry.values();
            for (Registry reg : registries){
                if (StringUtils.equalsIgnoreCase(registry, reg.value)){
                    return reg;
                }
            }
            return null;
        }
    }

}
