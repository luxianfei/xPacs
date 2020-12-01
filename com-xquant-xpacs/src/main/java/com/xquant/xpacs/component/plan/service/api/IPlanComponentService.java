/**
 * ******************************************
 * 文件名称: IPlanComponentService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 11:17:34
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.plan.service.api;

import com.xquant.xpims.tplan.entity.dto.PlanAutoCompleteDTO;
import com.xquant.xpims.tplan.entity.po.ext.PlanAutoCompletePO;

import java.util.List;

/**
 * @ClassName: IPlanComponentService
 * @Description: 计划组件接口
 * @author: yt.zhou
 * @date: 2020年09月28日 11:17:34
 */
public interface IPlanComponentService {

    /**
     * @author: yt.zhou
     * @date: 2020年09月28日 11:20
     * @description: 计划auto-complete控件数据获取
     * @param planAutoCompleteDTO
     * @return java.util.List<com.xquant.xpims.tplan.entity.po.ext.PlanAutoCompletePO>
     *
     */
    List<PlanAutoCompletePO> getPlanAutoComplete(PlanAutoCompleteDTO planAutoCompleteDTO);
}
