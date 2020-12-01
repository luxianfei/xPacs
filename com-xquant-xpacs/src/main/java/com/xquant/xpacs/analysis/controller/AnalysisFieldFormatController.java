/**
 * ******************************************
 * 文件名称: AnalysisFieldFormatController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月12日 17:17:03
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.controller;

import com.xquant.xpacs.analysis.entity.bo.FieldFormatBO;
import com.xquant.xpacs.analysis.service.api.IFieldFormatService;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpims.trep.dto.FieldFormatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AnalysisFieldFormatController
 * @Description: 指标格式化
 * @author: yt.zhou
 * @date: 2020年11月12日 17:17:03
 */
@RestController
@RequestMapping("/analysisFieldFormat")
public class AnalysisFieldFormatController {

    @Autowired
    private IFieldFormatService fieldFormatService;

    @RequestMapping("/getAllFieldFormats")
    public HttpBaseResponse getAllFieldFormats() {
        Map<String, FieldFormatDTO> fieldMap = fieldFormatService.getAllFieldFormats();

        FieldFormatBO fieldFormatBO = new FieldFormatBO(fieldMap);
        return HttpBaseResponseUtil.getSuccessResponse(fieldFormatBO);
    }
}
