/**
 *******************************************
 * 文件名称: IncomeStatisticsController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 收益统计
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月10日 下午6:26:47
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: IncomeStatisticsController
 * @Description: 收益统计
 * @author: jingru.jiang
 * @date: 2020年11月10日 下午6:26:47
 *
 */
 @RestController
 @RequestMapping("/incomeStatistics")
public class IncomeStatisticsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IncomeStatisticsController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private ITprtService tprtService;
    
    /**
     * @Title: calcIncomeStatistics
     * @Description: 收益统计计算--作废
     * @param: incomeStatisticsParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcIncomeStatistics", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcIncomeStatistics(AnalysisBaseParamDTO incomeStatisticsParamDTO) {
        String planCode = incomeStatisticsParamDTO.getPlanCode();
        // 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 获取计划下所有组合
        	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
        	List<String> portCodes = new ArrayList<>();
            for(Tprt tprt : portList) {
                portCodes.add(tprt.getPortCode());
            }
            portCodes.add(planCode);
            incomeStatisticsParamDTO.setPortCode(portCodes);
            incomeStatisticsParamDTO.setPortType(PortType.port.getCode());
            incomeStatisticsParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	incomeStatisticsParamDTO.setPortCode(portCodes);
        	incomeStatisticsParamDTO.setPortType(PortType.plan.getCode());
        	incomeStatisticsParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("incomeStatisticsXpacs", incomeStatisticsParamDTO);

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());

        return baseResponse;
    }
    
}
