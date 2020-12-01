/**
 * ******************************************
 * 文件名称: ManagerComponentServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 14:45:36
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.manager.service.impl;

import com.xquant.xpacs.component.manager.service.api.IManagerComponentService;
import com.xquant.xpims.tinvest.entity.dto.ManagerAutoCompleteDTO;
import com.xquant.xpims.tinvest.entity.po.ext.ManagerAutoCompletePO;
import com.xquant.xpims.tinvest.mapper.ext.InvestManagerExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: ManagerComponentServiceImpl
 * @Description: 投资经理组件相关
 * @author: yt.zhou
 * @date: 2020年09月28日 14:45:36
 */
@Service
public class ManagerComponentServiceImpl implements IManagerComponentService {
    @Autowired
    private InvestManagerExtMapper investManagerExtMapper;

    @Override
    public List<ManagerAutoCompletePO> getManagerAutoComplete(ManagerAutoCompleteDTO managerAutoCompleteDTO) {
        return investManagerExtMapper.getManagerAutoComplete(managerAutoCompleteDTO);
    }
}
