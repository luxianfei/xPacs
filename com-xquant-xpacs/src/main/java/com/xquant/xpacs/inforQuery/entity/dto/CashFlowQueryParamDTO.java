/**
 *******************************************
 * 文件名称: CashFlowQueryParamDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 现金流查询
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 现金流查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月5日 上午9:34:15
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.inforQuery.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: CashFlowQueryParamDTO
 * @Description: 现金流查询参数
 * @author: jingru.jiang
 * @date: 2020年11月5日 上午9:34:15
 *
 */
public class CashFlowQueryParamDTO extends AnalysisBaseParamDTO {
	/**统计频率  日频-D,月频-M,季频-Q,年频-Y*/
    private String freqType;

	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}
    
    
}
