package com.xquant.xpacs.analysis.service.api;

import com.xquant.xpims.trep.dto.FieldFormatDTO;

import java.util.Map;

public interface IFieldFormatService {

	Map<String, FieldFormatDTO> getAllFieldFormats();
}
