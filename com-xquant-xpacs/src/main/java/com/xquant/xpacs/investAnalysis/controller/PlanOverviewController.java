/**
 *******************************************
 * 文件名称: PlanOverviewController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 计划概览
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月16日 上午9:28:08
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.common.util.BigDecimalUtils;
import com.xquant.common.util.DateUtils;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.SortUtils;
import com.xquant.common.util.excel.ExcelExportUtils;
import com.xquant.tbm.entity.TbmInfo;
import com.xquant.tbm.service.api.ITbmInfoService;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.investAnalysis.entity.dto.InvestIncomeExportDTO;
import com.xquant.xpacs.investAnalysis.entity.dto.NetAssetsValueExportDTO;
import com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO;
import com.xquant.xpacs.investAnalysis.enums.CalcObjType;
import com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import sun.misc.BASE64Decoder;

/**
 * @ClassName: PlanOverviewController
 * @Description: 计划概览
 * @author: jingru.jiang
 * @date: 2020年11月16日 上午9:28:08
 *
 */
@RestController
@RequestMapping("/planOverview")
public class PlanOverviewController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlanOverviewController.class);
	// 资产净值情况导出模板
//	private static final String NAV_TEMPLATE = "";
	// 投资收益情况导出模板
//	private static final String INCOME_TEMPLATE = "";

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private ITprtService tprtService;
    @Autowired
    private IPlanOverviewService planOverviewService;
    @Autowired
    private IAnalysisDataCacheService dataCacheService;
    @Autowired
    private ITbmInfoService tbmInfoService;

    /**
     * @Title: calcNetAssetValue
     * @Description: 资产净值情况表计算
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcNetAssetValue", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcNetAssetValue(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	HttpBaseResponse baseResponse = new HttpBaseResponse ();

    	// 对象类型为计划
    	if (CalcObjType.plan.getCode().equals(calcObjType)) {
    		baseResponse = planOverviewService.calcPlanNav(planOverviewParamDTO);
    	} else {
    		// 对象类型为组合
    		baseResponse = planOverviewService.calcPortNav(planOverviewParamDTO);
    	}
        return baseResponse;
    }

    /**
     * @Title: calcNetRatio
     * @Description: 各组合资产占比饼状图指标计算
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcNetRatio", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcNetRatio(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		return HttpBaseResponseUtil.getSuccessResponse();
    	}

    	String planCode = planOverviewParamDTO.getPlanCode();

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
            planOverviewParamDTO.setPortCode(portCodes);
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	planOverviewParamDTO.setPortCode(portCodes);
        	planOverviewParamDTO.setPortType(PortType.plan.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("totalNavChart", planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> planMap = new HashMap<String, Object>();

        if (resultBO.getSuccessful()) {
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (map.get("portCode").equals(planCode)) {
        			planMap.putAll(map);
        		} else {
        			newResultList.add(map);
        		}
        	}
        	// 对新数组进行按期末总净值排序
        	newResultList = SortUtils.sort(newResultList, "gbPTotalNav", "desc");
        	// 计算总净值占比
        	Double gbPTotalNav = NumberUtils.convertToDouble(planMap.get("gbPTotalNav"));
        	for (Map<String,Object> map : newResultList) {
        		Double totalNav = NumberUtils.convertToDouble(map.get("gbPTotalNav"));
        		BigDecimal navRatio = BigDecimalUtils.divide(new BigDecimal(totalNav), new BigDecimal(gbPTotalNav));
        		map.put("navRatio", navRatio.multiply(new BigDecimal(1)).setScale(4, BigDecimal.ROUND_HALF_UP));
        	}
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }

    /**
     * @Title: calcTotalNav
     * @Description: 各组合期末资产净值柱状图指标计算
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcTotalNav", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcTotalNav(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		return HttpBaseResponseUtil.getSuccessResponse();
    	}

    	String planCode = planOverviewParamDTO.getPlanCode();

    	// 获取计划下所有组合
    	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
    	List<String> portCodes = new ArrayList<>();
    	for(Tprt tprt : portList) {
    		portCodes.add(tprt.getPortCode());
    	}
    	planOverviewParamDTO.setPortCode(portCodes);
    	planOverviewParamDTO.setPortType(PortType.port.getCode());
    	planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());

        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("totalNavChart", planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        if (resultBO.getSuccessful()) {
        	// 组进行按期末总净值排序
        	newResultList = SortUtils.sort(resultBO.getResultList(), "gbPTotalNav", "asc");
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }

    /**
     * @Title: calcInvestIncome
     * @Description: 投资收益情况表指标计算
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcInvestIncome", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcInvestIncome(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	HttpBaseResponse baseResponse = new HttpBaseResponse ();

    	// 对象类型为计划
    	if (CalcObjType.plan.getCode().equals(calcObjType)) {
    		baseResponse = planOverviewService.calcPlanIncome(planOverviewParamDTO);
    	} else {
    		// 对象类型为组合
    		baseResponse = planOverviewService.calcPortIncome(planOverviewParamDTO);
    	}
        return baseResponse;
    }

    /**
     * @Title: calcPlanIncomeTrend
     * @Description: 企业年金计划收益率走势
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcPlanIncomeTrend", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcPlanIncomeTrend(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		return HttpBaseResponseUtil.getSuccessResponse();
    	}

    	String planCode = planOverviewParamDTO.getPlanCode();
    	planOverviewParamDTO.addPortCode(planCode);

    	// 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 获取计划业绩基准
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        // 获取基准信息
        TbmInfo tbmInfo = tbmInfoService.getTbmInfoByBmId(bmCode);
        planOverviewParamDTO.setBmCode(bmCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算
        	planOverviewParamDTO.setPortType(PortType.plan.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("investIncomeTrend", planOverviewParamDTO);

        Map<String,Object> planMap = new HashMap<String, Object>();
        Map<String,Object> bmMap = new HashMap<String, Object>();
        if (resultBO.getSuccessful()) {
        	/**
        	 * 累计收益率时序图：时序图从0点开始，
        	 * 处理方式：画趋势图时在输入的[开始日期，结束日期]返回的实际数据集前补充一条“日期=开始日期-1，累计收益率=0”的记录。
        	 */
        	String newBegDate = DateUtils.addDate(planOverviewParamDTO.getBegDate(), -1);
        	// 计划
        	planMap.put("pName", "企业年金计划");
        	planMap.put("begDate", newBegDate);
        	planMap.put("endDate", newBegDate);
        	planMap.put("portCode", planCode);
        	planMap.put("gbPDefaultRtnCumulativeD", 0);
        	resultBO.getResultList().add(planMap);
        	// 基准
        	bmMap.put("bmName", tbmInfo.getBmName());
        	bmMap.put("begDate", newBegDate);
        	bmMap.put("endDate", newBegDate);
        	bmMap.put("bmCode", bmCode);
        	bmMap.put("gbBRtnCumulativeD", 0);
        	resultBO.getResultList().add(bmMap);
        	
        	// 更改计划及业绩基准名称
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (planCode.equals(map.get("portCode"))) {
        			map.put("pName", "企业年金计划");
        		} else if (bmCode.equals(map.get("bmCode"))){
        			map.put("bmName", "业绩基准-"+map.get("bmName"));
        		}
        	}
        	
        }

        // 排序
    	List<Map<String, Object>> newResultList = SortUtils.sort(resultBO.getResultList(), "endDate", "asc");
        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }

    /**
     * @Title: calcPortIncomeTrend
     * @Description: 各组合收益率走势
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcPortIncomeTrend", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcPortIncomeTrend(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	HttpBaseResponse baseResponse = new HttpBaseResponse ();

    	// 对象类型为计划
    	if (CalcObjType.plan.getCode().equals(calcObjType)) {
    		baseResponse = planOverviewService.calcPortIncomeTrendPlan(planOverviewParamDTO);
    	} else {
    		// 对象类型为组合
    		baseResponse = planOverviewService.calcPortIncomeTrendPort(planOverviewParamDTO);
    	}
        return baseResponse;
    }

    /**
     * @Title: calcIncomeCY
     * @Description: 各组合本年以来收益率
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcIncomeCY", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcIncomeCY(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		return HttpBaseResponseUtil.getSuccessResponse();
    	}

    	String planCode = planOverviewParamDTO.getPlanCode();

    	// 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 获取计划业绩基准
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        planOverviewParamDTO.setBmCode(bmCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 获取计划下所有组合
        	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
            for(Tprt tprt : portList) {
                portCodes.add(tprt.getPortCode());
            }
            planOverviewParamDTO.setPortCode(portCodes);
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	planOverviewParamDTO.setPortCode(portCodes);
        	planOverviewParamDTO.setPortType(PortType.plan.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("incomeCYChart", planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        if (resultBO.getSuccessful()) {
        	// 更改计划及业绩基准名称,合并数据
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (planCode.equals(map.get("portCode"))) {
        			map.put("pName", "企业年金计划");
        		} else if (bmCode.equals(map.get("bmCode"))){
        			map.put("pName", "业绩基准-"+map.get("bmName"));
        			map.put("gbPDefaultRtnCumulativeCY", map.get("gbBRtnCumulativeCY"));
        		}
        	}
        	// 按本年以来累计收益率排序
        	newResultList = SortUtils.sort(resultBO.getResultList(), "gbPDefaultRtnCumulativeCY", "asc");
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }

    /**
     * @Title: calcIncomeYeardCY
     * @Description: 各组合当年年化收益率
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcIncomeYeardCY", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcIncomeYeardCY(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		return HttpBaseResponseUtil.getSuccessResponse();
    	}

    	String planCode = planOverviewParamDTO.getPlanCode();

    	// 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 获取计划业绩基准
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        planOverviewParamDTO.setBmCode(bmCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 获取计划下所有组合
        	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
            for(Tprt tprt : portList) {
                portCodes.add(tprt.getPortCode());
            }
            planOverviewParamDTO.setPortCode(portCodes);
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	planOverviewParamDTO.setPortCode(portCodes);
        	planOverviewParamDTO.setPortType(PortType.plan.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("incomeYearedCYChart", planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        if (resultBO.getSuccessful()) {
        	// 更改计划及业绩基准名称,合并数据
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (planCode.equals(map.get("portCode"))) {
        			map.put("pName", "企业年金计划");
        		} else if (bmCode.equals(map.get("bmCode"))){
        			map.put("pName", "业绩基准-"+map.get("bmName"));
        			map.put("gbPDefaultRtnYearedCY", map.get("gbBRtnCumulativeYearedCY"));
        		}
        	}
        	// 按本年以来年化收益率排序
        	newResultList = SortUtils.sort(resultBO.getResultList(), "gbPDefaultRtnYearedCY", "asc");
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }

    /**
     * @Title: calcIncomeSU
     * @Description: 各组合成立以来累计收益率
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse
     * @throws
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcIncomeSU", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcIncomeSU(PlanOverviewParamDTO planOverviewParamDTO) {
    	String calcObjType = planOverviewParamDTO.getCalcObjType();
    	// 对象类型为组合
    	if (CalcObjType.port.getCode().equals(calcObjType)) {
    		return HttpBaseResponseUtil.getSuccessResponse();
    	}

    	String planCode = planOverviewParamDTO.getPlanCode();

    	// 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 获取计划业绩基准
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        planOverviewParamDTO.setBmCode(bmCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 获取计划下所有组合
        	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
            for(Tprt tprt : portList) {
                portCodes.add(tprt.getPortCode());
            }
            planOverviewParamDTO.setPortCode(portCodes);
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	planOverviewParamDTO.setPortCode(portCodes);
        	planOverviewParamDTO.setPortType(PortType.plan.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("incomeSUChart", planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        if (resultBO.getSuccessful()) {
        	// 更改计划及业绩基准名称,合并数据
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (planCode.equals(map.get("portCode"))) {
        			map.put("pName", "企业年金计划");
        		} else if (bmCode.equals(map.get("bmCode"))){
        			map.put("pName", "业绩基准-"+map.get("bmName"));
        			map.put("gbPDefaultRtnCumulativeSU", map.get("gbBRtnCumulativeSysSU"));
        		}
        	}
        	// 按成立以来累计收益率排序
        	newResultList = SortUtils.sort(resultBO.getResultList(), "gbPDefaultRtnCumulativeSU", "asc");
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月22日 20:09
     * @description: 资产净值导出EXCEL，包括图片
     * @param netAssetsValueExportDTO
     * @param response
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    @PostMapping("/exportNetAssetsValue")
    public HttpBaseResponse exportNetAssetsValue(@RequestBody NetAssetsValueExportDTO netAssetsValueExportDTO, HttpServletResponse response) {
    	String calcObjType = netAssetsValueExportDTO.getCalcObjType();
    	Map<String, Object> dataMap = new HashMap<>();
    	
    	// 查询区间--queryDate
    	String queryDate = netAssetsValueExportDTO.getBegDate() + "~" +netAssetsValueExportDTO.getEndDate();
    	dataMap.put("queryDate", queryDate);
    	dataMap.put("endDate", netAssetsValueExportDTO.getEndDate());

    	// 缓存表格数据获取
    	List<String> requestIds = netAssetsValueExportDTO.getRequestIds();
    	int j = 0;
    	for(String requestId : requestIds) {
			List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
			dataMap.put("list" + (++j), dataList);
		}

    	// 图片数据获取
    	BASE64Decoder decoder = new BASE64Decoder();
		List<String> chartDatas = netAssetsValueExportDTO.getChartDatas();
		int i = 0;
		for(String chartData : chartDatas) {
			try {
//				String base64Code = chartData.substring(22);
				byte[] imgBytes = decoder.decodeBuffer(chartData);
				ImageEntity imageEntity = new ImageEntity();
				imageEntity.setData(imgBytes);
				imageEntity.setColspan(3);
				imageEntity.setRowspan(25);
				dataMap.put("chart" + (++i), imageEntity);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		ClassPathResource classPathResource = new ClassPathResource(File.separator + "template" + File.separator + "netAssetsValueTemplate.xlsx");
		String tempPath = classPathResource.getPath();
		TemplateExportParams template = new TemplateExportParams(tempPath);

		// 导出文件名称拼接   计划:计划或组合资产净值情况-计划层-YYYYDDMM,组合:计划或组合资产净值情况-指定组合名称-YYYYDDMM
		String fileName = "";
		String newDate = DateUtils.formatDateStr(netAssetsValueExportDTO.getEndDate(), 
				DateUtils.SIMPLE_SHORT, DateUtils.DT_SHORT);
    	// 对象类型为计划
    	if (CalcObjType.plan.getCode().equals(calcObjType)) {
    		fileName = "计划或组合资产净值情况-计划层-" + newDate + ".xlsx";
    	} else {
    		// 对象类型为组合
    		String portCode = netAssetsValueExportDTO.getPortCode();
    		Tprt tprt = tprtService.getTprtByPortCode(portCode);
    		fileName = "计划或组合资产净值情况-" + tprt.getpName() + "-" + newDate + ".xlsx";
    	}
		Workbook workbook = ExcelExportUtil.exportExcel(template, dataMap);
		ExcelExportUtils.downLoadExcel(workbook, fileName, response);
    	return null;
	}

    /**
     * @Title: exportInvestIncome
     * @Description: 投资收益导出EXCEL，包括图片
     * @param: investIncomeExportDTO
     * @param: response
     * @return: HttpBaseResponse   
     * @throws
     */
    @PostMapping("/exportInvestIncome")
    public HttpBaseResponse exportInvestIncome(@RequestBody InvestIncomeExportDTO investIncomeExportDTO, HttpServletResponse response) {
    	String calcObjType = investIncomeExportDTO.getCalcObjType();
    	Map<String, Object> dataMap = new HashMap<>();
    	
    	// 查询区间--queryDate
    	String queryDate = investIncomeExportDTO.getBegDate() + "~" +investIncomeExportDTO.getEndDate();
    	dataMap.put("queryDate", queryDate);
    	dataMap.put("endDate", investIncomeExportDTO.getEndDate());

    	// 缓存表格数据获取
    	List<String> requestIds = investIncomeExportDTO.getRequestIds();
    	int j = 0;
    	for(String requestId : requestIds) {
			List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
			dataMap.put("list" + (++j), dataList);
		}

    	// 图片数据获取
    	BASE64Decoder decoder = new BASE64Decoder();
		List<String> chartDatas = investIncomeExportDTO.getChartDatas();
		int i = 0;
		for(String chartData : chartDatas) {
			try {
				byte[] imgBytes = decoder.decodeBuffer(chartData);
				ImageEntity imageEntity = new ImageEntity();
				imageEntity.setData(imgBytes);
				if (i <= 1) {
					imageEntity.setColspan(9);
					imageEntity.setRowspan(23);
				} else {
					imageEntity.setColspan(3);
					imageEntity.setRowspan(21);
				}
				dataMap.put("chart" + (++i), imageEntity);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		ClassPathResource classPathResource = new ClassPathResource(File.separator + "template" + File.separator + "investIncomeTemplate.xlsx");
		String tempPath = classPathResource.getPath();
		TemplateExportParams template = new TemplateExportParams(tempPath);

		// 导出文件名称拼接   计划:计划或组合投资收益情况-计划层-YYYYDDMM,组合:计划或组合投资收益情况-指定组合名称-YYYYDDMM
		String fileName = "";
		String newDate = DateUtils.formatDateStr(investIncomeExportDTO.getEndDate(), 
				DateUtils.SIMPLE_SHORT, DateUtils.DT_SHORT);
    	// 对象类型为计划
    	if (CalcObjType.plan.getCode().equals(calcObjType)) {
    		fileName = "计划或组合投资收益情况-计划层-" + newDate + ".xlsx";
    	} else {
    		// 对象类型为组合
    		String portCode = investIncomeExportDTO.getPortCode();
    		Tprt tprt = tprtService.getTprtByPortCode(portCode);
    		fileName = "计划或组合投资收益情况-" + tprt.getpName() + "-" + newDate + ".xlsx";
    	}
		Workbook workbook = ExcelExportUtil.exportExcel(template, dataMap);
		ExcelExportUtils.downLoadExcel(workbook, fileName, response);
    	return null;
	}
}
