/**
 *******************************************
 * 文件名称: TransationDetailParamDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 大类资产交易明细查询参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月18日 上午11:24:10
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.assetallocation.entity.dto;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

/**
 * @ClassName: TransationDetailParamDTO
 * @Description: 大类资产交易明细查询参数
 * @author: jingru.jiang
 * @date: 2020年11月18日 上午11:24:10
 *
 */
public class TransationDetailParamDTO extends AnalysisBaseParamDTO {
	/**对象类型*/
    private String calcObjType;
    /**分组节点Id,股票、债券、基金、养老金等*/
    private String nodeId;
    /**分组节点名称,股票、债券、基金、养老金等*/
    private String nodeName;
    /**是否走落地表*/
    private String isLand;
    /**分页-条数*/
    private int pageSize;
    /**分页-页数*/
    private int pageNum;
    
    public String getCalcObjType() {
    	return calcObjType;
    }
    
    public void setCalcObjType(String calcObjType) {
    	this.calcObjType = calcObjType;
    }
    
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getIsLand() {
		return isLand;
	}

	public void setIsLand(String isLand) {
		this.isLand = isLand;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}
