package com.xquant.xpacs.common.framework.impl;

import com.xquant.common.exception.BusinessServiceException;
import com.xquant.xpacs.common.framework.api.IServiceMethodAdapter;
import com.xquant.xpacs.common.framework.request.IRequest;
import com.xquant.xpacs.common.framework.request.IRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class AnnotationServiceMethodAdapter implements IServiceMethodAdapter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * <p>Title: invokeServiceMethod</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	@Override
	public Object invokeServiceMethod(IRequest request) {

		try {
			IRequestContext requestContext = request.getRequestContext();
            //分析上下文中的错误
            ServiceMethodHandler serviceMethodHandler = requestContext.getServiceMethodHandler();
            if (logger.isDebugEnabled()) {
                logger.debug("执行" + serviceMethodHandler.getHandler().getClass() +
                        "." + serviceMethodHandler.getHandlerMethod().getName());
            }
            if (serviceMethodHandler.isHandlerMethodWithParameter()) {
//                return serviceMethodHandler.getHandlerMethod().invoke(
//                        serviceMethodHandler.getHandler(), request);
            	return serviceMethodHandler.getHandlerMethod().invoke(
                        serviceMethodHandler.getHandler(), requestContext.getRequest());
            } else {
                return serviceMethodHandler.getHandlerMethod().invoke(serviceMethodHandler.getHandler());
            }
		} catch (Throwable e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException inve = (InvocationTargetException) e;
                if (inve.getTargetException() instanceof BusinessServiceException) {
                	BusinessServiceException bse = (BusinessServiceException) inve.getTargetException();
                	throw bse;
                }
                // 打印异常堆栈信息
                inve.getTargetException().printStackTrace();
                throw new RuntimeException(inve.getTargetException());
            } else {
                throw new RuntimeException(e);
            }
        }
	}

}
