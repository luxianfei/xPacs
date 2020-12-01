/**
 * ******************************************
 * 文件名称: PortInfoController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 15:06:53
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.port.controller;

import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: PortInfoController
 * @Description: 组合信息
 * @author: yt.zhou
 * @date: 2020年11月03日 15:06:53
 */
@RestController
@RequestMapping("/portInfo")
public class PortInfoController {
    @Autowired
    private ITprtService tprtService;

    /**
     * @author: yt.zhou
     * @date: 2020年11月16日 19:27
     * @description: 获取组合最新净值日
     * @param portCode  组合代码
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    @GetMapping("/getLatestNavDateByPortCode")
    public HttpBaseResponse getLatestNavDateByPortCode(String portCode){
        Map<String, String> latestNavDateByPlanCode = tprtService.getLatestNavDateByPortCode(portCode);
        if(latestNavDateByPlanCode == null) {
            return HttpBaseResponseUtil.getFailResponse("组合没有净值数据");
        } else {
            latestNavDateByPlanCode.put("latestDate",latestNavDateByPlanCode.get("T_DATE"));
            return HttpBaseResponseUtil.getSuccessResponse(latestNavDateByPlanCode);
        }
    }
}
