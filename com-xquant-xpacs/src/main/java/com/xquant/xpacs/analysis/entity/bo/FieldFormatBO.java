/**
 * ******************************************
 * 文件名称: FieldFormatBO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月12日 17:25:14
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.entity.bo;

import com.xquant.xpims.trep.dto.FieldFormatDTO;

import java.util.Map;

/**
 * @ClassName: FieldFormatBO
 * @Description: 系统指标格式化
 * @author: yt.zhou
 * @date: 2020年11月12日 17:25:14
 */
public class FieldFormatBO {

    /**
     * @author: yt.zhou
     * @date: 2020年11月12日 17:25
     * @description: 指标格式化，key为指标代码，value为指标格式化格式
     *
     */
    private Map<String, FieldFormatDTO> fieldFormats;

    public Map<String, FieldFormatDTO> getFieldFormats() {
        return fieldFormats;
    }

    public void setFieldFormats(Map<String, FieldFormatDTO> fieldFormats) {
        this.fieldFormats = fieldFormats;
    }

    public FieldFormatBO(Map<String, FieldFormatDTO> fieldFormats) {
        this.fieldFormats = fieldFormats;
    }
}
