/**
 * ******************************************
 * 文件名称: DataFormatModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年06月19日 13:26:13
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

/**
 * @ClassName: DataFormatModel
 * @Description: 数据处理模型
 * @author: yt.zhou
 * @date: 2020年06月19日 13:26:13
 */
public class DataFormatModel {
    /**排序字段*/
    private String sortField;
    /**排序类型，asc 顺序，desc 倒序*/
    private String sortType = "asc";
    /**取值数量*/
    private Long dataSize;
    /**分页 页码*/
    private Integer pageNum;
    /**分页 每页显示数量*/
    private Integer pageSize;

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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
