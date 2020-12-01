/**
 * ******************************************
 * 文件名称: AnalysisDetailToTreeDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年08月05日 10:59:25
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

/**
 * @ClassName: AnalysisDetailToTreeDTO
 * @Description: 明细按分组转树结构
 * @author: yt.zhou
 * @date: 2020年08月05日 10:59:25
 */
public class AnalysisDetailToTreeDTO extends AnalysisTreeDataDTO {
    //分组字段
    private String groupKey;

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}
