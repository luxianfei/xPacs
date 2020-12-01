/**
 * ******************************************
 * 文件名称: SecurityController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月27日 15:15:51
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.security.controller;

import com.xquant.common.bean.CommonResp;
import com.xquant.xpacs.component.security.service.api.ISecurityService;
import com.xquant.xpims.security.component.bo.SecurityBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: SecurityController
 * @Description: 证券选择组件
 * @author: yt.zhou
 * @date: 2020年09月27日 15:15:51
 */
@RestController("component-security-controller")
@RequestMapping("/component/security")
public class SecurityController {
    @Autowired
    private ISecurityService securityService;

    /**
     * @author: yt.zhou
     * @date: 2020年09月27日 15:18
     * @description: 证券选择控件查询
     * @param nameCode  查询条件
     * @param sType     证券类型
     * @return org.springframework.http.ResponseEntity<com.xquant.common.bean.CommonResp<java.util.List<com.xquant.xpims.security.component.bo.SecurityBO>>>
     *
     */
    @GetMapping("/getComponentSecurity")
    public ResponseEntity<CommonResp<List<SecurityBO>>> getComponentSecurity(String nameCode, String sType) {
        List<SecurityBO> list = securityService.securityFactory(nameCode,sType);
        return ResponseEntity.ok(new CommonResp<>(list));
    }

    @GetMapping("/getSecurityIssuer")
    public ResponseEntity<CommonResp<List<SecurityBO>>> getSecurityIssuer(String nameCode, String sType) {
        List<SecurityBO> list = securityService.securityIssuerFactory(nameCode,sType);
        return ResponseEntity.ok(new CommonResp<>(list));
    }
}
