/**
 * ******************************************
 * 文件名称: TableColumnModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 11:52:21
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import java.util.List;

/**
 * @ClassName: TableColumnModel
 * @Description: 表格列，对应EXTJS column的属性
 * @author: yt.zhou
 * @date: 2019年03月27日 11:52:21
 *
 */
public class TableColumnModel implements Cloneable {
    //表头名
    private String label;
    //列取值
    private String prop;
    //列宽
    private double width;
    //列类型
    private ColumnType cType;
    //列位置
    private String align;
    //列排序
    private int sortIndex;
    //隐藏/显示
    private boolean hidden;
    //子列
    private List<TableColumnModel> columns;
    //Id
    private String conUuId;
    //父Id,用于做多重表头的处理
    private String parentConUuId;
    //渲染
    private String renderer;

    private Format format;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public ColumnType getcType() {
        return cType;
    }

    public void setcType(ColumnType cType) {
        this.cType = cType;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public List<TableColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<TableColumnModel> columns) {
        this.columns = columns;
    }

    public String getConUuId() {
        return conUuId;
    }

    public void setConUuId(String conUuId) {
        this.conUuId = conUuId;
    }

    public String getParentConUuId() {
        return parentConUuId;
    }

    public void setParentConUuId(String parentConUuId) {
        this.parentConUuId = parentConUuId;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @author: yt.zhou
     * @date: 2020年06月29日 10:07
     * @description: 数据格式化
     */
    public static class Format {
        private String idxUnitName;
        private Double idxUnitRate;
        private String idxFormat;

        public String getIdxUnitName() {
            return idxUnitName;
        }

        public void setIdxUnitName(String idxUnitName) {
            this.idxUnitName = idxUnitName;
        }

        public Double getIdxUnitRate() {
            return idxUnitRate;
        }

        public void setIdxUnitRate(Double idxUnitRate) {
            this.idxUnitRate = idxUnitRate;
        }

        public String getIdxFormat() {
            return idxFormat;
        }

        public void setIdxFormat(String idxFormat) {
            this.idxFormat = idxFormat;
        }
    }

    /**
     * @author: yt.zhou
     * @date: 2020年06月29日 10:07
     * @description: 列类型枚举
     */
    public enum ColumnType{
        //字符串
        STRING,
        //数字
        NUMBER,
        //序号
        INDEX,
        //树
        TREE
    }
}
