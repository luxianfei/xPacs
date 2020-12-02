/**
 *******************************************
 * 文件名称: PlanOverviewServiceImpl.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 计划概览
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月13日 上午10:50:53
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.common.util.BigDecimalUtils;
import com.xquant.common.util.DateUtils;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.SortUtils;
import com.xquant.tbm.entity.TbmInfo;
import com.xquant.tbm.service.api.ITbmInfoService;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO;
import com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: PlanOverviewServiceImpl
 * @Description: 计划概览
 * @author: jingru.jiang
 * @date: 2020年11月13日 上午10:50:53
 *
 */
@Service
public class PlanOverviewServiceImpl implements IPlanOverviewService {

	@Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
	@Autowired
    private IAnalysisIndexAsyncCalcService analysisIndexAsyncCalcService;
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private ITprtService tprtService;
    @Autowired
    private ITbmInfoService tbmInfoService;

	/**
	 * <p>Title: calcPlanNav</p>
	 * <p>Description:计划的资产净值情况表计算 </p>
	 * @param planOverviewParamDTO
	 * @see com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService#calcPlanNav(com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO)
	 */
	@Override
	public HttpBaseResponse calcPlanNav(PlanOverviewParamDTO planOverviewParamDTO) {
		String moduleNo1 = "navTable";
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
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(moduleNo1, planOverviewParamDTO);
        
        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> planMap = new HashMap<String, Object>();
        if (resultBO.getSuccessful()) {
			List<Map<String,Object>> resultList = resultBO.getResultList();

			for (Map<String,Object> map1 : resultList) {
				if (map1.get("portCode").equals(planCode)) {
					planMap.putAll(map1);
					// 企业年金计划 总净值占比,排名
					planMap.put("pName", "企业年金计划");
					planMap.put("navRatio", 100);
					planMap.put("rank", "--");
					planMap.put("gbPUnitNav", null);
					planMap.put("gbPUnitNavPre", null);
					planMap.put("gbPUnitNavPreBY", null);
				} else if (map1.get("gbPTotalNav") != null){
					newResultList.add(map1);
				}
			}

			// 对新数组进行按期末总净值排序
			newResultList = SortUtils.sort(newResultList, "gbPTotalNav", "desc");
			// 计算总净值占比、排序
			Double gbPTotalNav = NumberUtils.convertToDouble(planMap.get("gbPTotalNav"));
			int rank = 1;
			for (Map<String,Object> map : newResultList) {
				Double totalNav = NumberUtils.convertToDouble(map.get("gbPTotalNav"));
				BigDecimal navRatio = BigDecimalUtils.divide(new BigDecimal(totalNav), new BigDecimal(gbPTotalNav));
				map.put("navRatio", navRatio.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
				map.put("rank", rank+"");
				rank++;
			}
			newResultList.add(planMap);
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
	}

	/**
	 * <p>Title: calcPortNav</p>
	 * <p>Description: 组合的资产净值情况表计算</p>
	 * @param planOverviewParamDTO
	 * @see com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService#calcPortNav(com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO)
	 */
	@Override
	public HttpBaseResponse calcPortNav(PlanOverviewParamDTO planOverviewParamDTO) {
        PlanOverviewParamDTO planOverviewServiceParam2 = new PlanOverviewParamDTO();
        BeanUtils.copyProperties(planOverviewParamDTO, planOverviewServiceParam2);

		String moduleNo1 = "navTable";
    	Future<AnalysisIndexCalcResultBO> future1 = null;
    	Future<AnalysisIndexCalcResultBO> future3 = null;

    	List<String> portCodeList = planOverviewParamDTO.getPortCode();
    	String portCode = portCodeList.get(0);
    	String planCode = tprtService.getTprtByPortCode(portCode).getPlanCode();

    	// 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	List<String> portCodes = new ArrayList<>();
        	// 组合
        	portCodes.add(portCode);
        	// 计划
        	portCodes.add(planCode);
            planOverviewParamDTO.setPortCode(portCodes);
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
            future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo1, planOverviewParamDTO);
        } else { // 计划复合计算-计划及其下组合
        	// 对组合进行计算
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(portCode);
        	planOverviewParamDTO.setPortCode(portCodes);
        	planOverviewParamDTO.setPortType(PortType.port.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        	future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo1, planOverviewParamDTO);

            // 对计划进行计算
        	portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	planOverviewServiceParam2.setPortCode(portCodes);
        	planOverviewServiceParam2.setPortType(PortType.plan.getCode());
        	planOverviewServiceParam2.setPortCalcType(PortCalcType.single.name());
        	future3 = analysisIndexAsyncCalcService.asyncCalc(moduleNo1, planOverviewServiceParam2);
        }

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> planMap = new HashMap<String, Object>();
        try {
			AnalysisIndexCalcResultBO result1 = future1.get();
			List<Map<String,Object>> resultList1 = result1.getResultList();

			if (future3 != null) {
				AnalysisIndexCalcResultBO result3 = future3.get();
				List<Map<String,Object>> resultList3 = result3.getResultList();
				resultList1.addAll(resultList3);
			}

			for (Map<String,Object> map1 : resultList1) {
				if (map1.get("portCode").equals(planCode)) {
					planMap.putAll(map1);
					// 企业年金计划 总净值占比,排名
					planMap.put("pName", "企业年金计划");
					planMap.put("navRatio", 100);
					planMap.put("rank", "--");
					planMap.put("gbPUnitNav", null);
					planMap.put("gbPUnitNavPre", null);
					planMap.put("gbPUnitNavPreBY", null);
				} else {
					newResultList.add(map1);
				}
			}

			// 对新数组进行按期末总净值排序
			newResultList = SortUtils.sort(newResultList, "gbPTotalNav", "desc");
			// 计算总净值占比、排序
			Double gbPTotalNav = NumberUtils.convertToDouble(planMap.get("gbPTotalNav"));
			int rank = 1;
			for (Map<String,Object> map : newResultList) {
				Double totalNav = NumberUtils.convertToDouble(map.get("gbPTotalNav"));
				BigDecimal navRatio = BigDecimalUtils.divide(new BigDecimal(totalNav), new BigDecimal(gbPTotalNav));
				map.put("navRatio", navRatio.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
				map.put("rank", rank+"");
				rank++;
			}
			newResultList.add(planMap);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}

        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getSuccessResponse(newResultList);
    	return baseResponse;
	}

	/**
	 * <p>Title: calcPlanIncome</p>
	 * <p>Description: 计划的投资收益情况表计算</p>
	 * @param planOverviewParamDTO
	 * @return
	 * @see com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService#calcPlanIncome(com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO)
	 */
	@Override
	public HttpBaseResponse calcPlanIncome(PlanOverviewParamDTO planOverviewParamDTO) {
		String moduleNo = "investIncomeTable";
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
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(moduleNo, planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> planMap = new HashMap<String, Object>();
        Map<String, Object> bmData = new HashMap<String, Object>();
        if (resultBO.getSuccessful()) {
        	List<Map<String,Object>> resultList = resultBO.getResultList();

        	// 分离 组合\计划 数据
        	for (Map<String,Object> map : resultList) {
        		if (planCode.equals(map.get("portCode"))) {
        			planMap.putAll(map);
        			// 企业年金计划 排名
        			planMap.put("pName", "企业年金计划");
        			planMap.put("rank", "--");
        		} else if (bmCode != null && bmCode.equals(map.get("bmCode"))) {
            		bmData.putAll(map);
            	}else if (map.get("gbPDefaultRtnYearedCY") != null) {
        			newResultList.add(map);
        		}
        	}

        	// 对新数组 按本年以来年化收益率进行排序
        	newResultList = SortUtils.sort(newResultList, "gbPDefaultRtnYearedCY", "desc");
        	// 计算排序,合并组合及基准为一条数据
        	int rank = 1;
        	for (Map<String,Object> data : newResultList) {
        		data.put("rank", rank++);
        		data.putAll(bmData);
        	}
        	planMap.putAll(bmData);
        	newResultList.add(planMap);
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
	}

	/**
	 * <p>Title: calcPortIncome</p>
	 * <p>Description: 组合的投资收益情况表计算</p>
	 * @param planOverviewParamDTO
	 * @return
	 * @see com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService#calcPortIncome(com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO)
	 */
	@Override
	public HttpBaseResponse calcPortIncome(PlanOverviewParamDTO planOverviewParamDTO) {
		PlanOverviewParamDTO planParam = new PlanOverviewParamDTO();
        BeanUtils.copyProperties(planOverviewParamDTO, planParam);

		String moduleNo = "investIncomeTable";
    	Future<AnalysisIndexCalcResultBO> future1 = null;
    	Future<AnalysisIndexCalcResultBO> future2 = null;

    	List<String> portCodeList = planOverviewParamDTO.getPortCode();
    	String portCode = portCodeList.get(0);
    	String planCode = tprtService.getTprtByPortCode(portCode).getPlanCode();

    	// 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        // 获取计划业绩基准
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        planOverviewParamDTO.setBmCode(bmCode);
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	List<String> portCodes = new ArrayList<>();
        	// 组合
        	portCodes.add(portCode);
        	// 计划
        	portCodes.add(planCode);
            planOverviewParamDTO.setPortCode(portCodes);
            planOverviewParamDTO.setPortType(PortType.port.getCode());
            planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
            future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo, planOverviewParamDTO);
        } else { // 计划复合计算-计划及其下组合
        	// 对组合进行计算
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(portCode);
        	planOverviewParamDTO.setPortCode(portCodes);
        	planOverviewParamDTO.setPortType(PortType.port.getCode());
        	planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());
        	future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo, planOverviewParamDTO);

            // 对计划进行计算
        	portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	planParam.setBmCode(null);
        	planParam.setPortCode(portCodes);
        	planParam.setPortType(PortType.plan.getCode());
        	planParam.setPortCalcType(PortCalcType.single.name());
        	future2 = analysisIndexAsyncCalcService.asyncCalc(moduleNo, planParam);
        }

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> planMap = new HashMap<String, Object>();
        Map<String, Object> bmData = new HashMap<String, Object>();
        try {
			AnalysisIndexCalcResultBO result1 = future1.get();
			List<Map<String,Object>> resultList1 = result1.getResultList();

			if (future2 != null) {
				AnalysisIndexCalcResultBO result2 = future2.get();
				List<Map<String,Object>> resultList2 = result2.getResultList();
				resultList1.addAll(resultList2);
			}
			// 分离 组合\计划 数据
        	for (Map<String,Object> map : resultList1) {
        		if (planCode.equals(map.get("portCode"))) {
        			planMap.putAll(map);
        			// 企业年金计划 排名
        			planMap.put("pName", "企业年金计划");
        			planMap.put("rank", "--");
        		} else if (bmCode != null && bmCode.equals(map.get("bmCode"))) {
            		bmData.putAll(map);
            	}else {
        			newResultList.add(map);
        		}
        	}

        	// 对新数组 按本年以来年化收益率进行排序
        	newResultList = SortUtils.sort(newResultList, "gbPDefaultRtnYearedCY", "desc");
        	// 计算排序,合并组合及基准为一条数据
        	int rank = 1;
        	for (Map<String,Object> data : newResultList) {
        		data.put("rank", rank++);
        		data.putAll(bmData);
        	}
        	planMap.putAll(bmData);
        	newResultList.add(planMap);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}

        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getSuccessResponse(newResultList);
    	return baseResponse;
	}

	/**
	 * <p>Title: calcPortIncomeTrendPlan</p>
	 * <p>Description: 各组合收益率走势--计划</p>
	 * @param planOverviewParamDTO
	 * @return
	 * @see com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService#calcPortIncomeTrendPlan(com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO)
	 */
	@Override
	public HttpBaseResponse calcPortIncomeTrendPlan(PlanOverviewParamDTO planOverviewParamDTO) {
		String planCode = planOverviewParamDTO.getPlanCode();

    	// 获取计划业绩基准
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        // 获取基准信息
        TbmInfo tbmInfo = tbmInfoService.getTbmInfoByBmId(bmCode);
        planOverviewParamDTO.setBmCode(bmCode);
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
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("investIncomeTrend", planOverviewParamDTO);

        List<Map<String,Object>> newResultList = new ArrayList<Map<String,Object>>();
        Map<String,List<Map<String,Object>>> endDateMap = new HashMap<String,List<Map<String,Object>>>();
        Map<String,Object> bmMap = new HashMap<String, Object>();
        if (resultBO.getSuccessful()) {
        	/**
        	 * 累计收益率时序图：时序图从0点开始，
        	 * 处理方式：画趋势图时在输入的[开始日期，结束日期]返回的实际数据集前补充一条“日期=开始日期-1，累计收益率=0”的记录。
        	 */
        	String newBegDate = DateUtils.addDate(planOverviewParamDTO.getBegDate(), -1);
        	// 组合
        	for(Tprt tprt : portList) {
        		Map<String,Object> portMap = new HashMap<String, Object>();
            	portMap.put("pName", tprt.getpName());
            	portMap.put("begDate", newBegDate);
            	portMap.put("endDate", newBegDate);
            	portMap.put("portCode", tprt.getPortCode());
            	portMap.put("gbPDefaultRtnCumulativeD", 0);
            	resultBO.getResultList().add(portMap);
            }
        	// 基准
        	bmMap.put("bmName", tbmInfo.getBmName());
        	bmMap.put("begDate", newBegDate);
        	bmMap.put("endDate", newBegDate);
        	bmMap.put("bmCode", bmCode);
        	bmMap.put("gbBRtnCumulativeD", 0);
        	resultBO.getResultList().add(bmMap);
        	
        	// 更改业绩基准名称
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (bmCode != null && bmCode.equals(map.get("bmCode"))){
        			map.put("bmName", "业绩基准-"+map.get("bmName"));
        		} else if (map.get("portCode") != null && !map.get("pName").toString().contains("直投")) {
        			String endDate = map.get("endDate").toString();
        			// 按时间分组
        			if (endDateMap.containsKey(endDate)) {
        				List<Map<String,Object>> list = endDateMap.get(endDate);
        				list.add(map);
        				endDateMap.put(endDate, list);
        			} else {
        				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        				list.add(map);
        				endDateMap.put(endDate, list);
        			}
        		}
        	}

        	newResultList.addAll(resultBO.getResultList());

        	// 计算每日中位数
        	for (Entry<String, List<Map<String, Object>>> entry : endDateMap.entrySet()) {
        		Map<String, Object> median = median(entry.getValue(),"gbPDefaultRtnCumulativeD","asc");
        		Map<String, Object> newMedian = new HashMap<String, Object>();
        		newMedian.put("medianName", "中位数");
        		newMedian.putAll(median);
        		newMedian.put("pName", "中位数");
        		newResultList.add(newMedian);
        	}
        }

        // 排序
        newResultList = SortUtils.sort(newResultList, "endDate", "asc");
        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newResultList);
    	return baseResponse;
	}

	/**
	 * <p>Title: calcPortIncomeTrendPort</p>
	 * <p>Description: 各组合收益率走势--组合</p>
	 * @param planOverviewParamDTO
	 * @return
	 * @see com.xquant.xpacs.investAnalysis.service.api.IPlanOverviewService#calcPortIncomeTrendPort(com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO)
	 */
	@Override
	public HttpBaseResponse calcPortIncomeTrendPort(PlanOverviewParamDTO planOverviewParamDTO) {
		List<String> portCodeList = planOverviewParamDTO.getPortCode();
    	String portCode = portCodeList.get(0);
    	Tprt tprt = tprtService.getTprtByPortCode(portCode);
    	String planCode = tprt.getPlanCode();

    	// 获取计划业绩基准
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        String bmCodes = plan.getBaseBench();
        String bmCode = bmCodes.split(",")[0];
        // 获取基准信息
        TbmInfo tbmInfo = tbmInfoService.getTbmInfoByBmId(bmCode);
        planOverviewParamDTO.setBmCode(bmCode);
        planOverviewParamDTO.setPortType(PortType.port.getCode());
        planOverviewParamDTO.setPortCalcType(PortCalcType.single.name());

        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("investIncomeTrend", planOverviewParamDTO);

        Map<String,Object> portMap = new HashMap<String, Object>();
        Map<String,Object> bmMap = new HashMap<String, Object>();
        if (resultBO.getSuccessful()) {
        	/**
        	 * 累计收益率时序图：时序图从0点开始，
        	 * 处理方式：画趋势图时在输入的[开始日期，结束日期]返回的实际数据集前补充一条“日期=开始日期-1，累计收益率=0”的记录。
        	 */
        	String newBegDate = DateUtils.addDate(planOverviewParamDTO.getBegDate(), -1);
        	// 组合
        	portMap.put("pName", tprt.getpName());
        	portMap.put("begDate", newBegDate);
        	portMap.put("endDate", newBegDate);
        	portMap.put("portCode", portCode);
        	portMap.put("gbPDefaultRtnCumulativeD", 0);
        	resultBO.getResultList().add(portMap);
        	// 基准
        	bmMap.put("bmName", tbmInfo.getBmName());
        	bmMap.put("begDate", newBegDate);
        	bmMap.put("endDate", newBegDate);
        	bmMap.put("bmCode", bmCode);
        	bmMap.put("gbBRtnCumulativeD", 0);
        	resultBO.getResultList().add(bmMap);
        	
        	// 更改业绩基准名称
        	for (Map<String,Object> map : resultBO.getResultList()) {
        		if (bmCode != null && bmCode.equals(map.get("bmCode"))){
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
	 * @Title: median
	 * @Description: 计算中位数
	 * @param: dataList
	 * @param: sortField
	 * @param: sortType
	 * @return: Map<String,Object>
	 * @throws
	 */
	private Map<String,Object> median(List<Map<String,Object>> dataList, final String sortField, final String sortType) {
		Map<String,Object> map = new HashMap<String,Object>();
		// 集合排序
		List<Map<String,Object>> list = SortUtils.sort(dataList, sortField, sortType);

		int size = list.size();
		if (size % 2 == 1) {
			map = list.get((size - 1) / 2);
		} else {
			map = list.get(size / 2 - 1);
			Double first = NumberUtils.convertToDouble(map.get(sortField));
			Double second = NumberUtils.convertToDouble(list.get(size / 2).get(sortField));
			BigDecimal total = BigDecimalUtils.add(new BigDecimal(first), new BigDecimal(second));
			BigDecimal median = BigDecimalUtils.divide(total, new BigDecimal(2));
			map.put(sortField, median);
		}
		return map;
	}


}
