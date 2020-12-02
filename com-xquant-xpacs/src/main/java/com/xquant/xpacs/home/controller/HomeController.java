/**
 *******************************************
 * 文件名称: HomeController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 首页
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 首页
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月30日 上午9:09:27
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.home.controller;

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

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.home.entity.vo.HomePlanInfoParamVO;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: HomeController
 * @Description: 首页
 * @author: jingru.jiang
 * @date: 2020年11月30日 上午9:09:27
 *
 */
@RestController
@RequestMapping("/home")
public class HomeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
	@Autowired
    private IPlanInfoService planInfoService;
	@Autowired
    private ITprtService tprtService;
	
    /**
     * @Title: calcBasePlanInfo
     * @Description: 计算首页基本信息
     * @param: homePlanInfoParamVO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcBasePlanInfo", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcBasePlanInfo(HomePlanInfoParamVO homePlanInfoParamVO) {
    	homePlanInfoParamVO.setModuleNo("basePlanInfo");
    	String planCode = homePlanInfoParamVO.getPlanCode();
    	List<String> portCodes = new ArrayList<>();
    	portCodes.add(planCode);
    	homePlanInfoParamVO.setPortCode(portCodes);

    	// 判断计划数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
            homePlanInfoParamVO.setPortType(PortType.port.getCode());
            homePlanInfoParamVO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算
        	homePlanInfoParamVO.setPortType(PortType.plan.getCode());
        	homePlanInfoParamVO.setPortCalcType(PortCalcType.single.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("basePlanInfo", homePlanInfoParamVO);

		// latestDate-最新净值日
		Map<String, String> latestNavDate = tprtService.getLatestNavDateByPlanCode(planCode);
		String latestDate = latestNavDate.get("T_DATE");
		// 存量组合数
		List<Tprt> portList = tprtService.getRetainPortListByPlanCode(planCode, latestDate);
		int retainCount = portList == null ? 0 : portList.size();
		
		List<Map<String, Object>> newResultList = new ArrayList<Map<String, Object>>();
		if (resultBO.getSuccessful()) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("latestDate", latestDate);
			data.put("retainCount", retainCount);
			data.put("planName", plan.getPlanName());
			data.put("planFullname", plan.getPlanFullname()+"("+plan.getPlanCode()+")");
			data.put("planCode", plan.getPlanCode());
			data.put("invsetDate", plan.getBegDate());
			// 加入基本信息
			for (Map<String, Object> map : resultBO.getResultList()) {
				data.putAll(map);
			}
			newResultList.add(data);
		}
        
        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }
    
    /**
     * @Title: calcCumulativeIncome
     * @Description: 计算首页累计收益率趋势图
     * @param: homePlanInfoParamVO
     * @return: HttpBaseResponse   
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcCumulativeIncome", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcCumulativeIncome(HomePlanInfoParamVO homePlanInfoParamVO) {
    	String planCode = homePlanInfoParamVO.getPlanCode();
    	List<String> portCodes = new ArrayList<>();
    	portCodes.add(planCode);
    	homePlanInfoParamVO.setPortCode(portCodes);

    	// 判断计划数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 获取计划业绩基准
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        homePlanInfoParamVO.setBmCode(bmCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
            homePlanInfoParamVO.setPortType(PortType.port.getCode());
            homePlanInfoParamVO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算
        	homePlanInfoParamVO.setPortType(PortType.plan.getCode());
        	homePlanInfoParamVO.setPortCalcType(PortCalcType.single.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(homePlanInfoParamVO.getModuleNo(), homePlanInfoParamVO);

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());
    	return baseResponse;
    }
    
}
