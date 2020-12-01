/**
 * ******************************************
 * 文件名称: PlanInfoController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 15:06:25
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.plan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: PlanInfoController
 * @Description: 计划信息
 * @author: yt.zhou
 * @date: 2020年11月03日 15:06:25
 */
@RestController
@RequestMapping("/planInfo")
public class PlanInfoController {

    @Autowired
    private ITprtService tprtService;
    @Autowired
    private IPlanInfoService planInfoService;


    /**
     * 获取计划下的组合信息
     * @param planCode
     * @return
     */
    @GetMapping("/getPortListByPlanCode")
    public HttpBaseResponse getPortListByPlanCode(String planCode){
        List<Tprt> tprtList = tprtService.getPortListByPlanCode(planCode);
        return HttpBaseResponseUtil.getListResponse(tprtList);
    }

    /**
     * 获取最新净值日
     * 获取计划的最新净值日期，结束日期默认为组合最新净值日，如果组合的最新净值日不同，则取最小值
     * @param planCode
     * @return
     */
    @GetMapping("/getLatestNavDateByPlanCode")
    public HttpBaseResponse getLatestNavDateByPlanCode(String planCode){
        Map<String, String> latestNavDateByPlanCode = tprtService.getLatestNavDateByPlanCode(planCode);
        latestNavDateByPlanCode.put("latestDate",latestNavDateByPlanCode.get("T_DATE"));
        return HttpBaseResponseUtil.getSuccessResponse(latestNavDateByPlanCode);
    }
    
    /**
     * @Title: getPlanInfo
     * @Description: 根据计划代码获取计划信息
     * @param: planCode
     * @return: HttpBaseResponse   
     * @throws
     */
    @GetMapping("/getPlanInfo")
    public HttpBaseResponse getPlanInfo(String planCode){
    	TplanInfo planInfo = planInfoService.getPlanInfo(planCode);
        return HttpBaseResponseUtil.getSuccessResponse(planInfo);
    }
}
