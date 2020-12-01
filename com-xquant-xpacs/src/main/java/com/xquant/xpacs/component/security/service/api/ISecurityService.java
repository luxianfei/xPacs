/**
 * ******************************************
 * 文件名称: ISecurityService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月27日 11:41:15
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.security.service.api;


import com.xquant.xpims.security.component.bo.SecurityBO;
import com.xquant.xpims.security.tbnd.entity.po.Tbnd;
import com.xquant.xpims.security.tbnd.entity.po.TbndKey;
import com.xquant.xpims.security.tbnd.entity.po.ext.TbndExt;

import java.util.List;

/**
 * @ClassName: ISecurityService
 * @Description: 证券相关
 * @author: yt.zhou
 * @date: 2020年09月27日 11:41:15
 */
public interface ISecurityService {
    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 13:21
     * @description:  查询股票
     * @param nameCode 股票查询条件
     * @return java.util.List<com.xquant.xpims.component.security.entity.bo.SecurityBO>
     *
     */
    List<SecurityBO> getStk(String nameCode);

    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 15:02
     * @description: 债券
     * @param nameCode
     * @return java.util.List<com.xquant.xpims.security.component.bo.SecurityBO>
     *
     */
    List<SecurityBO> getBnd(String nameCode);

    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 15:03
     * @description: 基金
     * @param nameCode
     * @return java.util.List<com.xquant.xpims.security.component.bo.SecurityBO>
     *
     */
    List<SecurityBO> getFnd(String nameCode);

    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 15:03
     * @description: 非标
     * @param nameCode
     * @return java.util.List<com.xquant.xpims.security.component.bo.SecurityBO>
     *
     */
    List<SecurityBO> getCashlb(String nameCode);

    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 15:03
     * @description: 养老金
     * @param nameCode
     * @return java.util.List<com.xquant.xpims.security.component.bo.SecurityBO>
     *
     */
    List<SecurityBO> getFinprod(String nameCode);

    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 15:05
     * @description: 工厂
     * @param nameCode 检索
     * @param sType 证券类型，STK股票，BND债券，FND基金，CASHLB非标，FINPROD养老金
     * @return java.util.List<com.xquant.xpims.security.component.bo.SecurityBO>
     *
     */
    List<SecurityBO> securityFactory(String nameCode, String sType);

    List<SecurityBO> securityIssuerFactory(String nameCode, String sType);

    /**
     * @author: yt.zhou
     * @date: 2020年11月26日 18:59
     * @description: 查询债券详细信息列表，
     * @param bndKeys  主键集合
     * @return java.util.List<com.xquant.xpims.security.tbnd.entity.po.Tbnd>
     *
     */
    List<Tbnd> getTbndInfoList(List<TbndKey> bndKeys);
}
