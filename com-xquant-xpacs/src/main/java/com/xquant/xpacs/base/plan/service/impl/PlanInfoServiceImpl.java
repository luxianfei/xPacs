/**
 * ******************************************
 * 文件名称: PlanInfoServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月11日 17:41:16
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.plan.service.impl;

import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tplan.mapper.TplanInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: PlanInfoServiceImpl
 * @Description: 计划信息
 * @author: yt.zhou
 * @date: 2020年11月11日 17:41:16
 */
@Service
public class PlanInfoServiceImpl implements IPlanInfoService {
    @Autowired
    private TplanInfoMapper tplanInfoMapper;

    @Override
    public TplanInfo getPlanInfo(String planCode) {
        return tplanInfoMapper.selectByPrimaryKey(planCode);
    }
}
