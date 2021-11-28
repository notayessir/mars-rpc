package com.notayessir.rpc.netty.remote.counter;

/**
 * 保活计数器
 */
public class KeepAliveCounter {

    /**
     * 失联次数
     */
    private int count;

    public KeepAliveCounter() {
        this.count = 1;
    }

    public int getCount() {
        return count;
    }

    public void incr(){
        count++;
    }

    public void reset() {
        count = 0;
    }
}
