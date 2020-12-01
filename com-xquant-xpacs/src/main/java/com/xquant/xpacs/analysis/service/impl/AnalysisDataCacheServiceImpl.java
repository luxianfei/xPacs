/**
 * ******************************************
 * 文件名称: AnalysisDataCacheServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月04日 15:11:24
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.impl;

import com.xquant.common.cache.CacheModelEnum;
import com.xquant.common.util.RedisTools;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AnalysisDataCacheServiceImpl
 * @Description: 缓存处理
 * @author: yt.zhou
 * @date: 2020年11月04日 15:11:24
 */
@Component
public class AnalysisDataCacheServiceImpl implements IAnalysisDataCacheService {
    @Autowired
    private RedisTools redisTools;

    @Override
    public void pushCache(String key, Object data) {
        redisTools.setObj(CacheModelEnum.XPACS, key, data, 2*60*60);
    }

    @Override
    public List<Map<String, Object>> getCache(String key) {
        return redisTools.getObj(CacheModelEnum.XPACS, key);
    }
}
