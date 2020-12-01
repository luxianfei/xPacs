/**
 * ******************************************
 * 文件名称: DataMergeModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月10日 09:34:00
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

import java.util.List;

/**
 * @ClassName: DataMergeModel
 * @Description: 数据合并
 * @author: yt.zhou
 * @date: 2020年07月10日 09:34:00
 */
public class DataMergeModel {
    /**合并维度*/
    private List<String> dimension;
    /**合并字段*/
    private List<String> dataField;
    /**维度不存在的数据合并依据*/
    private List<InexistenceDimenson> inexistenceDimensons;
    /**
     * Title: 行转列
     * Description:    rowToCloumn:['gbPTotalIncome','endDate']
     *
     * @param null      原 行数据：gbPTotalIncome  转换 列数据endDate
     * @return: 原 3条不同时间数据 和成1条不同时间数据
     * endDate: "2020-05-11"
     * gbPTotalIncome: 0.056
     * pName: "咕咕咕吧"
     * portCode: "aaaa"
     * <p>
     * 2018-05-11: 0.0453
     * 2019-05-11: 0.0123
     * 2020-05-11: 0.0023
     * gbPTotalIncome: 0.0023
     * pName: "演示计划_工银瑞信组合"
     * portCode: "I54Z0042"
     * @throws :
     */
    private List<String> rowToCloumn;

    public List<String> getDimension() {
        return dimension;
    }

    public void setDimension(List<String> dimension) {
        this.dimension = dimension;
    }

    public List<String> getDataField() {
        return dataField;
    }

    public void setDataField(List<String> dataField) {
        this.dataField = dataField;
    }

    public List<InexistenceDimenson> getInexistenceDimensons() {
        return inexistenceDimensons;
    }

    public void setInexistenceDimensons(List<InexistenceDimenson> inexistenceDimensons) {
        this.inexistenceDimensons = inexistenceDimensons;
    }

    public List<String> getRowToCloumn() {
        return rowToCloumn;
    }

    public void setRowToCloumn(List<String> rowToCloumn) {
        this.rowToCloumn = rowToCloumn;
    }

    public static class InexistenceDimenson {
        /**源数据合并条件*/
        private List<InexistenceModel> source;
        /**目标数据合并条件*/
        private List<InexistenceModel> target;

        public List<InexistenceModel> getSource() {
            return source;
        }

        public void setSource(List<InexistenceModel> source) {
            this.source = source;
        }

        public List<InexistenceModel> getTarget() {
            return target;
        }

        public void setTarget(List<InexistenceModel> target) {
            this.target = target;
        }
    }

    public static class InexistenceModel {
        /**对应数据map中的key*/
        private String key;
        /**
         * 当value为空时，则直接根据source的value与target的value一致，进行合并;
         * 当不为空时，则source的value等于该value,并且target的value等于value
         */
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
