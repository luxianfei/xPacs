/**
 * ******************************************
 * 文件名称: PortComponentController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 10:38:17
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.port.controller;

import com.xquant.common.bean.CommonResp;
import com.xquant.xpacs.component.port.service.api.IPortComponentService;
import com.xquant.xpims.tprt.entity.dto.PortAutoCompleteDTO;
import com.xquant.xpims.tprt.entity.po.ext.PortAutoCompletePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: PortComponentController
 * @Description: 组合控件
 * @author: yt.zhou
 * @date: 2020年09月28日 10:38:17
 */
@RestController
@RequestMapping("/component/port")
public class PortComponentController {
    @Autowired
    private IPortComponentService portComponentService;

    @GetMapping("/getPortAutoComplete")
    public ResponseEntity<CommonResp<List<PortAutoCompletePO>>> getPortAutoComplete(PortAutoCompleteDTO portAutoCompleteDTO, HttpServletRequest request) {
        /*LoginUserBO loginUserBO = (LoginUserBO) request.getSession().getAttribute(UserConstant.SESSION_USER);
        portAutoCompleteDTO.setUserId(loginUserBO.getUserId());*/

        List<PortAutoCompletePO> portAutoCompletePOList = portComponentService.getPortAutoComplete(portAutoCompleteDTO);
        CommonResp resp = new CommonResp(portAutoCompletePOList);

        return ResponseEntity.ok(resp);
    }
}
