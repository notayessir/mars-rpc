package com.notayessir.common.spring.event.provider;

import org.springframework.stereotype.Component;

/**
 * 用于检测连接是否可用
 */
@Component
public class PingPongService {

    public String ping() {
        return "pong";
    }
}
