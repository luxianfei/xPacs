/**
 * ******************************************
 * 文件名称: AnalysisBaseParamDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月19日 15:35:06
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.entity.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AnalysisBaseParamDTO
 * @Description: 分析功能基础参数
 * @author: yt.zhou
 * @date: 2020年10月19日 15:35:06
 */
public class AnalysisBaseParamDTO extends BaseParamDTO {
    /**开始日期*/
    private String begDate;
    /**结束日期*/
    private String endDate;
    /**组合代码*/
    private List<String> portCode;
    /**计划代码*/
    private String planCode;
    /**对象类型，组合，计划，投资经理，投管人 @see com.xquant.index.base.enums.PortType*/
    private String portType;
    /**计算类型 单个，复合，对比  @see com.xquant.index.base.enums.PortCalcType*/
    private String portCalcType;
    /**结果集是否需要缓存*/
    private Boolean cacheAble;
    /**指标计算是否走落地逻辑*/
    private Boolean landMid = true;
    /**指标方案代码*/
    private String moduleNo;

    public String getBegDate() {
        return begDate;
    }

    public void setBegDate(String begDate) {
        this.begDate = begDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getPortCode() {
        return portCode;
    }

    public void setPortCode(List<String> portCode) {
        this.portCode = portCode;
    }

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getPortCalcType() {
        return portCalcType;
    }

    public void setPortCalcType(String portCalcType) {
        this.portCalcType = portCalcType;
    }

    public Boolean getCacheAble() {
        return cacheAble;
    }

    public void setCacheAble(Boolean cacheAble) {
        this.cacheAble = cacheAble;
    }

    public Boolean getLandMid() {
        return landMid;
    }

    public void setLandMid(Boolean landMid) {
        this.landMid = landMid;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月12日 09:52
     * @description:    添加组合代码
     * @param portCode  添加组合代码
     *
     */
    public void addPortCode(String portCode) {
        if(this.getPortCode() == null) {
            this.portCode = new ArrayList<>();
        }
        this.portCode.add(portCode);
    }
}
