package com.xquant.xpacs.common.framework.api;

import com.xquant.xpacs.common.framework.impl.ServiceMethodHandler;

import java.util.Map;

public interface IServiceContext {

	/**
     * 注册一个服务处理器
     *
	 * @Method: addServiceMethod
	 * @Description: 注册一个服务处理器
     * @param methodName
     * @param version
     * @param serviceMethodHandler
     */
    void addServiceMethod(String methodName, String version, ServiceMethodHandler serviceMethodHandler);

    /**
     * 获取服务处理器
     *
	 * @Method: getServiceMethodHandler
	 * @Description: 获取服务处理器
     * @param methodName
     * @return
     */
    ServiceMethodHandler getServiceMethodHandler(String methodName, String version);

    /**
     * 是否是合法的服务方法
     *
	 * @Method: isValidMethod
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param methodName
     * @return
     */
    boolean isValidMethod(String methodName);

    /**
     * 是否存在对应的服务方法的版本号
     *
	 * @Method: isValidVersion
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param methodName
     * @param version
     * @return
     */
    boolean isValidVersion(String methodName, String version);

    /**
     * 获取所有的处理器列表
     *
	 * @Method: getAllServiceMethodHandlers
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return
     */
    Map<String, ServiceMethodHandler> getAllServiceMethodHandlers();

}
