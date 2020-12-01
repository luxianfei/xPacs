/**
 * ******************************************
 * 文件名称: DataFilterModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 09:20:59
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: DataFilterModel
 * @Description: 数据过滤
 * @author: yt.zhou
 * @date: 2019年03月27日 09:20:59
 *
 */
public class DataFilterModel {
    //筛选类型，ALL/FILTER，all不筛选，filter表示需要筛选
    private String fType;
    //如果fType=filter，这个表示筛选条件
    //例子：[{nodeName:'股票'},{nodeName:'债券'}]
    private List<Map<String,String>> condition;
    //筛选条件，ConditionModel表示一个条件，a=b，List<ConditionModel>表示一组条件，条件之前为且的关系，即满足所有条件
    private List<List<ConditionModel>> conditionList;

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public List<Map<String, String>> getCondition() {
        return condition;
    }

    public void setCondition(List<Map<String, String>> condition) {
        this.condition = condition;
    }

    public List<List<ConditionModel>> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<List<ConditionModel>> conditionList) {
        this.conditionList = conditionList;
    }
}
