/**
 * ******************************************
 * 文件名称: TcrpSubjectValueGroup.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月10日 14:53:36
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.inforQuery.entity.bo;

import com.xquant.xpims.tcrp.entity.po.TcrpSubjectValue;

import java.util.List;

/**
 * @ClassName: TcrpSubjectValueGroup
 * @Description: 估值信息分组结果
 * @author: yt.zhou
 * @date: 2020年11月10日 14:53:36
 */
public class TcrpSubjectValueGroupBO {
    /**正常科目，在估值表导出excel的第一块*/
    private List<TcrpSubjectValue> subjectValues1;
    /**统计科目1，在估值表导出excel的第二块*/
    private List<TcrpSubjectValue> subjectValues2;
    /**统计科目2，从gpf_crp_hld表数据看，科目代码为9开头的科目属于这类数据，放在估值表导出excel的第三块*/
    private List<TcrpSubjectValue> subjectValues3;

    public List<TcrpSubjectValue> getSubjectValues1() {
        return subjectValues1;
    }

    public void setSubjectValues1(List<TcrpSubjectValue> subjectValues1) {
        this.subjectValues1 = subjectValues1;
    }

    public List<TcrpSubjectValue> getSubjectValues2() {
        return subjectValues2;
    }

    public void setSubjectValues2(List<TcrpSubjectValue> subjectValues2) {
        this.subjectValues2 = subjectValues2;
    }

    public List<TcrpSubjectValue> getSubjectValues3() {
        return subjectValues3;
    }

    public void setSubjectValues3(List<TcrpSubjectValue> subjectValues3) {
        this.subjectValues3 = subjectValues3;
    }
}
