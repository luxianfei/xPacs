/**
 *******************************************
 * 文件名称: CalcObjType.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 计算对象类型-组合/计划
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月12日 下午8:26:00
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.enums;

/**
 * @ClassName: CalcObjType
 * @Description: 计算对象类型-组合/计划
 * @author: jingru.jiang
 * @date: 2020年11月12日 下午8:26:00
 *
 */
public enum CalcObjType {
	port("0", "组合"),
	plan("1", "计划");
	
	private CalcObjType(String code, String name) {
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
	
	public static CalcObjType getEnumByCode(String code) {
		if (code == null) {return null;}
		for (CalcObjType item : values()) {
			if(item.getCode().equals(code)) {
				return item;
			}
		}
		return port;
	}
}
