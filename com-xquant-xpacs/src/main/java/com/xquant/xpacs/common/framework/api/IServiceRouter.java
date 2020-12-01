package com.xquant.xpacs.common.framework.api;

import com.xquant.xpacs.common.framework.request.IRequestContextBuilder;
import com.xquant.xpacs.common.framework.security.ISecurityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IServiceRouter {

	public void setExecutor(ThreadPoolTaskExecutor executor);

	public void setRequestContextBuilder(IRequestContextBuilder requestContextBuilder);

	public void setErrorMessageSource(ResourceBundleMessageSource errorMessageSource);

	public void setSecurityManager(ISecurityManager securityManager);

	public void setApplicationContext(ApplicationContext applicationContext);

	public void startup();

	public Object annotationService(HttpServletRequest request, HttpServletResponse response);
}
