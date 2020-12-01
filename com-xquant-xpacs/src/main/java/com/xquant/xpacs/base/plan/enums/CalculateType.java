/**
 *******************************************
 * 文件名称: CalculateType.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月10日 下午5:01:18
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.base.plan.enums;

/**
 * @ClassName: CalculateType
 * @Description: 计划数据来源枚举
 * @author: jingru.jiang
 * @date: 2020年11月10日 下午5:01:18
 *
 */
public enum CalculateType {
	complex("0", "复合"),
	directCalc("1", "导入");
	
	private CalculateType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static boolean isValid(String code) {
		return true;
	}
	
	public static CalculateType getEnumByCode(String code) {
		if (code == null) {return null;}
		for (CalculateType item : values()) {
			if(item.getCode().equals(code)) {
				return item;
			}
		}
		return complex;
	}
}
