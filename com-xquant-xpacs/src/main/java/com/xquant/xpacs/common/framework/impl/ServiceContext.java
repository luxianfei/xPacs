package com.xquant.xpacs.common.framework.impl;

import com.xquant.common.annotation.ServiceMethod;
import com.xquant.common.annotation.ServiceMethodBean;
import com.xquant.xpacs.common.framework.api.IServiceContext;
import com.xquant.xpacs.common.framework.error.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServiceContext implements IServiceContext {

	protected static Logger logger = LoggerFactory.getLogger(ServiceContext.class);

    private final Map<String, ServiceMethodHandler> serviceHandlerMap = new HashMap<String, ServiceMethodHandler>();

    private final Set<String> serviceMethods = new HashSet<String>();

    public ServiceContext(ApplicationContext context) {
        registerFromContext(context);
    }

    /**
     * 扫描Spring容器中的Bean，查找有标注{@link ServiceMethod}注解的服务方法，将它们注册到{@link IIndexContext}中缓存起来。
     *
     * @Method: registerFromContext
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param context
     * @throws BeansException
     */
    private void registerFromContext(final ApplicationContext context) throws BeansException {
        if (logger.isDebugEnabled()) {
            logger.debug("对Spring上下文中的Bean进行扫描，查找Index服务方法: " + context);
        }
        String[] beanNames = context.getBeanNamesForType(Object.class);
        for (final String beanName : beanNames) {
            Class<?> handlerType = context.getType(beanName);
            //只对标注 ServiceMethodBean的Bean进行扫描
            if(AnnotationUtils.findAnnotation(handlerType, ServiceMethodBean.class) != null){
                ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() {
                            @Override
                			public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                                ReflectionUtils.makeAccessible(method);

                                ServiceMethod serviceMethod = AnnotationUtils.findAnnotation(method, ServiceMethod.class);
                                ServiceMethodBean serviceMethodBean = AnnotationUtils.findAnnotation(method.getDeclaringClass(), ServiceMethodBean.class);

                                ServiceMethodDefinition definition = null;
                                if (serviceMethodBean != null) {
                                    definition = buildServiceMethodDefinition(serviceMethodBean, serviceMethod);
                                } else {
                                    definition = buildServiceMethodDefinition(serviceMethod);
                                }
                                ServiceMethodHandler serviceMethodHandler = new ServiceMethodHandler();
                                serviceMethodHandler.setServiceMethodDefinition(definition);

                                //1.set handler
                                serviceMethodHandler.setHandler(context.getBean(beanName));
                                serviceMethodHandler.setHandlerMethod(method);


                                if (method.getParameterTypes().length > 1) {
	                              	throw new ServiceException(method.getDeclaringClass().getName() + "." + method.getName()
	                                  + "有多个入参，目前仅支持1个入参。");
	                            } else if (method.getParameterTypes().length == 1) {
	                            	Class<?> paramType = method.getParameterTypes()[0];
	                                if (!ClassUtils.isAssignable(Object.class, paramType)) {
	                                	throw new ServiceException(method.getDeclaringClass().getName() + "." + method.getName()
	                                              + "的入参必须是" + Object.class.getName());
	                            }
	                                boolean requestImplType = !(paramType.isAssignableFrom(Object.class));
	                                serviceMethodHandler.setRequestImplType(requestImplType);
	                                serviceMethodHandler.setRequestType((Class<? extends Object>) paramType);
	                            } else {
	                              	serviceMethodHandler.setRequestType(null);
	                                  logger.info(method.getDeclaringClass().getName() + "." + method.getName() + "无入参");
	                            }
                                Class<?> returnType = method.getReturnType();
                                serviceMethodHandler.setResponseType(returnType);

                                addServiceMethod(definition.getMethod(), definition.getVersion(), serviceMethodHandler);

                                if (logger.isDebugEnabled()) {
                                    logger.debug("注册服务方法：" + method.getDeclaringClass().getCanonicalName() +
                                            "#" + method.getName() + "(..)");
                                }
                            }
                        },
                        new ReflectionUtils.MethodFilter() {
                        	@Override
                            public boolean matches(Method method) {
                                return !method.isSynthetic() && AnnotationUtils.findAnnotation(method, ServiceMethod.class) != null;
                            }
                        }
                );
            }
        }
        if (context.getParent() != null) {
            registerFromContext(context.getParent());
        }
        if (logger.isInfoEnabled()) {
            logger.info("共注册了" + serviceHandlerMap.size() + "个服务方法");
        }
    }

    private ServiceMethodDefinition buildServiceMethodDefinition(ServiceMethodBean serviceMethodBean, ServiceMethod serviceMethod) {
        ServiceMethodDefinition definition = new ServiceMethodDefinition();
        definition.setMethodGroup(serviceMethodBean.group());
        definition.setMethodGroupTitle(serviceMethodBean.groupTitle());
        definition.setTimeout(serviceMethodBean.timeout());
        definition.setVersion(serviceMethodBean.version());

        //如果ServiceMethod所提供的值和ServiceMethodGroup不一样，覆盖之
        definition.setMethod(serviceMethod.method());
        definition.setMethodTitle(serviceMethod.title());

        if (!ServiceMethodDefinition.DEFAULT_GROUP.equals(serviceMethod.group())) {
            definition.setMethodGroup(serviceMethod.group());
        }

        if (!ServiceMethodDefinition.DEFAULT_GROUP_TITLE.equals(serviceMethod.groupTitle())) {
            definition.setMethodGroupTitle(serviceMethod.groupTitle());
        }

        if (serviceMethod.timeout() > 0) {
            definition.setTimeout(serviceMethod.timeout());
        }

        return definition;
    }

    private ServiceMethodDefinition buildServiceMethodDefinition(ServiceMethod serviceMethod) {
        ServiceMethodDefinition definition = new ServiceMethodDefinition();
        definition.setMethod(serviceMethod.method());
        definition.setMethodTitle(serviceMethod.title());
        definition.setMethodGroup(serviceMethod.group());
        definition.setMethodGroupTitle(serviceMethod.groupTitle());
        definition.setTimeout(serviceMethod.timeout());
        definition.setVersion(serviceMethod.version());
        return definition;
    }

	/**
	 * <p>Title: addServiceMethod</p>
	 * <p>Description: </p>
	 * @param methodName
	 * @param version
	 * @param serviceMethodHandler
	 * @see com.xquant.index.IIndexContext#addServiceMethod(String, String, com.xquant.index.support.ServiceMethodHandler)
	 */
	@Override
	public void addServiceMethod(String methodName, String version,
			ServiceMethodHandler serviceMethodHandler) {
		serviceMethods.add(methodName);
        serviceHandlerMap.put(ServiceMethodHandler.methodWithVersion(methodName, version), serviceMethodHandler);
	}

	/**
	 * <p>Title: getServiceMethodHandler</p>
	 * <p>Description: </p>
	 * @param methodName
	 * @param version
	 * @return
	 * @see com.xquant.index.IIndexContext#getServiceMethodHandler(String, String)
	 */
	@Override
	public ServiceMethodHandler getServiceMethodHandler(String methodName,
			String version) {
		return serviceHandlerMap.get(ServiceMethodHandler.methodWithVersion(methodName, version));
	}

	/**
	 * <p>Title: isValidMethod</p>
	 * <p>Description: </p>
	 * @param methodName
	 * @return
	 * @see com.xquant.index.IIndexContext#isValidMethod(String)
	 */
	@Override
	public boolean isValidMethod(String methodName) {
		return serviceMethods.contains(methodName);
	}

	/**
	 * <p>Title: isValidVersion</p>
	 * <p>Description: </p>
	 * @param methodName
	 * @param version
	 * @return
	 * @see com.xquant.index.IIndexContext#isValidVersion(String, String)
	 */
	@Override
	public boolean isValidVersion(String methodName, String version) {
		return serviceHandlerMap.containsKey(ServiceMethodHandler.methodWithVersion(methodName, version));
	}

	/**
	 * <p>Title: getAllServiceMethodHandlers</p>
	 * <p>Description: </p>
	 * @return
	 * @see com.xquant.index.IIndexContext#getAllServiceMethodHandlers()
	 */
	@Override
	public Map<String, ServiceMethodHandler> getAllServiceMethodHandlers() {
		return serviceHandlerMap;
	}

}
