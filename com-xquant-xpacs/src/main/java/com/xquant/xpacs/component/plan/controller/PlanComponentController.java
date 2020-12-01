/**
 * ******************************************
 * 文件名称: PlanComponentController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月28日 11:17:07
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.plan.controller;

import com.xquant.common.bean.CommonResp;
import com.xquant.xpacs.component.plan.service.api.IPlanComponentService;
import com.xquant.xpims.tplan.entity.dto.PlanAutoCompleteDTO;
import com.xquant.xpims.tplan.entity.po.ext.PlanAutoCompletePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: PlanComponentController
 * @Description: 计划组件
 * @author: yt.zhou
 * @date: 2020年09月28日 11:17:07
 */
@RestController
@RequestMapping("/component/plan")
public class PlanComponentController {
    @Autowired
    private IPlanComponentService planComponentService;

    /**
     * @author: yt.zhou
     * @date: 2020年09月28日 11:42
     * @description:
     * @param planAutoCompleteDTO
     * @param request
     * @return org.springframework.http.ResponseEntity<com.xquant.common.bean.CommonResp<java.util.List<com.xquant.xpims.tplan.entity.po.ext.PlanAutoCompletePO>>>
     *
     */
    @GetMapping("/getPlanAutoComplete")
    public ResponseEntity<CommonResp<List<PlanAutoCompletePO>>> getPlanAutoComplete(PlanAutoCompleteDTO planAutoCompleteDTO, HttpServletRequest request) {
        /*LoginUserBO loginUserBO = (LoginUserBO) request.getSession().getAttribute(UserConstant.SESSION_USER);
        planAutoCompleteDTO.setUserId(loginUserBO.getUserId());*/
        List<PlanAutoCompletePO> planAutoCompletePOList = planComponentService.getPlanAutoComplete(planAutoCompleteDTO);
        CommonResp resp = new CommonResp(planAutoCompletePOList);
        return ResponseEntity.ok(resp);
    }
}
