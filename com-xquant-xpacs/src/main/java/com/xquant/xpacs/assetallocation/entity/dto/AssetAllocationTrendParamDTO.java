/**
 * ******************************************
 * 文件名称: AssetAllocationTrendParamDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月12日 11:23:20
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.assetallocation.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: AssetAllocationTrendParamDTO
 * @Description: 资产配置走势
 * @author: yt.zhou
 * @date: 2020年11月12日 11:23:20
 */
public class AssetAllocationTrendParamDTO extends AnalysisBaseParamDTO {
    /**分组节点Id,权益类，固收类等*/
    private String nodeId;
    /**分组计算层级*/
    private String nodeCalcLevel = "local";
    /**是否计算计划*/
    private Boolean calcPlan;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeCalcLevel() {
        return nodeCalcLevel;
    }

    public void setNodeCalcLevel(String nodeCalcLevel) {
        this.nodeCalcLevel = nodeCalcLevel;
    }

    public Boolean getCalcPlan() {
        return calcPlan;
    }

    public void setCalcPlan(Boolean calcPlan) {
        this.calcPlan = calcPlan;
    }
}
