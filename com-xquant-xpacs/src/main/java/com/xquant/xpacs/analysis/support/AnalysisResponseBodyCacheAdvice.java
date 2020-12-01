/**
 * ******************************************
 * 文件名称: AnalysisResponseHandler.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月20日 10:02:47
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.support;

import com.xquant.common.cache.CacheModelEnum;
import com.xquant.common.util.RedisTools;
import com.xquant.common.util.UUIDUtil;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AnalysisResponseHandler
 * @Description: 分析功能结果集处理
 * @author: yt.zhou
 * @date: 2020年10月20日 10:02:47
 */
@ControllerAdvice
public class AnalysisResponseBodyCacheAdvice implements ResponseBodyAdvice<Object> {
    /**缓存key*/
    private static final String CACHE_KEY = "requestId";

    @Autowired
    private IAnalysisDataCacheService dataCacheService;
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if(returnType.getMethod().isAnnotationPresent(AnalysisResponseCacheAble.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if(body instanceof HttpBaseResponse) {
            HttpBaseResponse baseResponse = (HttpBaseResponse) body;
            if(baseResponse.isSuccess()) {
                //缓存数据Id
                String requestId = UUIDUtil.getUUID();
                dataCacheService.pushCache(requestId, baseResponse.getResult());

                Map<String, String> result = new HashMap<>();
                result.put(CACHE_KEY, requestId);

                HttpBaseResponse httpBaseResponse = HttpBaseResponseUtil.getSuccessResponse(result);
                return httpBaseResponse;
            } else {
                return baseResponse;
            }
        }

        return body;

    }
}
