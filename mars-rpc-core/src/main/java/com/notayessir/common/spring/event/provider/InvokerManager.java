package com.notayessir.common.spring.event.provider;

import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.registry.api.bean.Service;
import com.notayessir.rpc.api.Connector;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.ConnectionMeta;
import com.notayessir.rpc.api.bean.Invocation;
import com.notayessir.rpc.api.bean.InvokerMeta;
import com.notayessir.rpc.api.util.InvocationUtil;
import com.notayessir.rpc.netty.NettyConnector;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务提供者管理器，管理 invoker
 */
public class InvokerManager {

    private final static Logger LOG = LogManager.getLogger(InvokerManager.class);

    /**
     *  invoker 列表
     */
    private final List<Invoker> invokers;

    public InvokerManager(List<Invoker> invokers) {
        this.invokers = new CopyOnWriteArrayList<>();
        this.invokers.addAll(invokers);
    }


    public List<Invoker> getInvokers() {
        return invokers;
    }


    /**
     * 根据 ip+port 找到 invoker
     * @param host  ip
     * @param port  端口
     * @return      匹配的 invoker
     */
    public Invoker findInvoker(String host, Integer port) {
        for (Invoker invoker : invokers) {
            InvokerMeta invokerMeta = invoker.getInvokerMeta();
            if (StringUtils.equals(invokerMeta.getHost(), host)
                    && invokerMeta.getPort().equals(port)) {
                return invoker;
            }
        }
        return null;
    }

    /**
     * 移除 invoker
     * @param invokerMeta   invoker 信息
     */
    public void removeInvoker(InvokerMeta invokerMeta) {
        Invoker invoker = findInvoker(invokerMeta.getHost(), invokerMeta.getPort());
        if (Objects.isNull(invoker)){
            return;
        }
        invoker.setEnable(false);
        invokers.remove(invoker);
    }

    /**
     * 当 invoker 无效时，移除 invoker
     * @param service   服务提供者信息
     */
    public void removeInvoker(Service service) {
        Invoker invoker = findInvoker(service.getHost(), service.getPort());
        if (Objects.isNull(invoker)){
            return;
        }
        if (isAvailable(invoker)){
            return;
        }
        invoker.setEnable(false);
        invokers.remove(invoker);
    }

    /**
     * invoker 是否有效
     * @param invoker   目标 invoker
     * @return          是否有效
     */
    private boolean isAvailable(Invoker invoker) {
        if (!Objects.isNull(invoker) && invoker.isEnable()) {
            return pingPong(invoker);
        }
        return false;
    }

    /**
     * 根据服务提供者信息，连接并创建 invoker
     * @param service       服务提供者信息
     */
    public void addProvider(Service service) {
        Invoker invoker = findInvoker(service.getHost(), service.getPort());
        if (isAvailable(invoker)) {
            invoker.getInvokerMeta().put(service);
            return;
        }
        String host = service.getHost();
        Integer port = service.getPort();
        List<Service> list = new ArrayList<>();
        list.add(service);
        ConnectionMeta connectionMeta = new ConnectionMeta(host, port, list);
        Connector connector = new NettyConnector();
        Invoker newInvoker = connector.connect(connectionMeta);
        if (isAvailable(newInvoker)) {
            invokers.add(newInvoker);
        }
    }

    /**
     * 有效性检测
     * @param invoker   目标 invoker
     * @return          连接是否可用
     */
    private boolean pingPong(Invoker invoker) {
        Invocation invocation;
        try {
            invocation = InvocationUtil.buildPingPongInvocation(MarsRPCContext.genRequestId(), MarsRPCContext.getConsumerConfig().getSerializeConfig().getSerialization().getId());
            String resp = (String) invoker.invoke(invocation);
            return StringUtils.equalsIgnoreCase(resp, "pong");
        } catch (Throwable e) {
            LOG.error("error happened when pingPong. e:", e);
            e.printStackTrace();
            return false;
        }
    }

}
