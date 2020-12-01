/**
 * ******************************************
 * 文件名称: PortComponentServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 10:37:35
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.port.service.impl;

import com.xquant.xpacs.component.port.service.api.IPortComponentService;
import com.xquant.xpims.tprt.entity.dto.PortAutoCompleteDTO;
import com.xquant.xpims.tprt.entity.po.ext.PortAutoCompletePO;
import com.xquant.xpims.tprt.mapper.ext.TprtExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: PortComponentServiceImpl
 * @Description: 组合控件
 * @author: yt.zhou
 * @date: 2020年09月28日 10:37:35
 */
@Service
public class PortComponentServiceImpl implements IPortComponentService {
    @Autowired
    private TprtExtMapper tprtExtMapper;
    @Override
    public List<PortAutoCompletePO> getPortAutoComplete(PortAutoCompleteDTO portAutoCompleteDTO) {
        return tprtExtMapper.getPortAutoComplete(portAutoCompleteDTO);
    }
}
