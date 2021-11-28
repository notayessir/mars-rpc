package com.notayessir.registry.api.util;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.registry.api.bean.Service;

/**
 * url 解析器
 */
public class URLUtil {

    /**
     * 将服务解析成 url 暴露到注册中心上
     * @param service   服务信息
     * @return          url 字符串
     */
    public static String beanToJSON(Service service){
        return JSONObject.toJSONString(service);
    }

    /**
     * 将 url 解析成服务
     * @param url   url
     * @return      服务信息
     */
    public static Service jsonToBean(String url){
        return JSONObject.parseObject(url, Service.class);
    }

}
