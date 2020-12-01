package com.xquant.xpacs.common.framework.request;

import com.xquant.xpacs.common.framework.api.IServiceContext;
import com.xquant.xpacs.common.framework.impl.ServiceMethodDefinition;
import com.xquant.xpacs.common.framework.impl.ServiceMethodHandler;

import java.util.Map;

public interface IRequestContext {

	/**
     * 获取框架的上下文
	 * @Method: getContext
	 * @Description: 获取框架的上下文
     * @return
     */
    IServiceContext getContext();

    /**
     * 获取应用的appKey
	 * @Method: getAppKey
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return
     */
    String getAppKey();

    /**
     * 获取服务的方法
	 * @Method: getMethod
	 * @Description: 获取服务的方法
     * @return
     */
    String getMethod();

    /**
     * 获取服务的版本号
	 * @Method: getVersion
	 * @Description: 获取服务的版本号
     * @return
     */
    String getVersion();

    /**
     * 获取请求的原对象
	 * @Method: getRawRequest
	 * @Description: 获取请求的原对象
     * @return
     */
    Object getRawRequest();

    /**
     * 获取请求的原响应对象
	 * @Method: getRawResponse
	 * @Description: 获取请求的原响应对象
     * @return
     */
    Object getRawResponse();

    /**
     * 设置服务开始时间
	 * @Method: setServiceBeginTime
	 * @Description: 设置服务开始时间
     * @param serviceBeginTime
     */
    void setServiceBeginTime(long serviceBeginTime);

    /**
     * 获取服务开始时间
	 * @Method: getServiceBeginTime
	 * @Description: 获取服务开始时间，单位为毫秒，为-1表示无意义
     * @return
     */
    long getServiceBeginTime();

    /**
     * 设置服务结束时间
	 * @Method: setServiceEndTime
	 * @Description: 设置服务结束时间
     * @param serviceEndTime
     */
    void setServiceEndTime(long serviceEndTime);

    /**
     * 获取服务结束时间
	 * @Method: getServiceEndTime
	 * @Description: 获取服务结束时间，单位为毫秒，为-1表示无意义
     * @return
     */
    long getServiceEndTime();

    /**
     * 获取服务方法对象信息
     *
	 * @Method: getServiceMethodDefinition
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return
     */
    ServiceMethodDefinition getServiceMethodDefinition();

    /**
     * 获取服务方法处理器
	 * @Method: getServiceMethodHandler
	 * @Description: 获取服务方法处理器
     * @return
     */
    ServiceMethodHandler getServiceMethodHandler();


    /**
     * 设置响应对象
	 * @Method: setResponse
	 * @Description: 设置响应对象
     * @param response
     */
    void setResponse(Object response);

    /**
     * 返回响应对象
     *
	 * @Method: getResponse
	 * @Description: 返回响应对象
     * @return
     */
    Object getResponse();


    /**
     * 获取特定属性
     *
	 * @Method: getAttribute
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param name
     * @return
     */
    Object getAttribute(String name);

    /**
     * 设置属性的值
     *
	 * @Method: setAttribute
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param name
     * @param value
     */
    void setAttribute(String name, Object value);

    /**
     * 获取请求参数列表
     *
	 * @Method: getAllParams
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return
     */
    Map<String, Object> getAllParams();

    /**
     * 获取请求参数值
     *
	 * @Method: getParamValue
	 * @Description: 获取请求参数值
     * @param paramName
     * @return
     */
    Object getParamValue(String paramName);

    /**
     * 获取请求ID
	 * @Method: getRequestId
	 * @Description: 获取请求ID，是一个唯一的UUID，每次请求对应一个唯一的ID
     * @return
     */
    String getRequestId();

    /**
     * 设置请求对象
	 * @Method: setResponse
	 * @Description: 设置请求对象
     * @param response
     */
    void setRequest(Object response);

    /**
     * 返回请求对象
     *
	 * @Method: getResponse
	 * @Description: 返回请求对象
     * @return
     */
    Object getRequest();
}
