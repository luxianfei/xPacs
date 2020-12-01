/**
 * ******************************************
 * 文件名称: SecurityServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年09月27日 13:12:31
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.component.security.service.impl;

import com.xquant.xpacs.component.security.service.api.ISecurityService;
import com.xquant.xpims.security.component.bo.SecurityBO;
import com.xquant.xpims.security.component.mapper.SecurityComponentMapper;
import com.xquant.xpims.security.tbnd.entity.po.Tbnd;
import com.xquant.xpims.security.tbnd.entity.po.TbndExample;
import com.xquant.xpims.security.tbnd.entity.po.TbndKey;
import com.xquant.xpims.security.tbnd.mapper.TbndMapper;
import com.xquant.xpims.security.tbnd.mapper.ext.TbndMapperExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SecurityServiceImpl
 * @Description: 证券相关
 * @author: yt.zhou
 * @date: 2020年09月27日 13:12:31
 */
@Service("component-security-service")
public class SecurityServiceImpl implements ISecurityService {
    @Autowired
    private SecurityComponentMapper securityComponentMapper;
    @Autowired
    private TbndMapperExt tbndMapper;

    @Override
    public List<SecurityBO> getStk(String nameCode) {
        return securityComponentMapper.getTstk(nameCode);
    }

    @Override
    public List<SecurityBO> getBnd(String nameCode) {
        return securityComponentMapper.getTbnd(nameCode);
    }

    @Override
    public List<SecurityBO> getFnd(String nameCode) {
        return securityComponentMapper.getFnd(nameCode);
    }

    @Override
    public List<SecurityBO> getCashlb(String nameCode) {
        return securityComponentMapper.getCashlb(nameCode);
    }

    @Override
    public List<SecurityBO> getFinprod(String nameCode) {
        return securityComponentMapper.getFinprod(nameCode);
    }

    @Override
    public List<SecurityBO> securityFactory(String nameCode, String sType) {
        if("STK".equals(sType)) {
            //股票
            return this.getStk(nameCode);
        } else if ("BND".equals(sType)) {
            return this.getBnd(nameCode);
        } else if ("FND".equals(sType)) {
            return this.getFnd(nameCode);
        } else if ("CASHLB".equals(sType)) {
            return this.getCashlb(nameCode);
        } else if ("FINPROD".equals(sType)) {
            return this.getFinprod(nameCode);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SecurityBO> securityIssuerFactory(String nameCode, String sType) {
        if("STK".equals(sType)) {
            //股票
            return securityComponentMapper.getTstkIssuer(nameCode);
        } else if ("BND".equals(sType)) {
            return securityComponentMapper.getTbndIssuer(nameCode);
        } else if ("FND".equals(sType)) {
            return securityComponentMapper.getFndIssuer(nameCode);
        } else if ("FINPROD".equals(sType)) {
            return securityComponentMapper.getFinprodIssuer(nameCode);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Tbnd> getTbndInfoList(List<TbndKey> bndKeys) {
        return tbndMapper.getTbndInfoList(bndKeys);
    }
}
