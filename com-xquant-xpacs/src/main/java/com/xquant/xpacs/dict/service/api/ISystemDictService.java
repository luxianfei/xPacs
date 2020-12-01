package com.xquant.xpacs.dict.service.api;

import java.util.List;

import com.xquant.base.dictionary.entity.dto.EnumDictionary;

public interface ISystemDictService {
	public final static String LANG_CN = "zh_CN";
	public final static String SYS_DICT_TYPE = "xpims";

	/**
	 * 根据DICT修改列表中的枚举值
	 * @param list
	 * @param dict 字典二维数组 格式：{{数据中字段名、字典列表中DICT_SUB_TYPE的值}}, 例如{{"org_type","org_type"}}
	 */
	public void changeListOfDict(List<?> list, String dict[][]);
	
	/**
	 * 根据DICT修改对象中的枚举值
	 * @param obj  待修改对象
	 * @param dict 字典二维数组 格式：{{数据中字段名、字典列表中DICT_SUB_TYPE的值}}, 例如{{"org_type","org_type"}}
	 */
	public void changeOfDict(Object obj, String dict[][]);
	
	/**
	 * @Title: getDictList
	 * @Description: 根据类型获取字典表数据
	 * @param: subType   
	 * @return: List<EnumDictionary>   
	 * @throws
	 */
	public List<EnumDictionary> getDictList(String subType);
	
}
