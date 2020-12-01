/**
 * ******************************************
 * 文件名称: AnalysisModifyFieldGridDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年08月28日 09:44:29
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @ClassName: AnalysisModifyFieldGridDTO
 * @Description: 修改数据表格
 * @author: yt.zhou
 * @date: 2020年08月28日 09:44:29
 */
public class AnalysisModifyFieldChartDTO extends AnalysisChartDTO {
    /**
     * @author: yt.zhou
     * @date: 2020年08月28日 09:39
     * @description: 修改字段的模型
     * [
     *    {
     *        requestId: requestId,//缓存数据id
     *        modifyKeyAndValues:[
     *            {key:'pName',value:'固收型组合'},//key是要修改的字段，value是要修改的值
     *            {key:'count',value:3}
     *        ]
     *    }
     * ]
     *
     */
    private String modifyFields;

    public String getModifyFields() {
        return modifyFields;
    }

    public void setModifyFields(String modifyFields) {
        this.modifyFields = modifyFields;
    }

    public List<AnalysisModifyFieldDTO.ModifyFieldModel> getModifyFieldModes() {
        return JSONArray.parseArray(this.modifyFields,AnalysisModifyFieldDTO.ModifyFieldModel.class);
    }
}
