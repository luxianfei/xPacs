/**
 *******************************************
 * 文件名称: TransationInfoController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 交易信息
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月18日 上午10:22:45
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.assetallocation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.assetallocation.entity.dto.TransationCompParamDTO;
import com.xquant.xpacs.assetallocation.entity.dto.TransationDetailParamDTO;
import com.xquant.xpacs.assetallocation.entity.dto.TransationOverviewParamDTO;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.investAnalysis.enums.CalcObjType;

/**
 * @ClassName: TransationInfoController
 * @Description: 交易信息
 * @author: jingru.jiang
 * @date: 2020年11月18日 上午10:22:45
 *
 */
@RestController
@RequestMapping("/transationInfo")
public class TransationInfoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransationInfoController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IAnalysisIndexAsyncCalcService analysisIndexAsyncCalcService;
    
    /**
     * @Title: calcTransationOverview
     * @Description: 计算大类资产交易概况
     * @param: transationDetailParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcTransationOverview", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcTransationOverview(TransationOverviewParamDTO transationOverviewParamDTO) {
    	String moduleNo1 = "transactionOverview";
    	
    	String calcObjType = transationOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		transationOverviewParamDTO.setPortType(PortType.port.getCode());
    		transationOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
    	} else {// 对象类型为计划
    		String planCode = transationOverviewParamDTO.getPlanCode();
    		transationOverviewParamDTO.addPortCode(planCode);
    		transationOverviewParamDTO.setPortType(PortType.plan.getCode());
    		transationOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
    	}
        // 指标计算
    	AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(moduleNo1, transationOverviewParamDTO);
        
        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());
    	return baseResponse;
    }
    
    /**
     * @Title: calcTransationComp
     * @Description: 计算大类资产交易比较
     * @param: transationCompParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcTransationComp", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcTransationComp(TransationCompParamDTO transationCompParamDTO) {
    	String moduleNo1 = "transactionComp";
    	
		transationCompParamDTO.setPortType(PortType.port.getCode());
		transationCompParamDTO.setPortCalcType(PortCalcType.single.name());
		// 指标计算
		AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(moduleNo1, transationCompParamDTO);
        
        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        if (resultBO.getSuccessful()) {
        	List<Map<String,Object>> resultList1 = resultBO.getResultList();
        	// 剔除无值的记录
        	for (Map<String,Object> map1 : resultList1) {
        		if (map1.get("gbHldAvgMtm") != null || map1.get("totalAmount") != null) {
        			newResultList.add(map1);
        		}
        	}
        }
        
        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }
    
    /**
     * @Title: calcTransationDetail
     * @Description: 大类资产交易明细指标计算
     * @param: transationDetailParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcTransationDetail", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcTransationDetail(TransationDetailParamDTO transationDetailParamDTO) {
    	String moduleNo = "indTransactionDetail";
//    	String moduleNo = "transactionDetail";
//    	// 根据不同节点选择不同方案
//    	String bnd = "债券";
//    	String stk = "股票";
//    	if (bnd.equals(transationDetailParamDTO.getNodeName()) || 
//    			stk.equals(transationDetailParamDTO.getNodeName())) {
//    		moduleNo = "indTransactionDetail";
//    	}
    	
    	String calcObjType = transationDetailParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		transationDetailParamDTO.setPortType(PortType.port.getCode());
    		transationDetailParamDTO.setPortCalcType(PortCalcType.single.name());
    	} else {// 对象类型为计划
    		String planCode = transationDetailParamDTO.getPlanCode();
    		transationDetailParamDTO.addPortCode(planCode);
    		transationDetailParamDTO.setPortType(PortType.plan.getCode());
    		transationDetailParamDTO.setPortCalcType(PortCalcType.single.name());
    	}
    	// 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(moduleNo, transationDetailParamDTO);

        // 加上资产类型名称-assetName
        if (resultBO.getSuccessful()) {
        	List<Map<String,Object>> resultList = resultBO.getResultList();
        	for (Map<String,Object> data : resultList) {
        		data.put("assetName", transationDetailParamDTO.getNodeName());
        	}
        }
        
        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());
    	return baseResponse;
    }
}
