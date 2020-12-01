/**
 * ******************************************
 * 文件名称: SubjectValueBachDownLoadDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月11日 17:36:04
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.inforQuery.entity.dto;

import java.util.List;

/**
 * @ClassName: SubjectValueBachDownLoadDTO
 * @Description: 批量下载估值表
 * @author: yt.zhou
 * @date: 2020年11月11日 17:36:04
 */
public class SubjectValueBachDownLoadDTO {
    /**开始日期*/
    private String begDate;
    /**结束日期*/
    private String endDate;
    /**计划代码*/
    private String planCode;
    /**选择下载的组合信息*/
    private List<SubjectValueDownLoadDTO> subjectValues;

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

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public List<SubjectValueDownLoadDTO> getSubjectValues() {
        return subjectValues;
    }

    public void setSubjectValues(List<SubjectValueDownLoadDTO> subjectValues) {
        this.subjectValues = subjectValues;
    }
}
