package com.xquant.xpacs.common.framework.request;

import com.xquant.xpacs.common.framework.api.IServiceContext;
import com.xquant.xpacs.common.framework.config.ServiceParameterNames;
import com.xquant.xpacs.common.framework.impl.ServiceMethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
 * @ClassName: DefaultRequestContextBuilder
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: deming.ye
 * @date: 2018年7月27日 上午9:18:00
 *
 */
public class DefaultRequestContextBuilder implements IRequestContextBuilder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

//    public static final String X_REAL_IP = "X-Real-IP";
//
//    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private FormattingConversionService conversionService;

    private Validator validator;

    public DefaultRequestContextBuilder(FormattingConversionService conversionService) {
        this.conversionService = conversionService;
    }

	@Override
	public IRequestContext buildRequestContext(IServiceContext context,
			HttpServletRequest request, HttpServletResponse response) {
		DefaultRequestContext requestContext = new DefaultRequestContext(context);

        //设置请求对象及参数列表
        requestContext.setRawRequest(request);
        if (response != null) {
            requestContext.setRawResponse(response);
        }
        HashMap<String, Object> allParams = getRequestParams(request);
        // 设置登录用户ID
        if (getUserId(request) != null) {
        	allParams.put(ServiceParameterNames.getUserId(), getUserId(request));
        }
        requestContext.setAllParams(allParams);

        //设置服务的系统级参数
        requestContext.setAppKey(request.getParameter(ServiceParameterNames.getAppKey()));
        requestContext.setMethod(request.getParameter(ServiceParameterNames.getMethod()));
        String version = request.getParameter(ServiceParameterNames.getVersion());
        requestContext.setVersion(version == null ? "1.0" : version);

        //设置服务处理器
        ServiceMethodHandler serviceMethodHandler =
        		context.getServiceMethodHandler(requestContext.getMethod(), requestContext.getVersion());
        requestContext.setServiceMethodHandler(serviceMethodHandler);

        return requestContext;
	}

	private Long getUserId(HttpServletRequest request) {
		/*Object loginUser = request.getSession().getAttribute(UserConstant.SESSION_USER);
		if (loginUser == null) {
			return null;
		}
		return ((LoginUserBO)loginUser).getUserId();*/
		return 902L;
	}

	@Override
	public IRequest buildRequest(IRequestContext requestContext) {
//		AbstractRequest request = null;
		AbstractRequest request = new DefaultRequest();
        if (requestContext.getServiceMethodHandler().isRequestImplType()) {
        	Map<String, Object> requestMap = requestContext.getAllParams();
            BindingResult bindingResult = doBind(requestMap, requestContext.getServiceMethodHandler().getRequestType());
//            request = buildRequestFromBindingResult(requestContext, bindingResult);

            List<ObjectError> allErrors = bindingResult.getAllErrors();

            requestContext.setRequest(bindingResult.getTarget());
            requestContext.setAttribute(DefaultRequestContext.SPRING_VALIDATE_ERROR_ATTRNAME, allErrors);
        } else {
            request = new DefaultRequest();
        }
        request.setRequestContext(requestContext);
        return request;
	}

    private BindingResult doBind(Map<String, Object> requestMap, Class<? extends Object> requestType) {
    	PropertyValues pvs = new MutablePropertyValues(requestMap);
    	Object bindObject = BeanUtils.instantiateClass(requestType);
        DataBinder dataBinder = new DataBinder(bindObject, "bindObject");
        // 数据绑定前先做转换
        dataBinder.setConversionService(getFormattingConversionService());
        dataBinder.setValidator(getValidator());
        dataBinder.bind(pvs);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

    private Validator getValidator() {
        if (this.validator == null) {
            LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
            localValidatorFactoryBean.afterPropertiesSet();
            this.validator = localValidatorFactoryBean;
        }
        return this.validator;
    }


	public HashMap<String, Object> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> srcParamMap = request.getParameterMap();
        HashMap<String, Object> destParamMap = new HashMap<String, Object>(srcParamMap.size());
        for (String key : srcParamMap.keySet()) {
            String[] values = srcParamMap.get(key);
            if (values != null && values.length > 0) {
                destParamMap.put(key, values);
//                destParamMap.put(key, values[0]);
            } else {
                destParamMap.put(key, null);
            }
        }
        return destParamMap;
    }

    public FormattingConversionService getFormattingConversionService() {
        return conversionService;
    }

}
