package com.xquant.xpacs.analysis.service.impl;

import com.xquant.xpacs.analysis.service.api.IFieldFormatService;
import com.xquant.xpims.trep.dto.FieldFormatDTO;
import com.xquant.xpims.trep.mapper.ext.FieldFormatExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yt.zhou
 * @date: 2020年11月12日 17:20
 * @description: 指标格式化
 *
 */
@Component
public class FieldFormatServiceImpl implements IFieldFormatService {
	@Autowired
	private FieldFormatExtMapper fieldFormatExtMapper;

	@Override
	public Map<String, FieldFormatDTO> getAllFieldFormats() {
		Map<String, FieldFormatDTO> allFormatMap = new HashMap<>();
		// 引擎指标格式
		Map<String, FieldFormatDTO> fieldFormats = convertList2Map(fieldFormatExtMapper.getFieldFormat());
		allFormatMap.putAll(fieldFormats);
		// 固化指标格式
		Map<String, FieldFormatDTO> ldFieldFormats = convertList2Map(fieldFormatExtMapper.getLdFieldFormat());
		allFormatMap.putAll(ldFieldFormats);
		// 表达式指标格式
		Map<String, FieldFormatDTO> formulaFieldFormats = convertList2Map(fieldFormatExtMapper.getFormulaFieldFormat());
		allFormatMap.putAll(formulaFieldFormats);
		return allFormatMap;
	}

	private Map<String, FieldFormatDTO> convertList2Map(List<FieldFormatDTO> fieldFormats) {
		Map<String, FieldFormatDTO> formatMap = new HashMap<>();
		if (fieldFormats == null) {
			return formatMap;
		}
		for (FieldFormatDTO format : fieldFormats) {
			formatMap.put(format.getIdxCode(), format);
		}
		return formatMap;
	}
}
