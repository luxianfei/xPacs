package com.xquant.xpacs.common.remoting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IndexClient {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public final static String CALC_MODULE_URL = "/module/router";
	public final static String GET_CACHE_RESPONSE_URL = "/module/cacheResponse";
	public final static String PUT_LIST_CACHE_URL = "/module/putList2Cache";
	public final static String MODULE_NO = "moduleNo";

	@Value("${engine.index.remoting.enabled:false}")
	private boolean enabled;
	//引擎服务地址
	@Value("${engine.index.remoting.service-url:http://INDEX-SERVICE-DEV/index}")
    private String serviceUrl;
    //应用KEY
    private String appKey;
	@Autowired
	private RestTemplate restTemplate;

	public List<Map<String,Object>> getCacheReponse(String moduleRquestId, long start, long end) {
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("moduleRquestId", moduleRquestId);
		requestParams.put("start", start);
		requestParams.put("end", end);
		String responseContext = post(serviceUrl + GET_CACHE_RESPONSE_URL, requestParams);
		List<Map<String,Object>> response = (List<Map<String,Object>>) JSONArray.parse(responseContext);
		return response;
	}

	public boolean putList2Cache(String moduleRequestId, List<Map<String, Object>> response) {
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("moduleRequestId", moduleRequestId);
		requestParams.put("response", JSON.toJSONString(response));
		post(serviceUrl + PUT_LIST_CACHE_URL, requestParams);
		return true;
	}

	public String calcModule(HttpServletRequest request) {
		Map<String, String[]> form = new HashMap<>(request.getParameterMap());
		Long userId = getUserId(request);
		if (userId != null) {
			String[] values = {userId.toString()};
			form.put("userId", values);
		}
		return get(buildGetUrl(serviceUrl + CALC_MODULE_URL, form));
	}

	public String calcModule(String moduleNo, Map<String, String[]> parameters) {
		Map<String, String[]> form = new HashMap<>();
		if (parameters != null) {
			form.putAll(parameters);
		}
		form.put(MODULE_NO, new String[]{moduleNo});
		return get(buildGetUrl(serviceUrl + CALC_MODULE_URL, form));
	}

	private Long getUserId(HttpServletRequest request) {
		/*Object loginUserObj = request.getSession().getAttribute(UserConstant.SESSION_USER);
		if (loginUserObj != null) {
			LoginUserBO loginUser = (LoginUserBO) loginUserObj;
			return loginUser.getUserId();
		}*/
		return 902L;
	}

	public String calcModule(HttpMethod method, String requestBody) {
		return post(serviceUrl + CALC_MODULE_URL, method, requestBody);
	}

    private String post(String url, Map<String, Object> requestParams) {
    	String responseContext = restTemplate.postForObject(url,  toMultiValueMap(requestParams), String.class);
        if (logger.isDebugEnabled()) {
            logger.debug("response:\n" + responseContext);
        }
        return responseContext;
    }

    private String post(String url, HttpMethod method, String requestBody) {
    	ResponseEntity<String> responseEntity =
    	        restTemplate.exchange(url, method, new HttpEntity<String>(requestBody), String.class);
//    	String responseContext = restTemplate.postForObject(url,  new HttpEntity<String>(requestBody), String.class);
    	String responseContext = responseEntity.getBody();
    	if (logger.isDebugEnabled()) {
            logger.debug("response:\n" + responseContext);
        }
        return responseContext;
    }

    private String post(String url, HttpServletRequest request) {
        return post(url, getRequestParams(request));
    }

    private String get(String url) {
    	String responseContent = null;
    	try {
			URI uri = new URI(url);
			responseContent = restTemplate.getForObject(uri, String.class);
			if (logger.isDebugEnabled()) {
				logger.debug("response:\n" + responseContent);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	return responseContent;
    }

    private String buildGetUrl(String serverUrl, Map<String, String[]> form) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(serverUrl);
        requestUrl.append("?");
        String joinChar = "";
        for (Map.Entry<String, String[]> entry : form.entrySet()) {
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
            	for (String value : values) {
            		requestUrl.append(joinChar);
                    requestUrl.append(entry.getKey());
                    requestUrl.append("=");
                    requestUrl.append(value);
                    joinChar = "&";
            	}
            }
        }
        return requestUrl.toString();
    }

    private HashMap<String, Object> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> srcParamMap = request.getParameterMap();
        HashMap<String, Object> destParamMap = new HashMap<String, Object>(srcParamMap.size());
        for (String key : srcParamMap.keySet()) {
            String[] values = srcParamMap.get(key);
            // 组合和基准数据
            if (values != null && values.length > 0) {
            	destParamMap.put(key, values);
            } else {
                destParamMap.put(key, null);
            }
        }
        return destParamMap;
    }

	private MultiValueMap<String, Object> toMultiValueMap(Map<String, Object> form) {
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
		for (Map.Entry<String, Object> entry : form.entrySet()) {
			mvm.add(entry.getKey(), entry.getValue());
		}
		return mvm;
	}

}
