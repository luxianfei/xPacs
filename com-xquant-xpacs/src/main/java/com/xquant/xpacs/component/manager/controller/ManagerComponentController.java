/**
 * ******************************************
 * 文件名称: ManagerComponentController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 14:47:07
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.manager.controller;

import com.xquant.common.bean.CommonResp;
import com.xquant.xpacs.component.manager.service.api.IManagerComponentService;
import com.xquant.xpims.tinvest.entity.dto.ManagerAutoCompleteDTO;
import com.xquant.xpims.tinvest.entity.po.ext.ManagerAutoCompletePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: ManagerComponentController
 * @Description: 投资经理
 * @author: yt.zhou
 * @date: 2020年09月28日 14:47:07
 */
@RestController
@RequestMapping("/component/manager")
public class ManagerComponentController {
    @Autowired
    private IManagerComponentService managerComponentService;

    @GetMapping("/getManagerAutoComplete")
    public ResponseEntity<CommonResp<List<ManagerAutoCompletePO>>> getManagerAutoComplete(ManagerAutoCompleteDTO managerAutoCompleteDTO) {
        List<ManagerAutoCompletePO> managerAutoCompletePOList = managerComponentService.getManagerAutoComplete(managerAutoCompleteDTO);
        CommonResp resp = new CommonResp(managerAutoCompletePOList);
        return ResponseEntity.ok(resp);
    }
}
