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
 * 开发时间: 2019年03月27日 13:37:08
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TableResultModel
 * @Description: 表格数据格式化后的数据模型
 * @author: yt.zhou
 * @date: 2019年03月27日 13:37:08
 *
 */
public class SimpleTableResultModel extends TableResultModel {
    //数据集
    private List<Map<String,Object>> dataList;
    //数据量，用于分页
    private Integer total;

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
