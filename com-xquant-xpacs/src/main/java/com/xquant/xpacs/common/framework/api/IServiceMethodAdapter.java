package com.xquant.xpacs.common.framework.api;

import com.xquant.xpacs.common.framework.request.IRequest;

public interface IServiceMethodAdapter {

	/**
     * 调用服务方法
	 * @Method: invokeServiceMethod
	 * @Description: 反射调用服务方法
     * @param request
     * @return
     */
    Object invokeServiceMethod(IRequest request);
}
