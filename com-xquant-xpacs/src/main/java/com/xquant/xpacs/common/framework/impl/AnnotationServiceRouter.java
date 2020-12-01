package com.xquant.xpacs.common.framework.impl;

import com.xquant.xpacs.common.framework.api.IServiceContext;
import com.xquant.xpacs.common.framework.api.IServiceMethodAdapter;
import com.xquant.xpacs.common.framework.api.IServiceRouter;
import com.xquant.xpacs.common.framework.config.ServiceParameterNames;
import com.xquant.xpacs.common.framework.error.ErrorMessageSourceHandler;
import com.xquant.xpacs.common.framework.error.MainError;
import com.xquant.xpacs.common.framework.error.MainErrorType;
import com.xquant.xpacs.common.framework.error.ServiceException;
import com.xquant.xpacs.common.framework.request.DefaultRequestContextBuilder;
import com.xquant.xpacs.common.framework.request.IRequest;
import com.xquant.xpacs.common.framework.request.IRequestContext;
import com.xquant.xpacs.common.framework.request.IRequestContextBuilder;
import com.xquant.xpacs.common.framework.response.ErrorResponse;
import com.xquant.xpacs.common.framework.response.ServiceUnavailableErrorResponse;
import com.xquant.xpacs.common.framework.security.ISecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
@Component
public class AnnotationServiceRouter implements IServiceRouter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private IServiceContext context;

    private IRequestContextBuilder requestContextBuilder;

    private IServiceMethodAdapter serviceMethodAdapter = new AnnotationServiceMethodAdapter();

    private FormattingConversionService formattingConversionService;

    private ThreadPoolTaskExecutor executor;

    // 错误信息的国际化信息
    private ResourceBundleMessageSource errorMessageSource;

	private ISecurityManager securityManager;

	@Override
	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}
	@Override
	public void setRequestContextBuilder(IRequestContextBuilder moduleRequestContextBuilder) {
		this.requestContextBuilder = moduleRequestContextBuilder;
	}

	@Override
	public void setErrorMessageSource(ResourceBundleMessageSource errorMessageSource) {
		this.errorMessageSource = errorMessageSource;
		ErrorMessageSourceHandler.setErrorMessageSource(this.errorMessageSource);
	}

	@Override
	public void setSecurityManager(ISecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void startup() {
		//初始化类型转换器
        if (this.formattingConversionService == null) {
            this.formattingConversionService = getDefaultConversionService();
        }
        //实例化ServletRequestContextBuilder
        this.requestContextBuilder = new DefaultRequestContextBuilder(this.formattingConversionService);
        //创建框架上下文
        this.context = buildContext();
		// 设置国际化资源信息
		ErrorMessageSourceHandler.setErrorMessageSource(this.errorMessageSource);

	}

	private IServiceContext buildContext() {
		IServiceContext context = new ServiceContext(this.applicationContext);
        return context;
    }

	/**
     * 获取默认的格式化转换器
     * @Method: getDefaultConversionService
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return
     */
    private FormattingConversionService getDefaultConversionService() {
        FormattingConversionServiceFactoryBean serviceFactoryBean = new FormattingConversionServiceFactoryBean();
        serviceFactoryBean.afterPropertiesSet();
        return serviceFactoryBean.getObject();
    }

	@Override
	public Object annotationService(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		IRequestContext requestContext = null;
		IRequest request = null;
		try {
			requestContext = requestContextBuilder.buildRequestContext(
                    context, servletRequest, servletResponse);
        	request = requestContextBuilder.buildRequest(requestContext);

            requestContext.setResponse(doService(request));

		} catch (Throwable e) {
            if (requestContext != null) {
                String method = requestContext.getMethod();
                ServiceUnavailableErrorResponse errorResponse = new ServiceUnavailableErrorResponse(method, Locale.SIMPLIFIED_CHINESE);
                requestContext.setResponse(errorResponse);
            } else {
                throw new ServiceException("IRequestContext is null.", e);
            }
            e.printStackTrace();
        } finally {
            requestContext.setServiceEndTime(System.currentTimeMillis());
		}
		return requestContext.getResponse();
	}

	/**
     * 服务处理方法
	 * @Method: doService
	 * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param request
     * @return
     */
    private Object doService(IRequest request) {
        Object response = null;
        IRequestContext requestContext = request.getRequestContext();
        if (requestContext.getMethod() == null) {
        	response = new ErrorResponse(ErrorMessageSourceHandler.getMainError(
                    MainErrorType.MISSING_MODULE_NO, Locale.SIMPLIFIED_CHINESE,
                    ServiceParameterNames.getMethod()));
        } else if (!context.isValidMethod(requestContext.getMethod())) {
            MainError invalidMethodError = ErrorMessageSourceHandler.getMainError(
                    MainErrorType.INVALID_MODULE_NO, Locale.SIMPLIFIED_CHINESE,requestContext.getMethod());
            response = new ErrorResponse(invalidMethodError);
        } else {
            try {
            	response = serviceMethodAdapter.invokeServiceMethod(request);
            } catch (Exception e) {
                logger.error("调用" + requestContext.getMethod() + "时发生异常，异常信息为：" + e.getMessage());
                e.printStackTrace();
                response = new ServiceUnavailableErrorResponse(requestContext.getMethod(), Locale.SIMPLIFIED_CHINESE);
            }
        }
        return response;
    }
}
