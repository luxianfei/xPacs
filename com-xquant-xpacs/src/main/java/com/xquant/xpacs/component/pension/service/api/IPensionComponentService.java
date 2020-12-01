/**
 * ******************************************
 * 文件名称: IPensionComponentService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 13:42:59
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.pension.service.api;

import com.xquant.xpims.tinvest.entity.dto.PensionAutoCompleteDTO;
import com.xquant.xpims.tinvest.entity.po.ext.PensionAutoCompletePO;

import java.util.List;

/**
 * @ClassName: IPensionComponentService
 * @Description: 投管人
 * @author: yt.zhou
 * @date: 2020年09月28日 13:42:59
 */
public interface IPensionComponentService {
    /**
     * @author: yt.zhou
     * @date: 2020年09月28日 13:47
     * @description: 投管人auto-complete组件数据获取
     * @param pensionAutoCompleteDTO
     * @return java.util.List<com.xquant.xpims.tinvest.entity.po.ext.PensionAutoCompletePO>
     *
     */
    List<PensionAutoCompletePO> getPensionAutoComplete(PensionAutoCompleteDTO pensionAutoCompleteDTO);
}
