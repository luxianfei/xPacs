/**
 * ******************************************
 * 文件名称: AnalysisResponseInterceptor.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月20日 09:53:09
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.support;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName: AnalysisResponseInterceptor
 * @Description: 分析结果拦截器
 * @author: yt.zhou
 * @date: 2020年10月20日 09:53:09
 */
@Component
public class AnalysisResponseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class clazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            if(method.isAnnotationPresent(AnalysisResponse.class)) {
                //方法体有注解

            } else if(clazz.isAnnotationPresent(AnalysisResponse.class)) {
                //类有注解
            }
        }
        return false;
    }
}
