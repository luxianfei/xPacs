/**
 *******************************************
 * 文件名称: IncomeTrendServiceImpl.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 收益走势
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月9日 下午2:50:15
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.investAnalysis.entity.dto.IncomeTrendParamDTO;
import com.xquant.xpacs.investAnalysis.service.api.IIncomeTrendService;

/**
 * @ClassName: IncomeTrendServiceImpl
 * @Description: 收益走势
 * @author: jingru.jiang
 * @date: 2020年11月9日 下午2:50:15
 *
 */
@Service
public class IncomeTrendServiceImpl implements IIncomeTrendService{
	private static final Logger LOGGER = LoggerFactory.getLogger(IncomeTrendServiceImpl.class);
    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;

    @Async("analysisAsyncExecutor")
    @Override
    public Future<List<Map<String,Object>>> calcIncomeTrend(IncomeTrendParamDTO incomeTrendParamDTO) {
    	// 指标计算
        AnalysisIndexCalcResultBO indexCalcResultBO = analysisIndexCalcService.calc("incomeTrend", incomeTrendParamDTO);

        List<Map<String,Object>> resultList = new ArrayList<>();
        if(indexCalcResultBO.getSuccessful()) {
            resultList = indexCalcResultBO.getResultList();
        }

        return new AsyncResult<>(resultList);
    }
}
