/**
 * ******************************************
 * 文件名称: IEmphasisAssetCompareService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月27日 17:12:44
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.api;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @ClassName: IEmphasisAssetCompareService
 * @Description: 重点资产对比
 * @author: yt.zhou
 * @date: 2020年10月27日 17:12:44
 */
public interface IEmphasisAssetCompareAsyncService {

    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 17:15
     * @description: 前十大股票结果数据
     * @param calcBaseParam
     * @return java.util.concurrent.Future<com.xquant.xpims.analysis.entity.bo.AnalysisIndexCalcResultBO>
     *
     */
    Future<List<Map<String,Object>>> topTenStockCompare(AnalysisBaseParamDTO calcBaseParam);

    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 17:26
     * @description: 前十大重仓债券对比
     * @param calcBaseParam
     * @return java.util.concurrent.Future<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     *
     */
    Future<List<Map<String,Object>>> topTenBndCompare(AnalysisBaseParamDTO calcBaseParam);

    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 17:26
     * @description: 评级对比
     * @param calcBaseParam
     * @return java.util.concurrent.Future<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     *
     */
    Future<List<Map<String,Object>>> gradeCompare(AnalysisBaseParamDTO calcBaseParam);
}
