package com.notayessir.common.spring.definition.bean.base;

/**
 * 服务提供者的 RPC 框架协议配置
 */
public class ProtocolConfig {

    /**
     * 服务协议
     */
    private String protocol;

    /**
     * 服务器 IP
     */
    private String host;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 权重
     */
    private int weight;

    /**
     * 是否暴露服务到注册中心
     */
    private boolean expose;



    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isExpose() {
        return expose;
    }

    public void setExpose(boolean expose) {
        this.expose = expose;
    }
}
