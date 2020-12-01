/**
 * ******************************************
 * 文件名称: IPortComponentService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 10:37:18
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.port.service.api;

import com.xquant.xpims.tprt.entity.dto.PortAutoCompleteDTO;
import com.xquant.xpims.tprt.entity.po.ext.PortAutoCompletePO;

import java.util.List;

/**
 * @ClassName: IPortComponentService
 * @Description: 组合控件
 * @author: yt.zhou
 * @date: 2020年09月28日 10:37:18
 */
public interface IPortComponentService {
    /**
     * @author: yt.zhou
     * @date: 2020年09月28日 10:46
     * @description: 组合auto-complete控件数据检索
     * @param portAutoCompleteDTO
     * @return java.util.List<com.xquant.xpims.tprt.entity.po.ext.PortAutoCompletePO>
     *
     */
    List<PortAutoCompletePO> getPortAutoComplete(PortAutoCompleteDTO portAutoCompleteDTO);
}
