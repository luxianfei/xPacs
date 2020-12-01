/**
 *******************************************
 * 文件名称: IncomeTrendController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 收益情况
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 收益走势
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月9日 下午1:22:37
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
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.investAnalysis.entity.dto.IncomeTrendParamDTO;
import com.xquant.xpacs.investAnalysis.service.api.IIncomeTrendService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;

/**
 * @ClassName: IncomeTrendController
 * @Description: 收益走势
 * @author: jingru.jiang
 * @date: 2020年11月9日 下午1:22:37
 *
 */
@RestController
@RequestMapping("/incomeTrend")
public class IncomeTrendController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IncomeTrendController.class);

    @Autowired
    private IIncomeTrendService incomeTrendService;
    @Autowired
    private IPlanInfoService planInfoService;
    
    /**
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @Title: calcIncomeTrend
     * @Description: 收益走势指标计算
     * @param: incomeTrendParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcIncomeTrend", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcIncomeTrend(IncomeTrendParamDTO incomeTrendParamDTO) throws InterruptedException, ExecutionException {
    	List<Map<String,Object>> newDataList = new ArrayList<Map<String,Object>>();
    	List<Future<List<Map<String,Object>>>> futureList = new ArrayList<>();
    	
    	String planCode = incomeTrendParamDTO.getPlanCode();
        // 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 将计划代码加入到原有请求参数中
        	List<String> portCodes = incomeTrendParamDTO.getPortCode();
            portCodes.add(planCode);
            incomeTrendParamDTO.setPortCode(portCodes);
            incomeTrendParamDTO.setPortType(PortType.port.getCode());
            incomeTrendParamDTO.setPortCalcType(PortCalcType.single.name());
            // 指标计算
	        Future<List<Map<String,Object>>> portResult = incomeTrendService.calcIncomeTrend(incomeTrendParamDTO);
	        futureList.add(portResult);
        } else { // 计划复合计算-计划及其下组合
    		// 组合计算参数
    		incomeTrendParamDTO.setPortType(PortType.port.getCode());
    		incomeTrendParamDTO.setPortCalcType(PortCalcType.single.name());
    		// 指标计算
    		Future<List<Map<String,Object>>> portResult = incomeTrendService.calcIncomeTrend(incomeTrendParamDTO);
    		futureList.add(portResult);
    		
    		// 计划计算参数
    		if (planCode != null && !planCode.isEmpty()) {
    			IncomeTrendParamDTO planIncomeTrendParamDTO = new IncomeTrendParamDTO();
    			BeanUtils.copyProperties(incomeTrendParamDTO, planIncomeTrendParamDTO);
    			List<String> portCodes = new ArrayList<>();
    			portCodes.add(planCode);
    			planIncomeTrendParamDTO.setPortCode(portCodes);
    			planIncomeTrendParamDTO.setBmCode("");
    			planIncomeTrendParamDTO.setPortType(PortType.plan.getCode());
    			planIncomeTrendParamDTO.setPortCalcType(PortCalcType.single.name());
    			// 指标计算
    			Future<List<Map<String,Object>>> planResult = incomeTrendService.calcIncomeTrend(planIncomeTrendParamDTO);
    			futureList.add(planResult);
    		}
        }
        
        for(Future<List<Map<String,Object>>> future : futureList) {
        	List<Map<String,Object>> result = future.get();
        	newDataList.addAll(result);
        }
        
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getSuccessResponse(newDataList);

        return baseResponse;
    }

}
