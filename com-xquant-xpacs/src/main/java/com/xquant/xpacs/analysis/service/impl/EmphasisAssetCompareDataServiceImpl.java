/**
 * ******************************************
 * 文件名称: EmphasisAssetCompareDataServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月28日 15:52:41
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xquant.common.annotation.ServiceMethod;
import com.xquant.common.annotation.ServiceMethodBean;
import com.xquant.common.bean.GridPageResp;
import com.xquant.common.cache.CacheModelEnum;
import com.xquant.common.util.RedisTools;
import com.xquant.xpacs.analysis.entity.bo.AnalysisResponseCacheResultBO;
import com.xquant.xpacs.analysis.service.api.IEmphasisAssetCompareDataService;
import com.xquant.xpacs.common.format.model.AnalysisRow2CellDTO;
import com.xquant.xpacs.common.format.table.SimpleTableResultModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: EmphasisAssetCompareDataServiceImpl
 * @Description: 重点资产对比数据处理
 * @author: yt.zhou
 * @date: 2020年10月28日 15:52:41
 */
@ServiceMethodBean
public class EmphasisAssetCompareDataServiceImpl implements IEmphasisAssetCompareDataService {
    @Autowired
    private RedisTools redisTools;

    @ServiceMethod(method = "emphasisAssetCompareData")
    @Override
    public GridPageResp<SimpleTableResultModel> emphasisAssetCompareData(AnalysisRow2CellDTO row2CellDTO) {
        AnalysisResponseCacheResultBO cacheResultBO = redisTools.get(CacheModelEnum.XPIMS, row2CellDTO.getRequestId(), AnalysisResponseCacheResultBO.class);

        GridPageResp gridPageResp = ((JSONObject)cacheResultBO.getData()).toJavaObject(GridPageResp.class);
        gridPageResp.setResult(((JSONObject)gridPageResp.getResult()).toJavaObject(SimpleTableResultModel.class));

        return gridPageResp;
    }
}
