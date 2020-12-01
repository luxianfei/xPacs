package com.xquant.xpacs.common.framework.request;

public interface IRequest {

	/**
     * 获取服务方法的上下文
	 * @Method: getRequestContext
	 * @Description: 获取服务方法的上下文
     * @return
     */
    IRequestContext getRequestContext();
}
