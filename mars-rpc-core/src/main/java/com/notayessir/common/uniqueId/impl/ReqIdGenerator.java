package com.notayessir.common.uniqueId.impl;


import com.notayessir.common.uniqueId.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;


/**
 * 使用 AtomicLong 自增生成唯一的 request id
 */
public class ReqIdGenerator implements IdGenerator {

    private final AtomicLong requestId;

    public ReqIdGenerator() {
        this.requestId = new AtomicLong();
    }

    @Override
    public Long gen() {
        return requestId.incrementAndGet();
    }

}
