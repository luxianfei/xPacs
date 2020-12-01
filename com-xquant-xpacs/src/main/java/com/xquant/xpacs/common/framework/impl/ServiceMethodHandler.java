package com.xquant.xpacs.common.framework.impl;

import com.xquant.xpacs.common.framework.request.IRequest;
import com.xquant.xpacs.common.framework.response.BaseResponse;

import java.lang.reflect.Method;
import java.util.List;

 /**
 * @ClassName: ServiceMethodHandler
 * @Description: 指标计算服务方法处理器
 * @author: deming.ye
 * @date: 2018年7月26日 上午10:34:22
 *
 */
public class ServiceMethodHandler {

	//处理器对象
    private Object handler;

    //处理器的处理方法
    private Method handlerMethod;

    private ServiceMethodDefinition serviceMethodDefinition;

    //处理方法的请求对象类
    private Class<? extends Object> requestType = IRequest.class;

    //处理方法的返回对象类
    private Class<? extends Object> responseType = BaseResponse.class;

    //无需签名的字段列表
    private List<String> ignoreSignFieldNames;

    //是否是实现类
    private boolean requestImplType;

    public ServiceMethodHandler() {
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public Class<? extends Object> getRequestType() {
        return requestType;
    }

    public void setRequestType(Class<? extends Object> requestType) {
        this.requestType = requestType;
    }


    public boolean isHandlerMethodWithParameter() {
        return this.requestType != null;
    }

    public void setIgnoreSignFieldNames(List<String> ignoreSignFieldNames) {
        this.ignoreSignFieldNames = ignoreSignFieldNames;
    }

    public List<String> getIgnoreSignFieldNames() {
        return ignoreSignFieldNames;
    }

    public ServiceMethodDefinition getServiceMethodDefinition() {
        return serviceMethodDefinition;
    }

    public void setServiceMethodDefinition(ServiceMethodDefinition serviceMethodDefinition) {
        this.serviceMethodDefinition = serviceMethodDefinition;
    }

    public static String methodWithVersion(String methodName, String version) {
        return methodName + "#" + version;
    }

    public boolean isRequestImplType() {
        return requestImplType;
    }

    public void setRequestImplType(boolean requestImplType) {
        this.requestImplType = requestImplType;
    }

	public Class<? extends Object> getResponseType() {
		return responseType;
	}

	public void setResponseType(Class<? extends Object> responseType) {
		this.responseType = responseType;
	}
}
