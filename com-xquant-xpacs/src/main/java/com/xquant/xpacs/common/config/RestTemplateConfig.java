package com.xquant.xpacs.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Rest内部服务调用Bean初始化
 * @author deming.ye
 *
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	@LoadBalanced
	public RestTemplate createTemplate() {
		/*HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);
        httpRequestFactory.setReadTimeout(50000);*/

        return new RestTemplate(httpRequestFactory());
	}

	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	@Bean
	public HttpClient httpClient() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		//设置整个连接池最大连接数 根据自己的场景决定
		connectionManager.setMaxTotal(100);
		//路由是对maxTotal的细分
		connectionManager.setDefaultMaxPerRoute(100);
		RequestConfig requestConfig = RequestConfig.custom()
				//服务器返回数据(response)的时间，超过该时间抛出read timeout
				.setSocketTimeout(5*60000)
				//连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
				.setConnectTimeout(10000)
				//从连接池中获取连接的超时时间，超过该时间未拿到可用连接，
				//会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
				.setConnectionRequestTimeout(1000)
				.build();
		return HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connectionManager)
				.build();
	}
}
