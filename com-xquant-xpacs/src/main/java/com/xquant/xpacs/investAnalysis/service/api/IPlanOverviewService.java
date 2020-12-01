/**
 *******************************************
 * 文件名称: IPlanOverviewService.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 收益情况
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 计划概览
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月13日 上午10:49:55
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.service.api;

import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.investAnalysis.entity.dto.PlanOverviewParamDTO;

/**
 * @ClassName: IPlanOverviewService
 * @Description: 计划概览
 * @author: jingru.jiang
 * @date: 2020年11月13日 上午10:49:55
 *
 */
public interface IPlanOverviewService {
	
	/**
     * @Title: calcPlanNav
     * @Description: 计划的资产净值情况表计算
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
	HttpBaseResponse calcPlanNav(PlanOverviewParamDTO planOverviewParamDTO);
	
	/**
	 * @Title: calcPortNav
	 * @Description: 组合的资产净值情况表计算
     * @param: planOverviewParamDTO
     * @return: HttpBaseResponse   
     * @throws
     */
	HttpBaseResponse calcPortNav(PlanOverviewParamDTO planOverviewParamDTO);
	
	/**
	 * @Title: calcPlanIncome
	 * @Description: 计划的投资收益情况表计算
	 * @param: planOverviewParamDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	HttpBaseResponse calcPlanIncome(PlanOverviewParamDTO planOverviewParamDTO);
	
	/**
	 * @Title: calcPortIncome
	 * @Description: 组合的投资收益情况表计算
	 * @param: planOverviewParamDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	HttpBaseResponse calcPortIncome(PlanOverviewParamDTO planOverviewParamDTO);

	/**
	 * @Title: calcPortIncomeTrendPlan
	 * @Description: 各组合收益率走势--计划
	 * @param: planOverviewParamDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	HttpBaseResponse calcPortIncomeTrendPlan(PlanOverviewParamDTO planOverviewParamDTO);

	/**
	 * @Title: calcPortIncomeTrendPort
	 * @Description: 各组合收益率走势--组合
	 * @param: planOverviewParamDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	HttpBaseResponse calcPortIncomeTrendPort(PlanOverviewParamDTO planOverviewParamDTO);

}
