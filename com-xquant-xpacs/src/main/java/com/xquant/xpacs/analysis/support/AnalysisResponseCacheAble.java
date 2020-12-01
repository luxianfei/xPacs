/**
 * ******************************************
 * 文件名称: AnalysisResponseCache.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月28日 11:26:13
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.support;

import java.lang.annotation.*;

/**
 * @ClassName: AnalysisResponseCache
 * @Description: 结果数据缓存
 * @author: yt.zhou
 * @date: 2020年10月28日 11:26:13
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnalysisResponseCacheAble {
}
