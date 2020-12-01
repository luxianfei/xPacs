/**
 * ******************************************
 * 文件名称: IAnalysisIndexAsyncCalcService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月12日 14:14:36
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.impl;

import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @ClassName: IAnalysisIndexAsyncCalcService
 * @Description: 异步计算
 * @author: yt.zhou
 * @date: 2020年11月12日 14:14:36
 */
@Component
public class AnalysisIndexAsyncCalcServiceImpl implements IAnalysisIndexAsyncCalcService {

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;


    @Async("analysisAsyncExecutor")
    @Override
    public Future<AnalysisIndexCalcResultBO> asyncCalc(String moduleNo, AnalysisBaseParamDTO calcBaseParam) {
        AnalysisIndexCalcResultBO indexCalcResultBO = analysisIndexCalcService.calc(moduleNo,calcBaseParam);
        return new AsyncResult<>(indexCalcResultBO);
    }
}
