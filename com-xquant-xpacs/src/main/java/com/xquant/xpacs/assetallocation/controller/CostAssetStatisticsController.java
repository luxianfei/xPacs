/**
 *******************************************
 * 文件名称: CostAssetStatisticsController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 成本类资产统计
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月25日 下午6:19:25
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
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.assetallocation.entity.dto.CostAssetParamDTO;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.inforQuery.controller.CashFlowQueryController;
import com.xquant.xpacs.investAnalysis.enums.CalcObjType;

/**
 * @ClassName: CostAssetStatisticsController
 * @Description: 成本类资产统计
 * @author: jingru.jiang
 * @date: 2020年11月25日 下午6:19:25
 *
 */
@RestController
@RequestMapping("/costAssetStatistics")
public class CostAssetStatisticsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashFlowQueryController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    
    /**
     * @Title: calcCostCreditRating
     * @Description: 计算成本类资产信用评级分布
     * @param: costAssetParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcCostCreditRating", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcCostCreditRating(CostAssetParamDTO costAssetParamDTO) {
    	String planCode = costAssetParamDTO.getPlanCode();
    	String calcObjType = costAssetParamDTO.getCalcObjType();
    	
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		costAssetParamDTO.setPortType(PortType.port.getCode());
            costAssetParamDTO.setPortCalcType(PortCalcType.single.name());
    	} else {
    		List<String> portCodeList = new ArrayList<String>();
    		portCodeList.add(planCode);
			costAssetParamDTO.setPortCode(portCodeList);
    		costAssetParamDTO.setPortType(PortType.plan.getCode());
        	costAssetParamDTO.setPortCalcType(PortCalcType.single.name());
    	}
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("costAssetCreditRating", costAssetParamDTO);

        List<Map<String, Object>> newResultList = new ArrayList<Map<String,Object>>();
        if (resultBO.getSuccessful()) {
        	// 只获取有市值的评级记录
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (map.get("gValue") != null && map.get("topComp") != null) {
        			newResultList.add(map);
        		} else if (map.get("gValue") == null && map.get("topComp") != null) {
        			map.put("gValue", "无评级");
        			newResultList.add(map);
        		}
        	}
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }
    
}
