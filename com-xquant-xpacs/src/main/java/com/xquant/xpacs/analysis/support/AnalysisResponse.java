/**
 * ******************************************
 * 文件名称: AnalysisResponse.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月20日 09:55:54
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.support;

import com.xquant.xpacs.analysis.enums.EnumWebComponent;

import java.lang.annotation.*;

/**
 * @ClassName: AnalysisResponse
 * @Description: 分析功能结果集封装
 * @author: yt.zhou
 * @date: 2020年10月20日 09:55:54
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnalysisResponse {

    /**组件类型*/
    EnumWebComponent webComponent() default EnumWebComponent.TABLE;

    /**是否分析功能的数据，分析功能的数据，需要对数据做二次加工*/
    boolean isAnalysis() default true;
}
