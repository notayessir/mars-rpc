package com.notayessir.rpc.api.bean;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.common.spring.MarsRPCContext;
import com.notayessir.rpc.api.util.InvocationUtil;
import com.notayessir.rpc.netty.remote.bean.ResponseStatus;
import com.notayessir.rpc.netty.remote.bean.frame.RequestBody;
import com.notayessir.rpc.netty.remote.bean.frame.RequestFrame;
import com.notayessir.rpc.netty.remote.bean.frame.ResponseFrame;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;


/**
 * 响应任务
 */
public class ResponseTask implements Callable<ResponseFrame> {

    private final static Logger LOG = LogManager.getLogger(ResponseTask.class);

    /**
     * 请求帧
     */
    private final RequestFrame requestFrame;

    /**
     * 响应帧
     */
    private final ResponseFrame responseFrame;

    public ResponseTask(RequestFrame requestFrame) {
        this.requestFrame = requestFrame;
        this.responseFrame = InvocationUtil.toResponseFrame(requestFrame);
    }

    public ResponseFrame getResponseFrame() {
        return responseFrame;
    }

    @Override
    public ResponseFrame call(){
        RequestBody requestBody = requestFrame.getRequestBody();
        Object result;
        try {
            result = doInvoke(requestBody);
        } catch (Throwable e) {
            LOG.error("exception happened when doInvoke, requestId:{}, e:{}", requestFrame.getRequestId(), e);
            responseFrame.setStatus(ResponseStatus.SERVER_BUSINESS_ERROR.getValue());
            result = e.getMessage();
        }
        responseFrame.setInvocationResult(result);
        return responseFrame;
    }

    /**
     * 根据请求调用对象的方法
     * @param requestBody   请求信息
     * @return              调用结果
     * @throws Throwable    调用过程异常时抛出
     */
    private Object doInvoke(RequestBody requestBody) throws Throwable {
        Class<?>[] clazzArr = null;
        Object[] argClassInstanceArr = null;
        List<String> paramNames = requestBody.getParamNames();
        List<Object> paramValues = requestBody.getParamValues();
        if (!Objects.isNull(paramNames) && !paramNames.isEmpty()){
            clazzArr = new Class[paramNames.size()];
            argClassInstanceArr = new Object[paramNames.size()];
            for (int j = 0; j < paramNames.size(); j++) {
                clazzArr[j] = Class.forName(paramNames.get(j));
                argClassInstanceArr[j] = JSONObject.parseObject(JSONObject.toJSONString(paramValues.get(j)), clazzArr[j]);
            }
        }
        Object bean;
        if (StringUtils.isNotBlank(requestBody.getServiceName())){
            bean = MarsRPCContext.getBean(requestBody.getServiceName());
        }else {
            Class<?> serviceClz = Class.forName(requestBody.getInterfaceName());
            bean = MarsRPCContext.getBean(serviceClz);
        }
        Class<?> beanClass = bean.getClass();
        Method method = beanClass.getMethod(requestBody.getMethodName(), clazzArr);
        return method.invoke(bean, argClassInstanceArr);
    }
}
