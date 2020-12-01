package com.xquant.xpacs.investmentVarieties.service.api;

import com.xquant.common.bean.GridPageResp;
import com.xquant.xpims.security.tbnd.entity.dto.BondRatingDTO;
import com.xquant.xpims.security.tbnd.entity.dto.CompanyRatingDTO;
import com.xquant.xpims.security.tbnd.entity.dto.TbndDTO;
import com.xquant.xpims.security.tbnd.entity.po.Tbnd;
import com.xquant.xpims.security.tbnd.entity.po.ext.TbndExtRatingExt;
import com.xquant.xpims.security.tbnd.entity.po.ext.TcompanyRatingExt;
import com.xquant.xpims.security.tfnd.entity.dto.TfndDTO;
import com.xquant.xpims.security.tfnd.entity.po.ext.TfndExt;
import com.xquant.xpims.security.tracing.entity.dto.SecurityHldTracingDTO;
import com.xquant.xpims.security.tracing.entity.po.SecurityHldTracingPO;
import com.xquant.xpims.security.tstk.entity.dto.TstkDTO;
import com.xquant.xpims.security.tstk.entity.po.ext.TstkExt;
import com.xquant.xpims.tfinprod.entity.dto.TfinProdDTO;
import com.xquant.xpims.tfinprod.entity.po.ext.TfinprodDetailInfo;

import java.util.List;

public interface StockInfoService {

    TstkExt getTstkDetail(TstkDTO tstkDTO);

    Tbnd getTbndDetail(TbndDTO tbndDTO);

    TfndExt getTfndDetail(TfndDTO tfndDTO);

    TfinprodDetailInfo getFinprodDetail(TfinProdDTO tfinProdDTO);

    GridPageResp<List<TbndExtRatingExt>> selectBondRatingTrajectoryPage(BondRatingDTO bondRatingDTO);

    GridPageResp<List<TcompanyRatingExt>> selectCompanyRatingTrajectoryPage(CompanyRatingDTO companyRatingDTO);

    List<SecurityHldTracingPO> getSecurityHldTracing(SecurityHldTracingDTO securityHldTracingDTO);

    /**
     * @author: yt.zhou
     * @date: 2020年11月25日 20:32
     * @description: 获取全市场股票的市值
     * @return java.util.List<com.xquant.xpims.security.tstk.entity.po.ext.TstkExt>
     *
     */
    List<TstkExt> getStkMtm(String tDate);
}
