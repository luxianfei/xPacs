/**
 * ******************************************
 * 文件名称: AnalysisGridPageReq.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年06月29日 11:25:24
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import com.xquant.common.bean.bo.TableColumnModel;

import java.util.List;

/**
 * @ClassName: AnalysisGridPageReq
 * @Description: 分析功能表格查询参数
 * @author: yt.zhou
 * @date: 2020年06月29日 11:25:24
 */
public class AnalysisGridPageDTO extends AnalysisBaseDTO {
    private Integer pageSize;
    private Integer pageNum;
    //列信息，导出时需要
    private List<TableColumnModel> tableColumns;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<TableColumnModel> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<TableColumnModel> tableColumns) {
        this.tableColumns = tableColumns;
    }
}
