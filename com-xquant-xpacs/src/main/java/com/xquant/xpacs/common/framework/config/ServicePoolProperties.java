package com.xquant.xpacs.common.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "index.module.service-pool")
public class ServicePoolProperties {
	public final static int DEFAULT_MATCH_IF_MISSING = -999;

	private int corePoolSize = DEFAULT_MATCH_IF_MISSING;
	private int maxPoolSize = DEFAULT_MATCH_IF_MISSING;
	private int queueCapacity = DEFAULT_MATCH_IF_MISSING;
	private int keepAliveSeconds = DEFAULT_MATCH_IF_MISSING;

	public int getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public int getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public int getQueueCapacity() {
		return queueCapacity;
	}
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

}
