/**
 * ******************************************
 * 文件名称: ITprtService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月09日 16:50:28
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.port.service.api;

import com.xquant.xpims.tprt.entity.dto.TprtAnalysisDTO;
import com.xquant.xpims.tprt.entity.po.Tprt;
import com.xquant.xpims.tprt.entity.po.ext.TprtExt;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ITprtService
 * @Description: 组合相关接口
 * @author: yt.zhou
 * @date: 2020年07月09日 16:50:28
 */
public interface ITprtService {

    /**
     * @author: yt.zhou
     * @date: 2020年07月09日 16:51
     * @description: 根据计划查询组合
     * @param planCode
     * @return java.util.List<com.xquant.xpims.tprt.entity.po.Tprt>
     *
     */
    List<Tprt> getPortListByPlanCode(String planCode);



    /**
     * @author: yt.zhou
     * @date: 2020年07月09日 09:42
     * @description:  查询计划的默认结束日期，获取逻辑：
     *              先查询计划每个组合的最大净值日期，从最大日期中取最小值
     * @param planCodes
     * @return java.lang.String
     *
     */
    Map<String,String> getLatestNavDateByPlanCode(String planCodes);

    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 15:43
     * @description: 根据组合代码查询组合信息
     * @param portCode  组合代码
     * @return com.xquant.xpims.tprt.entity.po.Tprt
     *
     */
    Tprt getTprtByPortCode(String portCode);

    /**
     * @author: yt.zhou
     * @date: 2020年11月16日 19:22
     * @description: 获取组合最新净值日期
     * @param portCode  组合代码
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    Map<String,String> getLatestNavDateByPortCode(String portCode);
    
    /**
     * @Title: getRetainPortListByPlanCode
     * @Description: 获取未到期的组合
     * @param: planCode
     * @param: tDate
     * @return: List<Tprt>   
     * @throws
     */
    List<Tprt> getRetainPortListByPlanCode(String planCode, String tDate);
}
