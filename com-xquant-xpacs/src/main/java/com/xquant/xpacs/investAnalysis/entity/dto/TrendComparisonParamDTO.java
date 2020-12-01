/**
 *******************************************
 * 文件名称: TrendComparisonParamDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 走势对比查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月25日 下午8:33:20
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: TrendComparisonParamDTO
 * @Description: 走势对比查询参数
 * @author: jingru.jiang
 * @date: 2020年11月25日 下午8:33:20
 *
 */
public class TrendComparisonParamDTO extends AnalysisBaseParamDTO {
	/**计算节点*/
	private String nodeId;
	/**基准代码*/
	private String bmCode;
	/**是否计算计划 true-计算*/
	private boolean calcPlan;
	
	public String getNodeId() {
		return nodeId;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getBmCode() {
		return bmCode;
	}
	
	public void setBmCode(String bmCode) {
		this.bmCode = bmCode;
	}
	
	public boolean isCalcPlan() {
		return calcPlan;
	}
	
	public void setCalcPlan(boolean calcPlan) {
		this.calcPlan = calcPlan;
	}
	
}
