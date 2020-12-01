/**
 *******************************************
 * 文件名称: RelativeAchievementController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 收益情况
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 相对业绩
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月6日 下午2:05:35
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.dictionary.entity.dto.EnumDictionary;
import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.tbm.service.api.ITbmInfoService;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.dict.service.api.ISystemDictService;
import com.xquant.xpacs.investAnalysis.entity.dto.RelativeAchievementParamDTO;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: RelativeAchievementController
 * @Description: 相对业绩
 * @author: jingru.jiang
 * @date: 2020年11月6日 下午2:05:35
 *
 */
@RestController
@RequestMapping("/relativeAchievement")
public class RelativeAchievementController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RelativeAchievementController.class);
	
    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private ITprtService tprtService;
    @Autowired
    private ITbmInfoService tbmInfoService;
    @Autowired
    private ISystemDictService systemDictService;
    
    /**
     * @Title: calcRelativeAchievement
     * @Description: 相对业绩指标计算
     * @param: relativeAchievementParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcRelativeAchievement", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcRelativeAchievement(RelativeAchievementParamDTO relativeAchievementParamDTO) {
    	String bmCode = relativeAchievementParamDTO.getBmCode();
    	
    	String planCode = relativeAchievementParamDTO.getPlanCode();
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
            relativeAchievementParamDTO.setPortCode(portCodes);
            relativeAchievementParamDTO.setPortType(PortType.port.getCode());
            relativeAchievementParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	relativeAchievementParamDTO.setPortCode(portCodes);
        	relativeAchievementParamDTO.setPortType(PortType.plan.getCode());
        	relativeAchievementParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("relativeAchievement", relativeAchievementParamDTO);

        List<Map<String,Object>> newDataList = new ArrayList<Map<String,Object>>();
        Map<String, Object> bmData = new HashMap<String, Object>();
        if(resultBO.getSuccessful()) {
        	//调用成功
            List<Map<String,Object>> dataList = resultBO.getResultList();
            for(Map<String, Object> data : dataList) {
            	if (bmCode.equals(data.get("bmCode"))) {
            		bmData = data;
            	} else {
            		newDataList.add(data);
            	}
            }
        }
        
        if(newDataList.size() != 0) {
        	//合并组合及基准为一条数据
            for(Map<String, Object> data : newDataList) {
            	data.putAll(bmData);
            }
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newDataList);

        return baseResponse;
    }
    
    /**
     * @Title: getTbmInfo
     * @Description: 获取市场基准成分信息
     * @param: endDate
     * @param: subType
     * @return: HttpBaseResponse   
     * @throws
     */
    @RequestMapping(value = "/getTbmInfo", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse getTbmInfo(String endDate, String subType) {
    	List <String> tbmInfoList = new ArrayList<String>();
    	List<EnumDictionary> dictList = systemDictService.getDictList(subType);
    	for (EnumDictionary dict : dictList) {
    		String tbmInfoCompName = tbmInfoService.getTbmInfoCompName(dict.getDictKey(), endDate);
    		String tbmInfo = dict.getDictValue() + " = " + tbmInfoCompName;
    		tbmInfoList.add(tbmInfo);
    	}

        return HttpBaseResponseUtil.getListResponse(tbmInfoList);
    }
}
