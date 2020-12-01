/**
 *******************************************
 * 文件名称: TrendComparisonController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 走势对比
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月25日 下午8:23:51
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.inforQuery.controller.CashFlowQueryController;
import com.xquant.xpacs.investAnalysis.entity.dto.TrendComparisonParamDTO;

/**
 * @ClassName: TrendComparisonController
 * @Description: 走势对比
 * @author: jingru.jiang
 * @date: 2020年11月25日 下午8:23:51
 *
 */
@RestController
@RequestMapping("/trendComparison")
public class TrendComparisonController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashFlowQueryController.class);
	
	@Autowired
    private IAnalysisIndexAsyncCalcService analysisIndexAsyncCalcService;
	
	/**
	 * @Title: calcAssetIncomeTrend
	 * @Description: 计算资产走势对比
	 * @param: trendComparisonParamDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	@AnalysisResponseCacheAble
    @RequestMapping(value = "/calcAssetIncomeTrend", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcAssetIncomeTrend(TrendComparisonParamDTO trendComparisonParamDTO) {
    	String planCode = trendComparisonParamDTO.getPlanCode();
    	boolean calcPlan = trendComparisonParamDTO.isCalcPlan();

        List<Future<AnalysisIndexCalcResultBO>> futureList = new ArrayList<>();
        
        // 计算组合
    	if (trendComparisonParamDTO.getPortCode() != null && !trendComparisonParamDTO.getPortCode().isEmpty()) {
    		trendComparisonParamDTO.setPortType(PortType.port.getCode());
        	trendComparisonParamDTO.setPortCalcType(PortCalcType.single.name());
        	// 指标计算
        	Future<AnalysisIndexCalcResultBO> future = analysisIndexAsyncCalcService.asyncCalc("incomeTrendComp", trendComparisonParamDTO);
        	futureList.add(future);
    	}
    	
    	// 计算计划
    	TrendComparisonParamDTO trendComparisonPlanParamDTO = new TrendComparisonParamDTO();
    	BeanUtils.copyProperties(trendComparisonParamDTO, trendComparisonPlanParamDTO);
    	if (calcPlan) {
    		List<String> portCodeList = new ArrayList<String>();
    		portCodeList.add(planCode);
    		trendComparisonPlanParamDTO.setPortCode(portCodeList);
    		trendComparisonPlanParamDTO.setPortType(PortType.plan.getCode());
    		trendComparisonPlanParamDTO.setPortCalcType(PortCalcType.single.name());
    		// 指标计算
    		Future<AnalysisIndexCalcResultBO> future = analysisIndexAsyncCalcService.asyncCalc("incomeTrendComp", trendComparisonPlanParamDTO);
    		futureList.add(future);
    	}

        List<Map<String, Object>> newResultList = new ArrayList<Map<String,Object>>();
        try {
        	for(Future<AnalysisIndexCalcResultBO> future : futureList) {
        		AnalysisIndexCalcResultBO resultBO = future.get();
        		if (resultBO.getSuccessful()) {
        			newResultList.addAll(resultBO.getResultList());
        		}
        	}
        } catch (InterruptedException e) {
        	throw new RuntimeException(e);
        } catch (ExecutionException e) {
        	throw new RuntimeException(e);
        }
        
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getSuccessResponse(newResultList);
    	return baseResponse;
    }
	
}
