/**
 * ******************************************
 * 文件名称: AnalysisReq.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月01日 18:02:12
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xquant.xpacs.common.format.DataKeyMapping;
import com.xquant.xpacs.common.format.DataMergeModel;

import java.util.List;

/**
 * @ClassName: AnalysisReq
 * @Description: 分析功能查询参数
 * @author: yt.zhou
 * @date: 2020年07月01日 18:02:12
 */
public class AnalysisBaseDTO {
    private String requestId;
    /**排序字段*/
    private String sortField;
    /**排序类型，asc 顺序，desc 倒序*/
    private String sortType = "asc";
    /**取值数量*/
    private Long dataSize;
    /**过滤条件 a=b*/
    private String filter;
    /**源字段映射新字段*/
    private String keyMappingStr;
    /**数据合并条件*/
    private String merge;
//    private List<DataKeyMapping> keyMappings;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Long getDataSize() {
        return dataSize;
    }

    public void setDataSize(Long dataSize) {
        this.dataSize = dataSize;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKeyMappingStr() {
        return keyMappingStr;
    }

    public void setKeyMappingStr(String keyMappingStr) {
        this.keyMappingStr = keyMappingStr;
    }

    public String getMerge() {
        return merge;
    }

    public void setMerge(String merge) {
        this.merge = merge;
    }

    public List<DataKeyMapping> getKeyMappings() {
        if(this.keyMappingStr != null) {
            return JSONArray.parseArray(this.keyMappingStr,DataKeyMapping.class);
        }
        return null;
    }

    public DataMergeModel getDataMergeModel() {
        if(this.merge != null) {
            return JSONObject.parseObject(this.merge,DataMergeModel.class);
        }
        return null;
    }
}
