package com.notayessir.rpc.netty;

import com.notayessir.rpc.api.Connector;
import com.notayessir.rpc.api.Invoker;
import com.notayessir.rpc.api.bean.ConnectionMeta;
import com.notayessir.rpc.api.bean.InvokerMeta;
import com.notayessir.rpc.netty.remote.NettyClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * netty 连接器
 */
public class NettyConnector implements Connector {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Override
    public Invoker connect(ConnectionMeta connectionMeta) {
        try {
            InvokerMeta invokerMeta = new InvokerMeta(connectionMeta);
            NettyClient nettyClient = new NettyClient(invokerMeta);
            nettyClient.start();
            return new NettyInvoker(nettyClient, invokerMeta, true);
        }catch (Exception e){
            LOG.error("error happened when connect to server", e);
            return null;
        }
    }


    @Override
    public List<Invoker> connect(List<ConnectionMeta> connectionMetas) {
        List<Invoker> list = new ArrayList<>();
        for (ConnectionMeta connectionMeta : connectionMetas){
            Invoker invoker = connect(connectionMeta);
            if (!Objects.isNull(invoker)){
                list.add(invoker);
            }
        }
        return list;
    }
}
