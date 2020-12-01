/**
 * ******************************************
 * 文件名称: PensionComponentServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 13:43:22
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.pension.service.impl;

import com.xquant.xpacs.component.pension.service.api.IPensionComponentService;
import com.xquant.xpims.tinvest.entity.dto.PensionAutoCompleteDTO;
import com.xquant.xpims.tinvest.entity.po.ext.PensionAutoCompletePO;
import com.xquant.xpims.tinvest.mapper.ext.TPensionOrgInfoExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: PensionComponentServiceImpl
 * @Description: 投管人
 * @author: yt.zhou
 * @date: 2020年09月28日 13:43:22
 */
@Service
public class PensionComponentServiceImpl implements IPensionComponentService {
    @Autowired
    private TPensionOrgInfoExtMapper pensionOrgInfoExtMapper;
    @Override
    public List<PensionAutoCompletePO> getPensionAutoComplete(PensionAutoCompleteDTO pensionAutoCompleteDTO) {
        return pensionOrgInfoExtMapper.getPensionAutoComplete(pensionAutoCompleteDTO);
    }
}
