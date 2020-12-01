/**
 * ******************************************
 * 文件名称: IAnalysisDataCacheService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月04日 15:11:01
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.api;

import com.xquant.common.cache.CacheModelEnum;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IAnalysisDataCacheService
 * @Description: 缓存处理
 * @author: yt.zhou
 * @date: 2020年11月04日 15:11:01
 */
public interface IAnalysisDataCacheService {

    /**
     * @author: yt.zhou
     * @date: 2020年11月04日 15:13
     * @description: 缓存数据保存,默认失效时间2小时
     * @param key    缓存key
     * @param data   缓存数据
     *
     */
    void pushCache(String key, Object data);

    /**
     * @author: yt.zhou
     * @date: 2020年11月04日 15:13
     * @description: 缓存数据获取
     * @param key   缓存key
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    List<Map<String, Object>> getCache(String key);
}
