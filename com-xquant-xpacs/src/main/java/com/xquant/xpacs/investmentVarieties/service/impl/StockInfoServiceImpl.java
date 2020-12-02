package com.xquant.xpacs.investmentVarieties.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xquant.common.annotation.ServiceMethod;
import com.xquant.common.bean.GridPageResp;
import com.xquant.xpacs.investmentVarieties.service.api.StockInfoService;
import com.xquant.xpims.security.tbnd.entity.dto.BondRatingDTO;
import com.xquant.xpims.security.tbnd.entity.dto.CompanyRatingDTO;
import com.xquant.xpims.security.tbnd.entity.dto.TbndDTO;
import com.xquant.xpims.security.tbnd.entity.po.Tbnd;
import com.xquant.xpims.security.tbnd.entity.po.TbndExtRatingExample;
import com.xquant.xpims.security.tbnd.entity.po.TcompanyRatingExample;
import com.xquant.xpims.security.tbnd.entity.po.ext.TbndExt;
import com.xquant.xpims.security.tbnd.entity.po.ext.TbndExtRatingExt;
import com.xquant.xpims.security.tbnd.entity.po.ext.TcompanyRatingExt;
import com.xquant.xpims.security.tbnd.mapper.ext.TbndExtRatingMapperExt;
import com.xquant.xpims.security.tbnd.mapper.ext.TbndMapperExt;
import com.xquant.xpims.security.tbnd.mapper.ext.TcompanyRatingMapperExt;
import com.xquant.xpims.security.tfnd.entity.dto.TfndDTO;
import com.xquant.xpims.security.tfnd.entity.po.ext.TfndExt;
import com.xquant.xpims.security.tfnd.mapper.ext.TfndMapperExt;
import com.xquant.xpims.security.tracing.entity.dto.SecurityHldTracingDTO;
import com.xquant.xpims.security.tracing.entity.po.SecurityHldTracingPO;
import com.xquant.xpims.security.tracing.mapper.ext.StockPositionTracingExtMapper;
import com.xquant.xpims.security.tstk.entity.dto.TstkDTO;
import com.xquant.xpims.security.tstk.entity.po.ext.TstkExt;
import com.xquant.xpims.security.tstk.mapper.ext.TstkExtMapper;
import com.xquant.xpims.tfinprod.entity.dto.TfinProdDTO;
import com.xquant.xpims.tfinprod.entity.po.ext.TfinprodDetailInfo;
import com.xquant.xpims.tfinprod.mapper.ext.TfinprodExpandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockInfoServiceImpl implements StockInfoService {
    @Autowired
    private TstkExtMapper tstkExtMapper;
    @Autowired
    private TfndMapperExt tfndMapperExt;
    @Autowired
    private TbndMapperExt tbndMapper;
    @Autowired
    private TcompanyRatingMapperExt tcompanyRatingMapperExt;
    @Autowired
    private TbndExtRatingMapperExt tbndExtRatingMapper;
    @Autowired
    private TfinprodExpandMapper tfinprodExpandMapper;
    @Autowired
    private StockPositionTracingExtMapper stockPositionTracingExtMapper;

    @Override
    public TstkExt getTstkDetail(TstkDTO tstkDTO) {
        return tstkExtMapper.getTstkDetail(tstkDTO);
    }

    @Override
    public TbndExt getTbndDetail(TbndDTO tbndDTO) {
        return tbndMapper.getTbndDetail(tbndDTO);
    }

    @Override
    public TfndExt getTfndDetail(TfndDTO tfndDTO) {
        return tfndMapperExt.getTfndDetail(tfndDTO);
    }

    @Override
    public TfinprodDetailInfo getFinprodDetail(TfinProdDTO tfinProdDTO) {
        return tfinprodExpandMapper.getTfinprodDetailInfo(tfinProdDTO);
    }

    @Override
    public GridPageResp<List<TbndExtRatingExt>> selectBondRatingTrajectoryPage(BondRatingDTO bondRatingDTO) {
        Page page = null;
        if(bondRatingDTO.getPageNum() != null) {
            page = PageHelper.startPage(bondRatingDTO.getPageNum(),bondRatingDTO.getPageSize());
        }
        TbndExtRatingExample example = new TbndExtRatingExample();
        example.createCriteria()
                .andICodeEqualTo(bondRatingDTO.getiCode())
                .andATypeEqualTo(bondRatingDTO.getaType())
                .andMTypeEqualTo(bondRatingDTO.getmType())
                .andBegDateLessThan(bondRatingDTO.getEndDate());
        example.setOrderByClause("beg_date desc");
        List<TbndExtRatingExt> list = tbndExtRatingMapper.selectBndRatingByExample(example);
        long total = page == null ? list.size() : page.getTotal();
        GridPageResp resp = new GridPageResp(list,total);
        return resp;
    }

    @Override
    public GridPageResp<List<TcompanyRatingExt>> selectCompanyRatingTrajectoryPage(CompanyRatingDTO companyRatingDTO) {
        Page page=null;
        if (companyRatingDTO.getPageNum()!=null){
            page=PageHelper.startPage(companyRatingDTO.getPageNum(),companyRatingDTO.getPageSize());
        }
        TcompanyRatingExample example = new TcompanyRatingExample();
        example.createCriteria()
                .andCompCodeEqualTo(companyRatingDTO.getCompCode())
                .andBegDateLessThan(companyRatingDTO.getEndDate());
        example.setOrderByClause("beg_date desc");
        List<TcompanyRatingExt> tcompanyRatingExtList=tcompanyRatingMapperExt.selectCompanyRatingByExample(example);
        long total = page==null?tcompanyRatingExtList.size():page.getTotal();
        GridPageResp resp = new GridPageResp(tcompanyRatingExtList,total);
        return resp;
    }

    @ServiceMethod(method = "getSecurityHldTracing")
    @Override
    public List<SecurityHldTracingPO> getSecurityHldTracing(SecurityHldTracingDTO securityHldTracingDTO) {
        return stockPositionTracingExtMapper.getSecurityHldTracingByIssuer(securityHldTracingDTO);
    }

    @Override
    public List<TstkExt> getStkMtm(String tDate) {
        return tstkExtMapper.getTstkClass(tDate);
    }
}
