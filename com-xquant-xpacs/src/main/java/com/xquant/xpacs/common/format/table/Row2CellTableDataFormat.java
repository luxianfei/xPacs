/**
 * ******************************************
 * 文件名称: Row2CellTableDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年04月12日 09:15:56
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.common.bean.bo.TableColumnModel;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.StringUtils;
import com.xquant.xpacs.common.format.BaseResultModel;
import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;
import com.xquant.xpacs.common.format.model.AnalysisRow2CellDTO;
import org.springframework.beans.BeanUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName: Row2CellTableDataFormat
 * @Description: 行转列表格数据处理
 * @author: yt.zhou
 * @date: 2019年04月12日 09:15:56
 *
 */
public class Row2CellTableDataFormat extends AbstractTableDataFormat{

    private SimpleTableResultModel formatData(List<Map<String, Object>> list, AnalysisRow2CellDTO row2CellDTO) {

        SimpleTableResultModel resultModel = (SimpleTableResultModel) this.row2Cell(list,row2CellDTO);

        List<Map<String,Object>> resultList = resultModel.getDataList();

        List<Map<String,Object>> pageList = super.getPageList(resultList,row2CellDTO);

        resultModel.setDataList(pageList);
        resultModel.setTotal(resultList.size());
        return resultModel;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年04月12日 17:58
     * @description: 行转列
     * @param list              数据集
     * @param row2CellDTO       参数
     * @return com.xquant.product.xqa.app.common.dataFormat.table.model.TableResultModel
     *
     */
    public TableResultModel row2Cell(List<Map<String, Object>> list, AnalysisRow2CellDTO row2CellDTO){

        List<String> smRowKeys = row2CellDTO.getSameRowFlagKey();
        List<String> uniqueKeys = row2CellDTO.getUniqueKey();
        List<String> parentColumnKeys = row2CellDTO.getParentColumnKey();

        List<TableColumnModel> columnModelList = row2CellDTO.getTableColumns();


        Set<String> originalKeys = this.getOriginalKeys(smRowKeys);


        //父列名称与父列对象的对应关系
        Map<String,TableColumnModel> parentColumnMap = new HashMap<>();
        //数据集
        List<Map<String,Object>> dataList = new ArrayList<>();
        Set<String> fields = new HashSet<>();

        //表格列表集合
        List<TableColumnModel> resultColumnList = new ArrayList<>();
        Map<String,Map<String,Object>> rowKeyMap = new HashMap<>();
        for(Map<String,Object> data : list) {
            String uks = "";
            for(String uk : uniqueKeys){
                if(!"".equals(uks)) {
                    uks += "_";
                }
                uks += data.get(uk);
            }

            //获取相同行标识的取值，当该值相同时，放置于同一行
            String val = "";
            for(String sm : smRowKeys) {
                val += "_" + StringUtils.defaultIfNull(data.get(sm),"");
            }

            Map<String,Object> rowResult = rowKeyMap.get(val);
            if(rowResult == null) {
                rowResult = new HashMap();
                rowResult.put("sameRowKey",val);
                rowKeyMap.put(val,rowResult);

                dataList.add(rowResult);
            }

            //封装表头
            String parentColumnName = "";
            for(String columnKey : parentColumnKeys) {
                if(!"".equals(parentColumnName)) {
                    parentColumnName += ",";
                }

                parentColumnName += data.get(columnKey);
            }

            for(String oKey : originalKeys) {
                rowResult.put(oKey,data.get(oKey));
            }

            //最终返回的结果为拼接后的key+
            for(TableColumnModel model : columnModelList) {
                //将需要显示的字段，通过数据唯一键封装成新的字段
                String dataIndex;
                if(originalKeys.contains(model.getProp())) {
                    //如果是同行标识的列，直接用该列即可，不需要拼接数据的唯一key,做为父列
                    dataIndex = model.getProp();
                    rowResult.put(dataIndex,data.get(model.getProp()));

                    if(fields.contains(dataIndex)) {
                        continue;
                    }
                    fields.add(dataIndex);

                    resultColumnList.add(model);
                } else {
                    TableColumnModel pColumnModel = parentColumnMap.get(parentColumnName);

                    if(pColumnModel == null) {
                        //父列
                        pColumnModel = new TableColumnModel();
                        pColumnModel.setLabel(parentColumnName);
                        pColumnModel.setAlign("center");
                        parentColumnMap.put(parentColumnName,pColumnModel);

                        pColumnModel.setColumns(new ArrayList<>());

                        resultColumnList.add(pColumnModel);
                    }

                    dataIndex = uks + "_" + model.getProp();

                    Object dataVal = data.get(model.getProp());

                    /*if(model.getcType() == TableColumnModel.ColumnType.NUMBER) {
                        //数字类型
                        Double dv = NumberUtils.convertToDouble(dataVal, null);
                        if(dv != null) {
                            dataVal = dv;
                            //格式化数字类型
                            TableColumnModel.Format format = model.getFormat();
                            if(format != null) {
                                if(format.getIdxUnitRate() != null) {
                                    dataVal = dv * format.getIdxUnitRate();
                                }

                                //小数位，千分位格式化
                                String parttern = format.getIdxFormat();
                                if(parttern != null) {
                                    DecimalFormat decimalFormat = new DecimalFormat(parttern);
                                    dataVal = decimalFormat.format(dataVal);
                                }
                            }
                        }
                    }*/

                    rowResult.put(dataIndex,dataVal);

                    if(fields.contains(dataIndex)) {
                    	continue;
                    }
                    fields.add(dataIndex);
                    //子列
                    List<TableColumnModel> sonColumns = pColumnModel.getColumns();


                    TableColumnModel targetColumn = new TableColumnModel();
                    BeanUtils.copyProperties(model, targetColumn);

                    targetColumn.setProp(dataIndex);
                    sonColumns.add(targetColumn);
                }

            }

        }

        resultColumnList = singleColumnConvert(resultColumnList);
        //结果对象
        SimpleTableResultModel resultModel = new SimpleTableResultModel();
        resultModel.setDataList(dataList);
        resultModel.setColumnModelList(resultColumnList);
//        resultModel.setFields(fields.toArray(new String[]{}));
        return resultModel;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年04月12日 18:33
     * @description:  原始的key，即不与唯一键做拼接的key
     * @return java.util.Set<java.lang.String>
     *
     */
    public Set<String> getOriginalKeys(String sameRowFlagKey){
        Set<String> originalKeys = new HashSet<>();
        String[] srfk = sameRowFlagKey.split(",");
        for(String s : srfk) {
            originalKeys.add(s);
        }
        return originalKeys;
    }

    public Set<String> getOriginalKeys(List<String> sameRowFlagKeys){
        Set<String> originalKeys = new HashSet<>();
        for(String s : sameRowFlagKeys) {
            originalKeys.add(s);
        }
        return originalKeys;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年07月08日 17:12
     * @description:    如果子列只有一列，那么就不需要用父子关系
     * @param columnModelList
     * @return java.util.List<com.xquant.product.xqa.app.common.dataFormat.table.model.TableColumnModel>
     *
     */
    private List<TableColumnModel> singleColumnConvert(List<TableColumnModel> columnModelList) {
        List<TableColumnModel> columnModels = new ArrayList<>();
        for(TableColumnModel columnModel : columnModelList) {
            List<TableColumnModel> subColumns = columnModel.getColumns();
            TableColumnModel tableColumnModel;
            if(subColumns != null && subColumns.size() == 1) {
                //如果子列只有一列,则直接将子列设置为该列，将列的名称设置为父列的名称
                tableColumnModel = subColumns.get(0);
                tableColumnModel.setLabel(columnModel.getLabel());
            } else {
                tableColumnModel = columnModel;
            }
            columnModels.add(tableColumnModel);
        }
        return columnModels;
    }

    @Override
    protected BaseResultModel doComponentFormat(List<Map<String, Object>> list, AnalysisBaseDTO analysisBaseDTO) {
        return this.formatData(list,(AnalysisRow2CellDTO)analysisBaseDTO);
    }
}
