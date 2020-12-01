package com.xquant.xpacs.dict.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xquant.base.dictionary.entity.dto.EnumDictionary;
import com.xquant.base.dictionary.service.api.EnumDictionaryExternalService;
import com.xquant.common.exception.BusinessServiceException;
import com.xquant.xpacs.dict.service.api.ISystemDictService;

@Service
public class SystemDictServiceImpl implements ISystemDictService {
	@Autowired
	private EnumDictionaryExternalService enumDictionaryExternalService;

	@Override
	public void changeListOfDict(List<?> list, String[][] dict) {
		try	{
			for (Object obj : list) {
				for (int s = 0; s < dict.length; s++) {
					String fieldName = dict[s][0].trim();
					String subType = dict[s][1].trim();
					if (subType == null) {
						continue;
					}
					
					Map<String, EnumDictionary> dictMap = enumDictionaryExternalService.query(LANG_CN, SYS_DICT_TYPE, subType);
					Object dictKey = PropertyUtils.getProperty(obj, fieldName);
					if (dictKey != null && dictMap.get(dictKey) != null) {
						EnumDictionary value = dictMap.get(dictKey);
						
						if (PropertyUtils.isWriteable(obj, fieldName)) {
							PropertyUtils.setProperty(obj, fieldName, value.getDictValue());
						}
					}
				}
			}
		} catch (Exception e){
			throw new BusinessServiceException(e);
		}
	}

	@Override
	public void changeOfDict(Object obj, String[][] dict) {
		try	{
			for (int s = 0; s < dict.length; s++) {
				String fieldName = dict[s][0].trim();
				String subType = dict[s][1].trim();
				if (subType == null) { continue; }
				
				Map<String, EnumDictionary> dictMap = enumDictionaryExternalService.query(LANG_CN, SYS_DICT_TYPE, subType);
				Object dictKey = PropertyUtils.getProperty(obj, fieldName);
				if (dictKey != null && dictMap != null && dictMap.get(dictKey) != null) {
					EnumDictionary value = dictMap.get(dictKey);
					
					if (PropertyUtils.isWriteable(obj, fieldName)) {
						PropertyUtils.setProperty(obj, fieldName, value.getDictValue());
					}
				}
			}
		} catch (Exception e){
			throw new BusinessServiceException(e);
		}
	}

	/**
	 * <p>Title: getDictList</p>
	 * <p>Description: 根据类型获取字典表数据</p>
	 * @param subType
	 * @return
	 * @see com.xquant.xpims.common.dict.service.api.ISystemDictService#getDictList(java.lang.String)
	 */
	@Override
	public List<EnumDictionary> getDictList(String subType) {
		List<EnumDictionary> list = new ArrayList<EnumDictionary>();
		Map<String, EnumDictionary> dictMap = enumDictionaryExternalService.query(LANG_CN, SYS_DICT_TYPE, subType);
		for (Map.Entry<String, EnumDictionary> entry : dictMap.entrySet()) {
			list.add(entry.getValue());
		}
		List<EnumDictionary> sortedList = list.stream().sorted(Comparator.comparing(EnumDictionary::getDictKeyOrder)).collect(Collectors.toList());
		return sortedList;
	}

}
