/**
 *******************************************
 * 文件名称: RiskIndexParamDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 绩效指标查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月24日 下午5:11:23
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: RiskIndexParamDTO
 * @Description: 绩效指标查询参数
 * @author: jingru.jiang
 * @date: 2020年11月24日 下午5:11:23
 *
 */
public class RiskIndexParamDTO extends AnalysisBaseParamDTO {
	/**样本收益率方案：0=自然日收益率; 1=交易日累计收益率*/
    private String cumuMode = "1";

	public String getCumuMode() {
		return cumuMode;
	}

	public void setCumuMode(String cumuMode) {
		this.cumuMode = cumuMode;
	}
    
}
