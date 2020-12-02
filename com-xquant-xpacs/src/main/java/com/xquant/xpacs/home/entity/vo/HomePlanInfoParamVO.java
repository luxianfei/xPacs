/**
 *******************************************
 * 文件名称: HomePlanInfoParamVO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 首页计划信息查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月30日 下午4:47:40
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.home.entity.vo;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: HomePlanInfoParamVO
 * @Description: 首页计划信息查询参数
 * @author: jingru.jiang
 * @date: 2020年11月30日 下午4:47:40
 *
 */
public class HomePlanInfoParamVO extends AnalysisBaseParamDTO {
	/**基准代码*/
	private String bmCode;
	
	public String getBmCode() {
		return bmCode;
	}
	
	public void setBmCode(String bmCode) {
		this.bmCode = bmCode;
	}
}
