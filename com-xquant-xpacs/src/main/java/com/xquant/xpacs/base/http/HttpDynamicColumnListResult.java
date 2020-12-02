/**
 * ******************************************
 * 文件名称: HttpDynamicColumnListResult.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 11:35:03
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.http;

import com.xquant.common.bean.HttpPageListResult;
import com.xquant.common.bean.bo.TableColumnModel;

import java.util.List;

/**
 * @ClassName: HttpDynamicColumnListResult
 * @Description: 动态列的数据结果集
 * @author: yt.zhou
 * @date: 2020年11月03日 11:35:03
 */
public class HttpDynamicColumnListResult extends HttpPageListResult {
    private List<TableColumnModel> columnModelList;

    public List<TableColumnModel> getColumnModelList() {
        return columnModelList;
    }

    public void setColumnModelList(List<TableColumnModel> columnModelList) {
        this.columnModelList = columnModelList;
    }

    public HttpDynamicColumnListResult(List data, List<TableColumnModel> columnModelList) {
        super(data);
        this.columnModelList = columnModelList;
    }

    public HttpDynamicColumnListResult(List data, long count, List<TableColumnModel> columnModelList) {
        super(data, count);
        this.columnModelList = columnModelList;
    }

}
