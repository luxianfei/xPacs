/**
 * ******************************************
 * 文件名称: TableTreeDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V5.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2019年01月31日 14:12:04
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.common.bean.bo.TreeTableDataModel;
import com.xquant.xpacs.common.format.BaseResultModel;
import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;
import com.xquant.xpacs.common.format.model.AnalysisTreeDataDTO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TableTreeDataFormat
 * @Description: 表格数据格式化
 * @author: yt.zhou
 * @date: 2019年01月31日 14:12:04
 *
 */
public class TreeTableDataFormat extends AbstractTableDataFormat {

    private TreeTableResultModel doFormatData(List<Map<String, Object>> list, AnalysisTreeDataDTO treeDataDTO) {
        //数据集，构建树形结构的数据集
        List<TreeTableDataModel> dataList = TreeTableBuildTreeUtil.buildTree(list,treeDataDTO.getNodeKey(),treeDataDTO.getParentNodeKey());
        TreeTableResultModel model = new TreeTableResultModel();
        model.setDataList(dataList);
        return model;
    }

    @Override
    protected BaseResultModel doComponentFormat(List<Map<String, Object>> list, AnalysisBaseDTO analysisBaseDTO) {
        return this.doFormatData(list, (AnalysisTreeDataDTO) analysisBaseDTO);
    }
}
