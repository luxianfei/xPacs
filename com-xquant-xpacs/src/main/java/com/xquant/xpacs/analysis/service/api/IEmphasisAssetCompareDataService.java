/**
 * ******************************************
 * 文件名称: IEmphasisAssetCompareDataService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月28日 15:51:28
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.api;

import com.xquant.common.bean.GridPageResp;
import com.xquant.xpacs.common.format.model.AnalysisRow2CellDTO;
import com.xquant.xpacs.common.format.table.SimpleTableResultModel;

/**
 * @ClassName: IEmphasisAssetCompareDataService
 * @Description: 重点资产数据处理
 * @author: yt.zhou
 * @date: 2020年10月28日 15:51:28
 */
public interface IEmphasisAssetCompareDataService {

    /**
     * @author: yt.zhou
     * @date: 2020年10月28日 14:44
     * @description:    重点资产对比，行转列导出
     * @param row2CellDTO
     * @return com.xquant.common.bean.GridPageResp<com.xquant.xpims.common.format.table.SimpleTableResultModel>
     *
     */
    GridPageResp<SimpleTableResultModel> emphasisAssetCompareData(AnalysisRow2CellDTO row2CellDTO);
}
