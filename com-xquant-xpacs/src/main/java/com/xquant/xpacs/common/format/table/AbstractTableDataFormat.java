/**
 * ******************************************
 * 文件名称: AbstractTableDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 11:51:13
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.common.bean.bo.TableColumnModel;
import com.xquant.xpacs.common.format.AbstractDataFormat;
import com.xquant.xpacs.common.format.model.AnalysisGridPageDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AbstractTableDataFormat
 * @Description: 表格数据处理
 * @author: yt.zhou
 * @date: 2019年03月27日 11:51:13
 *
 */
public abstract class AbstractTableDataFormat extends AbstractDataFormat {

    /**
     * @author: yt.zhou
     * @date: 2019年03月27日 15:57
     * @description: 得到字段集合
     * @param columnModelList
     * @return java.lang.String[]
     *
     */
    public String[] getFields(List<TableColumnModel> columnModelList) {
        List<String> fields = getFieldsList(columnModelList);
        return fields.toArray(new String[]{});
    }

    private List<String> getFieldsList(List<TableColumnModel> columnModelList) {
        List<String> fields = new ArrayList<>();
        for(TableColumnModel columnModel : columnModelList) {
            if(columnModel.getProp() != null) {
                fields.add(columnModel.getProp());
            }
            if(columnModel.getColumns() != null ) {
                fields.addAll(getFieldsList(columnModel.getColumns()));
            }
        }
        return fields;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年08月04日 15:42
     * @description:    获取分页数据
     * @param dataList
     * @param gridPageDTO
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    public List<Map<String,Object>> getPageList(List<Map<String, Object>> dataList,AnalysisGridPageDTO gridPageDTO) {
        //页码
        Integer pageNumber = gridPageDTO.getPageNum();
        //每页数量
        Integer pageSize = gridPageDTO.getPageSize();

        List<Map<String,Object>> pageList;
        if(pageNumber == null || pageNumber == 0) {
            //表示不分页
            pageList = dataList;
        } else {
            int fromIndex = (pageNumber - 1) * pageSize;
            int toIndex = pageNumber * pageSize;

            fromIndex = Math.min(fromIndex,dataList.size());
            toIndex = Math.min(toIndex,dataList.size());

            pageList = dataList.subList(fromIndex,toIndex);
        }

        return pageList;
    }

}
