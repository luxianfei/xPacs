/**
 *******************************************
 * 文件名称: InvestIncomeExportDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 投资收益导出
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月23日 下午1:15:25
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.TableChartExportBaseDTO;

/**
 * @ClassName: InvestIncomeExportDTO
 * @Description: 投资收益导出
 * @author: jingru.jiang
 * @date: 2020年11月23日 下午1:15:25
 *
 */
public class InvestIncomeExportDTO extends TableChartExportBaseDTO{
	/**对象类型  0组合  1计划*/
    private String calcObjType;
    /**开始日期*/
    private String begDate;
    /**结束日期*/
    private String endDate;
    /**组合代码*/
    private String portCode;
    
	public String getCalcObjType() {
		return calcObjType;
	}
	public void setCalcObjType(String calcObjType) {
		this.calcObjType = calcObjType;
	}
	public String getBegDate() {
		return begDate;
	}
	public void setBegDate(String begDate) {
		this.begDate = begDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
}
