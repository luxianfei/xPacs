/**
 *******************************************
 * 文件名称: RiskIndexController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 绩效指标
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月10日 下午5:44:45
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
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.investAnalysis.entity.dto.RiskIndexParamDTO;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: RiskIndexController
 * @Description: 绩效指标
 * @author: jingru.jiang
 * @date: 2020年11月10日 下午5:44:45
 *
 */
@RestController
@RequestMapping("/riskIndex")
public class RiskIndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RiskIndexController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IAnalysisIndexAsyncCalcService analysisIndexAsyncCalcService;
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private ITprtService tprtService;
    
    /**
     * @Title: calcRiskIndex
     * @Description: 绩效指标计算
     * @param: riskIndexParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcRiskIndex", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcRiskIndex(RiskIndexParamDTO riskIndexParamDTO) {
    	String moduleNo = "riskIndexXpacs";
    	AnalysisBaseParamDTO riskIndexDepoParam = new AnalysisBaseParamDTO();
        BeanUtils.copyProperties(riskIndexParamDTO, riskIndexDepoParam);
    	AnalysisIndexCalcResultBO resultBO = new AnalysisIndexCalcResultBO();
    	
    	String planCode = riskIndexParamDTO.getPlanCode();
        // 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 获取计划下所有组合
        	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
            for(Tprt tprt : portList) {
                portCodes.add(tprt.getPortCode());
            }
            riskIndexParamDTO.setPortCode(portCodes);
            riskIndexParamDTO.setPortType(PortType.port.getCode());
            riskIndexParamDTO.setPortCalcType(PortCalcType.single.name());
            // 指标计算
        	Future<AnalysisIndexCalcResultBO> future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo, riskIndexParamDTO);
        	
        	// 计算活期存款 -- 分组层面,只能复合
        	List<String> planCodes = new ArrayList<>();
        	planCodes.add(planCode);
        	riskIndexDepoParam.setPortCode(planCodes);
        	riskIndexDepoParam.setPortType(PortType.plan.getCode());
        	riskIndexDepoParam.setPortCalcType(PortCalcType.single.name());
            // 指标计算
            Future<AnalysisIndexCalcResultBO> future2 = analysisIndexAsyncCalcService.asyncCalc("riskIndexDepoXpacs", riskIndexDepoParam);
            
			try {
				resultBO = future1.get();
				AnalysisIndexCalcResultBO calcResult2 = future2.get();
				
				// 合并数据--活期存款
				Object obj = null;
				for (Map<String,Object> map : calcResult2.getResultList()) {
					obj = map.get("gbHldWAvgMtmRootDeposit");
				}
				for (Map<String,Object> map : resultBO.getResultList()) {
					if (map.get("portCode").equals(planCode)) {
						map.put("gbHldWAvgMtmRootDeposit",obj);
					}
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
            
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	riskIndexParamDTO.setPortCode(portCodes);
        	riskIndexParamDTO.setPortType(PortType.plan.getCode());
        	riskIndexParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        	// 指标计算
        	resultBO = analysisIndexCalcService.calc(moduleNo, riskIndexParamDTO);
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());

        return baseResponse;
    }
}
