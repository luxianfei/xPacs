/**
 * ******************************************
 * 文件名称: PensionComponentController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 13:42:01
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.pension.controller;

import com.xquant.common.bean.CommonResp;
import com.xquant.xpacs.component.pension.service.api.IPensionComponentService;
import com.xquant.xpims.tinvest.entity.dto.PensionAutoCompleteDTO;
import com.xquant.xpims.tinvest.entity.po.ext.PensionAutoCompletePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: PensionComponentController
 * @Description: 投管人选择控件
 * @author: yt.zhou
 * @date: 2020年09月28日 13:42:01
 */
@RestController
@RequestMapping("/component/pension")
public class PensionComponentController {

    @Autowired
    private IPensionComponentService pensionComponentService;
    /**
     * @author: yt.zhou
     * @date: 2020年09月28日 13:56
     * @description: 投管人auto-complete组件
     * @param pensionAutoCompleteDTO
     * @return org.springframework.http.ResponseEntity<com.xquant.common.bean.CommonResp<java.util.List<com.xquant.xpims.tinvest.entity.po.ext.PensionAutoCompletePO>>>
     *
     */
    @GetMapping("/getPensionAutoComplete")
    public ResponseEntity<CommonResp<List<PensionAutoCompletePO>>> getPensionAutoComplete(PensionAutoCompleteDTO pensionAutoCompleteDTO) {
        List<PensionAutoCompletePO> pensionAutoCompletePOList = pensionComponentService.getPensionAutoComplete(pensionAutoCompleteDTO);
        CommonResp resp = new CommonResp(pensionAutoCompletePOList);
        return ResponseEntity.ok(resp);
    }
}
