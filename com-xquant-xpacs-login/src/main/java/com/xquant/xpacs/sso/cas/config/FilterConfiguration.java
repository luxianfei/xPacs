package com.xquant.xpacs.sso.cas.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xquant.xpacs.sso.cas.filter.PlatformAuthenticationFilter;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FilterConfiguration {

    @Value("${sso.enabled:false}")
    private boolean ssoEenabled;
    @Value("${sso.cas.casServerLoginUrl:http://localhost:8080/cas}")
    private String casServerLoginUrl;
    @Value("${sso.cas.serverName:http://localhost:8080/PlatformSso}")
    private String serverName;
    /**
     * 登录过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", casServerLoginUrl);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }

    /**
     * 过滤验证器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //这里要用Cas20ProxyReceivingTicketValidationFilter这个过滤器，否则验证ticket的时候url会多出一个/p3，从而导致调不到casServer的服务
        registration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", casServerLoginUrl);
        initParameters.put("serverName", serverName);
        initParameters.put("useSession", "true");
        initParameters.put("redirectAfterValidation","false");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(3);
        return registration;
    }

    /**
     * 授权过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new PlatformAuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerLoginUrl", casServerLoginUrl+"/login");
        initParameters.put("serverName", serverName);
        initParameters.put("ssoEenabled", String.valueOf(ssoEenabled));
        initParameters.put("ignorePattern",
                "/marketActive/tellerlogout|/angular|/controls|/styles|/images|/js|/error|" +
                        "/health|/platFormManage/updatePlatFormManage|/logout|/base/login|/login/*");
        /**
         * <list>
					<value>/angular</value>
					<value>/controls</value>
					<value>/styles</value>
					<value>/images</value>
					<value>/js</value>
					<value>/error</value>
					<value>/health</value>
				</list>
         */
        initParameters.put("useSession", "true");
        //表示过滤所有
        initParameters.put("ignoreUrlPatternType", "com.xquant.xpacs.sso.cas.filter.SimpleUrlPatternMatcherStrategy");

        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * wraper过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        // 设定加载的顺序
        registration.setOrder(5);
        return registration;
    }

    /**
     * 添加监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration(){
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
