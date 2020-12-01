package com.xquant.xpacs.common.framework.request;

import com.xquant.xpacs.common.framework.api.IServiceContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 /**
 * @ClassName: IRequestContextBuilder
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: deming.ye
 * @date: 2018年7月26日 上午10:40:43
 *
 */
public interface IRequestContextBuilder {

	/**
     * 根据reqeuest及response请求响应对象，创建{@link IRequestContext}实例。绑定系统参数，请求对象
	 * @Method: buildRequestContext
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param context
     * @param request
     * @param response
     * @return
     */
	IRequestContext buildRequestContext(IServiceContext context, HttpServletRequest request, HttpServletResponse response);

    /**
     * 绑定业务参数
	 * @Method: buildRequest
	 * @Description: 绑定业务参数
     * @param requestContext
     */
    IRequest buildRequest(IRequestContext requestContext);

}
