/**
 *******************************************
 * 文件名称: TransationOverviewParamDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 大类资产交易概况查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月18日 上午10:46:56
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.assetallocation.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: TransationOverviewParamDTO
 * @Description: 大类资产交易概况查询参数
 * @author: jingru.jiang
 * @date: 2020年11月18日 上午10:46:56
 *
 */
public class TransationOverviewParamDTO extends AnalysisBaseParamDTO {
	/**对象类型*/
    private String calcObjType;

	public String getCalcObjType() {
		return calcObjType;
	}

	public void setCalcObjType(String calcObjType) {
		this.calcObjType = calcObjType;
	}
	
}
