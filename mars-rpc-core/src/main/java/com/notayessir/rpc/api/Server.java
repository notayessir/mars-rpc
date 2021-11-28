package com.notayessir.rpc.api;

import java.util.concurrent.CountDownLatch;

public interface Server extends Closable{

    void start(CountDownLatch prepareLatch);

}
