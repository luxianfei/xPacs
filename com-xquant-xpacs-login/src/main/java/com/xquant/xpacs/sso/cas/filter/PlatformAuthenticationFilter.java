package com.xquant.xpacs.sso.cas.filter;


import com.alibaba.fastjson.JSON;
import com.xquant.common.bean.CommonResp;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.authentication.*;
import org.jasig.cas.client.configuration.ConfigurationKey;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class PlatformAuthenticationFilter extends AbstractCasFilter {
    /**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;
    
    private boolean ssoEenabled = false;

    private String ssoServerName;

    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    private AuthenticationRedirectStrategy authenticationRedirectStrategy = new DefaultAuthenticationRedirectStrategy();

    private UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategyClass = null;

    private static final Map<String, Class<? extends UrlPatternMatcherStrategy>> PATTERN_MATCHER_TYPES =
            new HashMap<String, Class<? extends UrlPatternMatcherStrategy>>();

    static {
        PATTERN_MATCHER_TYPES.put("CONTAINS", ContainsPatternUrlPatternMatcherStrategy.class);
        PATTERN_MATCHER_TYPES.put("REGEX", RegexUrlPatternMatcherStrategy.class);
        PATTERN_MATCHER_TYPES.put("EXACT", ExactUrlPatternMatcherStrategy.class);
    }

    public PlatformAuthenticationFilter() {
        this(Protocol.CAS2);
    }

    protected PlatformAuthenticationFilter(final Protocol protocol) {
        super(protocol);
    }

    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        //拦截器获取参数
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setSsoEenabled(getBoolean(new ConfigurationKey<Boolean>("ssoEenabled", Boolean.FALSE)));
            setSsoServerName(getString(ConfigurationKeys.SERVER_NAME));
            setCasServerLoginUrl(getString(ConfigurationKeys.CAS_SERVER_LOGIN_URL));
            setRenew(getBoolean(ConfigurationKeys.RENEW));
            setGateway(getBoolean(ConfigurationKeys.GATEWAY));

            final String ignorePattern = getString(ConfigurationKeys.IGNORE_PATTERN);
            final String ignoreUrlPatternType = getString(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE);

            if (ignorePattern != null) {
                final Class<? extends UrlPatternMatcherStrategy> ignoreUrlMatcherClass = PATTERN_MATCHER_TYPES.get(ignoreUrlPatternType);
                if (ignoreUrlMatcherClass != null) {
                    this.ignoreUrlPatternMatcherStrategyClass = ReflectUtils.newInstance(ignoreUrlMatcherClass.getName());
                } else {
                    try {
                        logger.trace("Assuming {} is a qualified class name...", ignoreUrlPatternType);
                        this.ignoreUrlPatternMatcherStrategyClass = ReflectUtils.newInstance(ignoreUrlPatternType);
                    } catch (final IllegalArgumentException e) {
                        logger.error("Could not instantiate class [{}]", ignoreUrlPatternType, e);
                    }
                }
                if (this.ignoreUrlPatternMatcherStrategyClass != null) {
                    this.ignoreUrlPatternMatcherStrategyClass.setPattern(ignorePattern);
                }
            }

            final Class<? extends GatewayResolver> gatewayStorageClass = getClass(ConfigurationKeys.GATEWAY_STORAGE_CLASS);

            if (gatewayStorageClass != null) {
                setGatewayStorage(ReflectUtils.newInstance(gatewayStorageClass));
            }

            final Class<? extends AuthenticationRedirectStrategy> authenticationRedirectStrategyClass = getClass(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS);

            if (authenticationRedirectStrategyClass != null) {
                this.authenticationRedirectStrategy = ReflectUtils.newInstance(authenticationRedirectStrategyClass);
            }
        }
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                               final FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession(true);
        if (!ssoEenabled){
            filterChain.doFilter(request, response);
            return;
        }
        if (isRequestUrlExcluded(request)) {
            logger.debug("Request is ignored.");
            filterChain.doFilter(request, response);
            return;
        }

        final Assertion assertion = (Assertion) session.getAttribute(CONST_CAS_ASSERTION);

        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = retrieveTicketFromRequest(request);
        final boolean wasGatewayed = this.gateway && this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        if ((CommonUtils.isNotBlank(ticket) || wasGatewayed)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String modifiedServiceUrl;

        logger.debug("no ticket and no assertion found");
        if (this.gateway) {
            logger.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        logger.debug("Constructed service url: {}", modifiedServiceUrl);

        /**
         * 由于前后端分离，采用页面重定向，指定到SSO统一跳转页面
         */
//        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl,
//                getProtocol().getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);
        final String urlToRedirectTo = this.constructSSORedirectUrl();

        logger.debug("redirecting to \"{}\"", urlToRedirectTo);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=UTF-8");
        //以上情况均没有被放行,登录无效的情况
        out.println(JSON.toJSONString(new CommonResp("402", "ssoLogin", urlToRedirectTo)));
        //this.authenticationRedirectStrategy.redirect(request, response, urlToRedirectTo);
    }
    
    private String constructSSORedirectUrl() {
    	return casServerLoginUrl + "?service=" + ssoServerName + "?ssoLogin=";
    }

    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }

    public void setSsoEenabled(boolean ssoEenabled) {
		this.ssoEenabled = ssoEenabled;
	}

	public void setSsoServerName(String serverName) {
		this.ssoServerName = serverName;
	}

	private boolean isRequestUrlExcluded(final HttpServletRequest request) {
        if (this.ignoreUrlPatternMatcherStrategyClass == null) {
            return false;
        }

        final StringBuffer urlBuffer = request.getRequestURL();
        if (request.getQueryString() != null) {
            urlBuffer.append("?").append(request.getQueryString());
        }
        final String requestUri = urlBuffer.toString();
        return this.ignoreUrlPatternMatcherStrategyClass.matches(requestUri);
    }

}
