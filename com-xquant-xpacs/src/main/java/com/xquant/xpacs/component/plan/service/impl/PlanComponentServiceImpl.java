/**
 * ******************************************
 * 文件名称: PlanComponentServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 11:17:53
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.plan.service.impl;

import com.xquant.xpacs.component.plan.service.api.IPlanComponentService;
import com.xquant.xpims.tplan.entity.dto.PlanAutoCompleteDTO;
import com.xquant.xpims.tplan.entity.po.ext.PlanAutoCompletePO;
import com.xquant.xpims.tplan.mapper.ext.TplanInfoExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: PlanComponentServiceImpl
 * @Description: 计划组件接口
 * @author: yt.zhou
 * @date: 2020年09月28日 11:17:53
 */
@Service
public class PlanComponentServiceImpl implements IPlanComponentService {
    @Autowired
    private TplanInfoExtMapper tplanInfoExtMapper;

    @Override
    public List<PlanAutoCompletePO> getPlanAutoComplete(PlanAutoCompleteDTO planAutoCompleteDTO) {
        return tplanInfoExtMapper.getPlanAutoComplete(planAutoCompleteDTO);
    }
}
