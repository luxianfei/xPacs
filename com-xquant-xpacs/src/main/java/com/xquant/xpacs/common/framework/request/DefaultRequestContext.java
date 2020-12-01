/**
 *******************************************
 * 文件名称: DefaultIndexRequestContext.java
 * 系统名称: xALMS资产负债管理与量化分析系统
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0.1
 * @author: deming.ye
 * 开发时间: 2018年7月27日 下午3:16:17
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.common.framework.request;

import com.xquant.xpacs.common.framework.api.IServiceContext;
import com.xquant.xpacs.common.framework.error.MainError;
import com.xquant.xpacs.common.framework.impl.ServiceMethodDefinition;
import com.xquant.xpacs.common.framework.impl.ServiceMethodHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

 /**
 * @ClassName: DefaultIndexRequestContext
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: deming.ye
 * @date: 2018年7月27日 下午3:16:17
 *
 */
public class DefaultRequestContext implements IRequestContext  {

	public static final String SPRING_VALIDATE_ERROR_ATTRNAME = "$SPRING_VALIDATE_ERROR_ATTRNAME";

    private long serviceBeginTime = -1;

    private long serviceEndTime = -1;

	private IServiceContext context;

	private Object rawRequest;

	private Object rawResponse;

	private String requestId = UUID.randomUUID().toString().toUpperCase();

    private String appKey;

	private String method;

    private String version;

    private Map<String, Object> allParams;

    private ServiceMethodHandler serviceMethodHandler;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    private MainError mainError;

    private Object request;

    private Object response;

    public DefaultRequestContext(IServiceContext context) {
        this.context = context;
        this.serviceBeginTime = System.currentTimeMillis();
        this.requestId = StringUtils.replace(UUID.randomUUID().toString().toUpperCase(), "-", "");
    }

    public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
    @Override
	public String getRequestId(){
    	return this.requestId;
    }

	/**
	 * @return context
	 */
    @Override
	public IServiceContext getContext() {
		return context;
	}

	/**
	 * @param context 要设置的 context
	 */
	public void setContext(IServiceContext context) {
		this.context = context;
	}

	/**
	 * @return appKey
	 */
	@Override
	public String getAppKey() {
		return appKey;
	}

	/**
	 * @param appKey 要设置的 appKey
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * @return method
	 */
	@Override
	public String getMethod() {
		return method;
	}

	/**
	 * @param method 要设置的 method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return version
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * @param version 要设置的 version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return allParams
	 */
	@Override
	public Map<String, Object> getAllParams() {
		return allParams;
	}

	/**
	 * @param allParams 要设置的 allParams
	 */
	public void setAllParams(Map<String, Object> allParams) {
		this.allParams = allParams;
	}
	@Override
	public Object getParamValue(String paramName) {
		if (allParams != null) {
            return allParams.get(paramName);
        } else {
            return null;
        }
	}

	/**
	 * @return request
	 */
	@Override
	public Object getRawRequest() {
		return rawRequest;
	}

	/**
	 * @param request 要设置的 request
	 */
	public void setRawRequest(Object rawRequest) {
		this.rawRequest = rawRequest;
	}

	/**
	 * @return response
	 */
	@Override
	public Object getRawResponse() {
		return rawResponse;
	}

	/**
	 * @param response 要设置的 response
	 */
	public void setRawResponse(Object rawResponse) {
		this.rawResponse = rawResponse;
	}

	/**
	 * @return serviceMethodHandler
	 */
	@Override
	public ServiceMethodHandler getServiceMethodHandler() {
		return serviceMethodHandler;
	}

	/**
	 * @param serviceMethodHandler 要设置的 serviceMethodHandler
	 */
	public void setServiceMethodHandler(ServiceMethodHandler serviceMethodHandler) {
		this.serviceMethodHandler = serviceMethodHandler;
	}

	/**
	 * @return serviceBeginTime
	 */
	@Override
	public long getServiceBeginTime() {
		return serviceBeginTime;
	}

	/**
	 * @param serviceBeginTime 要设置的 serviceBeginTime
	 */
	@Override
	public void setServiceBeginTime(long serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	/**
	 * @return serviceEndTime
	 */
	@Override
	public long getServiceEndTime() {
		return serviceEndTime;
	}

	/**
	 * @param serviceEndTime 要设置的 serviceEndTime
	 */
	@Override
	public void setServiceEndTime(long serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}
	@Override
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}
	@Override
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	/**
	 * @return mainError
	 */
	public MainError getMainError() {
		return mainError;
	}

	/**
	 * @param mainError 要设置的 mainError
	 */
	public void setMainError(MainError mainError) {
		this.mainError = mainError;
	}

	/**
	 * @return response
	 */
	@Override
	public Object getResponse() {
		return response;
	}

	/**
	 * @param response 要设置的 response
	 */
	@Override
	public void setResponse(Object response) {
		this.response = response;
	}
	@Override
	public ServiceMethodDefinition getServiceMethodDefinition() {
        return serviceMethodHandler.getServiceMethodDefinition();
    }
	@Override
	public Object getRequest() {
		return request;
	}
	@Override
	public void setRequest(Object request) {
		this.request = request;
	}
}
