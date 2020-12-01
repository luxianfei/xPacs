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
import java.util.HashMap;
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
import com.xquant.common.util.DateUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.assetallocation.entity.dto.TransationCompParamDTO;
import com.xquant.xpacs.assetallocation.entity.dto.TransationDetailParamDTO;
import com.xquant.xpacs.assetallocation.entity.dto.TransationOverviewParamDTO;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
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
    	String moduleNo2 = "preTransactionOverview";
    	String begDate = transationOverviewParamDTO.getBegDate();
    	String newDate = DateUtils.addDate(begDate, 1);
    	
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
    	TransationOverviewParamDTO preOverviewParam = new TransationOverviewParamDTO();
        BeanUtils.copyProperties(transationOverviewParamDTO, preOverviewParam);
        preOverviewParam.setBegDate(newDate);
    	
        // 指标计算
        Future<AnalysisIndexCalcResultBO> future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo1, transationOverviewParamDTO);
        Future<AnalysisIndexCalcResultBO> future2 = analysisIndexAsyncCalcService.asyncCalc(moduleNo2, preOverviewParam);
        
        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        try {
			AnalysisIndexCalcResultBO result1 = future1.get();
			AnalysisIndexCalcResultBO result2 = future2.get();
			newResultList = result1.getResultList();
			List<Map<String,Object>> resultList2 = result2.getResultList();
			
			// 合并期初指标数据
			Map<String,Map<String,Object>> newMap = new HashMap<String, Map<String,Object>>();
			for (Map<String,Object> map2 : resultList2) {
				newMap.put(map2.get("nodeId").toString(), map2);
			}
			for (Map<String,Object> map1 : newResultList) {
				if(newMap.containsKey(map1.get("nodeId").toString())) {
					map1.putAll(newMap.get(map1.get("nodeId").toString()));
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
    	String moduleNo2 = "preTransactionComp";
    	String begDate = transationCompParamDTO.getBegDate();
    	String newDate = DateUtils.addDate(begDate, 1);
    	
		transationCompParamDTO.setPortType(PortType.port.getCode());
		transationCompParamDTO.setPortCalcType(PortCalcType.single.name());
		TransationCompParamDTO preCompParam = new TransationCompParamDTO();
        BeanUtils.copyProperties(transationCompParamDTO, preCompParam);
        preCompParam.setBegDate(newDate);
    	
        // 指标计算
        Future<AnalysisIndexCalcResultBO> future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo1, transationCompParamDTO);
        Future<AnalysisIndexCalcResultBO> future2 = analysisIndexAsyncCalcService.asyncCalc(moduleNo2, preCompParam);
        
        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        try {
			AnalysisIndexCalcResultBO result1 = future1.get();
			AnalysisIndexCalcResultBO result2 = future2.get();
			newResultList = result1.getResultList();
			List<Map<String,Object>> resultList2 = result2.getResultList();
			
			// 合并期初指标数据
			Map<String,Map<String,Object>> newMap = new HashMap<String, Map<String,Object>>();
			for (Map<String,Object> map2 : resultList2) {
				newMap.put(map2.get("portCode").toString(), map2);
			}
			for (Map<String,Object> map1 : newResultList) {
				if(newMap.containsKey(map1.get("portCode"))) {
					map1.putAll(newMap.get(map1.get("portCode").toString()));
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
    	String moduleNo = "transactionDetail";
    	// 根据不同节点选择不同方案
    	if ("债券".equals(transationDetailParamDTO.getNodeName()) || 
    			"股票".equals(transationDetailParamDTO.getNodeName())) {
    		moduleNo = "indTransactionDetail";
    	}
    	
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
