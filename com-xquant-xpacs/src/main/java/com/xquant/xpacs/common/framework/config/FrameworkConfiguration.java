package com.xquant.xpacs.common.framework.config;

import com.xquant.xpacs.common.framework.api.IServiceRouter;
import com.xquant.xpacs.common.framework.impl.AnnotationServiceRouter;
import com.xquant.xpacs.common.framework.security.DefaultSecurityManager;
import com.xquant.xpacs.common.framework.security.ISecurityManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableConfigurationProperties({ ServicePoolProperties.class })
public class FrameworkConfiguration {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final int DEFAULT_CORE_POOL_SIZE = 30;
    public static final int DEFAULT_MAX_POOL_SIZE = 120;
    public static final int DEFAULT_KEEP_ALIVE_SECONDS = 3 * 60;
    public static final int DEFAULT_QUENE_CAPACITY = 10;
	private static final String ROUTER_THREAD_NAME_PREFIX = "ANNOTATION-SERVICE-CALC-";


    public static final long DEFAULT_CACHE_EXPRIRE = 24 * 60 * 60;

	private static final String I18N_ERROR = "i18n/errorMessages";

	private final ApplicationContext applicationContext;

	public FrameworkConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean(name = "annotationServiceRouter")
	public IServiceRouter annotationServiceRouter(
			@Autowired(required = false) ISecurityManager securityManager,
			ServicePoolProperties properties) {
		IServiceRouter serviceRouter = new AnnotationServiceRouter();
		serviceRouter.setApplicationContext(applicationContext);
		serviceRouter.setExecutor(getTaskExecutor(properties));

		//设置校验器
        if (securityManager == null) {
            securityManager = new DefaultSecurityManager();
        }
        serviceRouter.setSecurityManager(securityManager);

		//设置国际化错误资源
		serviceRouter.setErrorMessageSource(getMessageSource());

		serviceRouter.startup();

		return serviceRouter;
	}

	public ResourceBundleMessageSource getMessageSource() {
		HashSet<String> baseNamesSet = new HashSet<String>();
        baseNamesSet.add(I18N_ERROR);

        String[] totalBaseNames = baseNamesSet.toArray(new String[0]);

        if (logger.isInfoEnabled()) {
            logger.info("加载错误码国际化资源：{}", StringUtils.join(totalBaseNames, ","));
        }
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setBasenames(totalBaseNames);
        return bundleMessageSource;
    }

    public ThreadPoolTaskExecutor getTaskExecutor(ServicePoolProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        int corePoolSize = properties.getCorePoolSize();
        if (corePoolSize == ServicePoolProperties.DEFAULT_MATCH_IF_MISSING) {
        	executor.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
        } else {
        	executor.setCorePoolSize(corePoolSize);
        }
        //配置最大线程数
        int maxPoolSize = properties.getMaxPoolSize();
        if (maxPoolSize == ServicePoolProperties.DEFAULT_MATCH_IF_MISSING) {
        	executor.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
        } else {
            executor.setMaxPoolSize(maxPoolSize);
        }
        //配置队列大小
        int queueCapacity = properties.getQueueCapacity();
        if (queueCapacity == ServicePoolProperties.DEFAULT_MATCH_IF_MISSING) {
        	executor.setQueueCapacity(DEFAULT_QUENE_CAPACITY);
        } else {
            executor.setQueueCapacity(queueCapacity);
        }
        //配置线程池维护线程所允许的空闲时间，默认为3*60s
        int keepAliveSeconds = properties.getKeepAliveSeconds();
        if (keepAliveSeconds == ServicePoolProperties.DEFAULT_MATCH_IF_MISSING) {
        	executor.setKeepAliveSeconds(DEFAULT_KEEP_ALIVE_SECONDS);
        } else {
            executor.setKeepAliveSeconds(keepAliveSeconds);
        }
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(ROUTER_THREAD_NAME_PREFIX);
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
