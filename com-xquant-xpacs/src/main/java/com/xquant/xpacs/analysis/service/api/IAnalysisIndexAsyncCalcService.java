/**
 * ******************************************
 * 文件名称: IAnalysisIndexCalcAsyncService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月12日 14:12:33
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.api;

import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

import java.util.concurrent.Future;

/**
 * @ClassName: IAnalysisIndexCalcAsyncService
 * @Description: 异步计算
 * @author: yt.zhou
 * @date: 2020年11月12日 14:12:33
 */
public interface IAnalysisIndexAsyncCalcService {

    /**
     * @author: yt.zhou
     * @date: 2020年11月12日 14:13
     * @description:    异步指标计算
     * @param moduleNo  指标方案代码
     * @param analysisBaseParamDTO 计算参数
     * @return com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO
     *
     */
    Future<AnalysisIndexCalcResultBO> asyncCalc(String moduleNo, AnalysisBaseParamDTO analysisBaseParamDTO);
}
