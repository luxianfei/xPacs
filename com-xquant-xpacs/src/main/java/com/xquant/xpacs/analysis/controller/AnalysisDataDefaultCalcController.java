/**
 * ******************************************
 * 文件名称: AnalysisDataDefaultCalcController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月02日 14:47:59
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.controller;

import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: AnalysisDataDefaultCalcController
 * @Description: 指标方案默认调用方法
 * @author: yt.zhou
 * @date: 2020年11月02日 14:47:59
 */
@RestController
@RequestMapping("/analysisDataDefaultCalc")
public class AnalysisDataDefaultCalcController {

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;

    /**
     * @author: yt.zhou
     * @date: 2020年11月02日 14:57
     * @description: 指标计算默认调用计算接口，普通计算，即不需要做复杂的逻辑处理就可以使用的
     * @param request   参数
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/defaultCalc", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse defaultCalc(HttpServletRequest request) {
        AnalysisIndexCalcResultBO analysisIndexCalcResultBO = analysisIndexCalcService.defaultCalc(request);

        HttpBaseResponse baseResponse = new HttpBaseResponse(analysisIndexCalcResultBO.getCode(),
                analysisIndexCalcResultBO.getMessage(), analysisIndexCalcResultBO.getResultList());
        return baseResponse;
    }
}
