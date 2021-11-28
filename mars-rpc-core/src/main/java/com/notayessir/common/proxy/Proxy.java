package com.notayessir.common.proxy;

import com.notayessir.registry.api.bean.ReferenceCache;

/**
 * 代理接口
 */
public interface Proxy {

    /**
     * 为接口引用创建代理
     * @param referenceCache    引用信息集合
     * @throws Exception        创建代理异常时抛出
     */
    void gen(ReferenceCache referenceCache) throws Exception;
}
