/**
 * ******************************************
 * 文件名称: IAnalysisIndexCalcService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月19日 16:17:57
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.api;

import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: IAnalysisIndexCalcService
 * @Description: 分析指标计算接口
 * @author: yt.zhou
 * @date: 2020年10月19日 16:17:57
 */
public interface IAnalysisIndexCalcService {

    /**
     * @author: yt.zhou
     * @date: 2020年10月20日 09:38
     * @description:  指标方案执行计算
     * @param moduleNo      指标方案代码
     * @param baseParamDTO  指标方案需要的参数
     * @return com.xquant.xpims.analysis.entity.bo.AnalysisIndexCalcResultBO
     *
     */
    AnalysisIndexCalcResultBO calc(String moduleNo, AnalysisBaseParamDTO baseParamDTO);

    /**
     * @author: yt.zhou
     * @date: 2020年11月02日 14:51
     * @description:    默认指标计算调用方法，不需要前置后置逻辑处理的业务模块，可以直接调用
     * @param request
     * @return com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO
     *
     */
    AnalysisIndexCalcResultBO defaultCalc(HttpServletRequest request);
}
