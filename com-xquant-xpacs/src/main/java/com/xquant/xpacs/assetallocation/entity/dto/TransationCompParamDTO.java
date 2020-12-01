/**
 *******************************************
 * 文件名称: TransationCompParamDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 大类资产交易比较查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月18日 上午11:06:09
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.assetallocation.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: TransationCompParamDTO
 * @Description: 大类资产交易比较查询参数
 * @author: jingru.jiang
 * @date: 2020年11月18日 上午11:06:09
 *
 */
public class TransationCompParamDTO extends AnalysisBaseParamDTO {
    /**分组节点Id,股票、债券、基金、养老金等*/
    private String nodeId;
    
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
}
