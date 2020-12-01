/**
 * ******************************************
 * 文件名称: AnalysisRow2CellDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年08月04日 15:24:28
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import java.util.List;

/**
 * @ClassName: AnalysisRow2CellDTO
 * @Description: 行列转换
 * @author: yt.zhou
 * @date: 2020年08月04日 15:24:28
 */
public class AnalysisRow2CellDTO extends AnalysisGridPageDTO {
    //同一行的标志，即通过什么字段判断两条数据是同一行的，比如日期，不同的证券，相同日期的数据转换为同一行数据
    private List<String> sameRowFlagKey;
    //数据唯一键，每行数据通过哪些字段标识唯一，同一行的数据转换后根据该属性来判断哪些字段区分不同列
    private List<String> uniqueKey;
    //父列名称取值
    private List<String> parentColumnKey;

    public List<String> getSameRowFlagKey() {
        return sameRowFlagKey;
    }

    public void setSameRowFlagKey(List<String> sameRowFlagKey) {
        this.sameRowFlagKey = sameRowFlagKey;
    }

    public List<String> getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(List<String> uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public List<String> getParentColumnKey() {
        return parentColumnKey;
    }

    public void setParentColumnKey(List<String> parentColumnKey) {
        this.parentColumnKey = parentColumnKey;
    }
}
