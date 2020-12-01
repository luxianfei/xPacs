/**
 * ******************************************
 * 文件名称: NetAssetsValueExportDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月22日 20:09:52
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.investAnalysis.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.TableChartExportBaseDTO;

/**
 * @ClassName: NetAssetsValueExportDTO
 * @Description: 资产净值导出
 * @author: yt.zhou
 * @date: 2020年11月22日 20:09:52
 */
public class NetAssetsValueExportDTO extends TableChartExportBaseDTO{
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
