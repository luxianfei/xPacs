/**
 * ******************************************
 * 文件名称: SimpleTableExportDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年06月29日 10:56:57
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import java.util.List;

import com.xquant.common.bean.GridPageReq;
import com.xquant.common.bean.bo.TableColumnModel;

/**
 * @ClassName: SimpleTableExportDTO
 * @Description: 表格导出参数接收
 * @author: yt.zhou
 * @date: 2020年06月29日 10:56:57
 */
public class SimpleTableExportDTO<T extends GridPageReq> {
    /**数据查询参数*/
    private T queryReq;
    /**获取数据链接*/
    private String dataPath;
    /**列信息*/
    private List<TableColumnModel> tableColumns;

    public GridPageReq getQueryReq() {
        return queryReq;
    }

    public void setQueryReq(T queryReq) {
        this.queryReq = queryReq;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public List<TableColumnModel> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<TableColumnModel> tableColumns) {
        this.tableColumns = tableColumns;
    }
}
