/**
 * ******************************************
 * 文件名称: EntireAssetParamDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月19日 15:36:03
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.entity.dto;

import java.util.List;

/**
 * @ClassName: EntireAssetParamDTO
 * @Description: 整体资产分析
 * @author: yt.zhou
 * @date: 2020年10月19日 15:36:03
 */
public class EntireAssetParamDTO extends AnalysisBaseParamDTO {
    /**计划分类*/
    private List<String> planClasss;
    /**投资风格*/
    private List<String> invStyles;

    public List<String> getPlanClasss() {
        return planClasss;
    }

    public void setPlanClasss(List<String> planClasss) {
        this.planClasss = planClasss;
    }

    public List<String> getInvStyles() {
        return invStyles;
    }

    public void setInvStyles(List<String> invStyles) {
        this.invStyles = invStyles;
    }
}
