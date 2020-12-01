/**
 * ******************************************
 * 文件名称: TableResultModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 15:55:46
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.common.bean.bo.TableColumnModel;
import com.xquant.xpacs.common.format.BaseResultModel;

import java.util.List;

/**
 * @ClassName: TableResultModel
 * @Description: 表格数据
 * @author: yt.zhou
 * @date: 2019年03月27日 15:55:46
 *
 */
public class TableResultModel extends BaseResultModel {
    //表格列表
    private List<TableColumnModel> columnModelList;
    //列数组
    private String[] fields;

    public List<TableColumnModel> getColumnModelList() {
        return columnModelList;
    }

    public void setColumnModelList(List<TableColumnModel> columnModelList) {
        this.columnModelList = columnModelList;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
