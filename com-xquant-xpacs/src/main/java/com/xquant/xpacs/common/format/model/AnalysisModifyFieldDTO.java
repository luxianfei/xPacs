/**
 * ******************************************
 * 文件名称: AnalysisModifyFieldDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年08月27日 16:02:00
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName: AnalysisModifyFieldDTO
 * @Description:   修改缓存数据的字段
 * @author: yt.zhou
 * @date: 2020年08月27日 16:02:00
 */
public class AnalysisModifyFieldDTO extends AnalysisBaseDTO {
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

    public List<ModifyFieldModel> getModifyFieldModes() {
        return JSONArray.parseArray(this.modifyFields,ModifyFieldModel.class);
    }

    public static class ModifyFieldModel {
        //缓存数据
        private String requestId;
        private List<ModifyKeyAndValue> modifyKeyAndValues;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public List<ModifyKeyAndValue> getModifyKeyAndValues() {
            return modifyKeyAndValues;
        }

        public void setModifyKeyAndValues(List<ModifyKeyAndValue> modifyKeyAndValues) {
            this.modifyKeyAndValues = modifyKeyAndValues;
        }
    }

    public static class ModifyKeyAndValue {
        //需要设置的key
        private String key;
        //对应的value
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
