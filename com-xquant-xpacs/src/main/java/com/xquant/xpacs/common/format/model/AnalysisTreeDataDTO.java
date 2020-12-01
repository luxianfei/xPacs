/**
 * ******************************************
 * 文件名称: AnalysisTreeDataDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月07日 09:13:06
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

/**
 * @ClassName: AnalysisTreeDataDTO
 * @Description: 树形结构数据参数接收
 * @author: yt.zhou
 * @date: 2020年07月07日 09:13:06
 */
public class AnalysisTreeDataDTO extends AnalysisBaseDTO {
    /**构建树结构的子节点key*/
    private String nodeKey = "nodeId";
    /**构建树结构的父节点key*/
    private String parentNodeKey = "parentNodeId";

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getParentNodeKey() {
        return parentNodeKey;
    }

    public void setParentNodeKey(String parentNodeKey) {
        this.parentNodeKey = parentNodeKey;
    }
}
