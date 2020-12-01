/**
 * ******************************************
 * 文件名称: TreeTableResultModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 13:40:06
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.common.bean.bo.TreeTableDataModel;

import java.util.List;

/**
 * @ClassName: TreeTableResultModel
 * @Description: 树形表格数据格式化结果
 * @author: yt.zhou
 * @date: 2019年03月27日 13:40:06
 *
 */
public class TreeTableResultModel extends TableResultModel {
    //数据集
    private List<TreeTableDataModel> dataList;

    public List<TreeTableDataModel> getDataList() {
        return dataList;
    }

    public void setDataList(List<TreeTableDataModel> dataList) {
        this.dataList = dataList;
    }

}
