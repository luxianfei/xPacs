/**
 * ******************************************
 * 文件名称: SimpleTableDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 10:54:03
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;
import com.xquant.xpacs.common.format.model.AnalysisGridPageDTO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SimpleTableDataFormat
 * @Description: 简单表格数据
 * @author: yt.zhou
 * @date: 2019年03月27日 10:54:03
 *
 */
public class SimpleTableDataFormat extends AbstractTableDataFormat {
    /**
     * @author: yt.zhou
     * @date: 2019年03月27日 10:54
     * @description: 简单表格数据处理，直接返回数据
     * @param dataList
     * @param baseDTO
     * @return java.lang.Object
     *
     */
    @Override
    protected SimpleTableResultModel doComponentFormat(List<Map<String, Object>> dataList, AnalysisBaseDTO baseDTO) {
        SimpleTableResultModel model = new SimpleTableResultModel();

        List<Map<String,Object>> pageList = super.getPageList(dataList,(AnalysisGridPageDTO) baseDTO);

        model.setDataList(pageList);
        model.setTotal(dataList.size());
        return model;
    }
}
