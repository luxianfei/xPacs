
package com.xquant.xpacs.investmentVarieties.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xquant.auth.userManager.constant.UserConstant;
import com.xquant.auth.userManager.entity.bo.LoginUserBO;
import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.common.bean.CommonResp;
import com.xquant.common.bean.GridPageResp;
import com.xquant.common.util.BigDecimalUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.investmentVarieties.service.api.StockInfoService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
 @RequestMapping("/stockInfo")
public class StockInfoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockInfoController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private StockInfoService stockInfoService;
    @Autowired
    private ITprtService tprtService;

    /**
     * 股票列表
     * @param analysisBaseParamDTO
     * @return
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcAssetInfoList", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcAssetInfoList(AnalysisBaseParamDTO analysisBaseParamDTO) {
        analysisBaseParamDTO.setLandMid(false);
        analysisBaseParamDTO.setPortType(PortType.port.getCode());
        if(analysisBaseParamDTO.getPortCode().size() == 0){
            analysisBaseParamDTO.getPortCode().add(analysisBaseParamDTO.getPlanCode());
            analysisBaseParamDTO.setPortType(PortType.plan.getCode());
        }
        analysisBaseParamDTO.setPortCalcType(PortCalcType.single.name());
        // 资产信息计算
        AnalysisIndexCalcResultBO stockInfoList = analysisIndexCalcService.calc(analysisBaseParamDTO.getModuleNo(), analysisBaseParamDTO);
        // 获取计划层面总净值
        List<String> plan_list = new ArrayList<>();
        plan_list.add(analysisBaseParamDTO.getPlanCode());
        analysisBaseParamDTO.setPortCode(plan_list);
        analysisBaseParamDTO.setPortType(PortType.plan.getCode());
        AnalysisIndexCalcResultBO planNav = analysisIndexCalcService.calc("totalNav", analysisBaseParamDTO);

        if(stockInfoList.getResultList().size()>0){
            for(Map<String,Object> stockInfo : stockInfoList.getResultList()){
                stockInfo.put("gbHldWMtmRoot", BigDecimalUtils.divide(new BigDecimal(stockInfo.get("gbHldMtm").toString()),new BigDecimal(planNav.getResultList().get(0).get("gbPTotalNav").toString())));
            }
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(stockInfoList.getCode(), stockInfoList.getMessage(), stockInfoList.getResultList());

        return baseResponse;
    }

    /**
     * 股票基本信息
     * @param tstkDTO
     * @return
     */
    @GetMapping("/getTstkDetail")
    public ResponseEntity<CommonResp<TstkExt>> getTstkDetail(TstkDTO tstkDTO) {
        TstkExt tstk = stockInfoService.getTstkDetail(tstkDTO);
        CommonResp resp = new CommonResp(tstk);
        return ResponseEntity.ok(resp);
    }

    /**
     * 债券基本信息
     * @param tbndDTO
     * @return
     */
    @GetMapping("/getTbndDetail")
    public ResponseEntity<CommonResp<Tbnd>> getTbndDetail(TbndDTO tbndDTO) {
        Tbnd tbnd = stockInfoService.getTbndDetail(tbndDTO);
        CommonResp resp = new CommonResp(tbnd);
        return ResponseEntity.ok(resp);
    }

    /**
     * 基金基本信息
     * @param tfndDTO
     * @return
     */
    @GetMapping("/getTfndDetail")
    public ResponseEntity<CommonResp<TfndExt>> getTfndDetail(TfndDTO tfndDTO) {
        TfndExt tfnd = stockInfoService.getTfndDetail(tfndDTO);
        CommonResp resp = new CommonResp(tfnd);
        return ResponseEntity.ok(resp);
    }

    /**
     *养老金基本信息
     * @param tfinProdDTO
     * @return
     */
    @GetMapping("/getFinprodDetail")
    public ResponseEntity<CommonResp<TfinprodDetailInfo>> getFinprodDetail(TfinProdDTO tfinProdDTO) {
        TfinprodDetailInfo tfinprod = stockInfoService.getFinprodDetail(tfinProdDTO);
        CommonResp resp = new CommonResp(tfinprod);
        return ResponseEntity.ok(resp);
    }

    /**
     * 债券评级
     * @param bondRatingDTO
     * @return
     */
    @GetMapping("/getBondRatingTrajectory")
    public HttpBaseResponse getBondRatingTrajectory(BondRatingDTO bondRatingDTO) {
        return new HttpBaseResponse(stockInfoService.selectBondRatingTrajectoryPage(bondRatingDTO));
    }

    /**
     * 主体评级
     * @param companyRatingDTO
     * @return
     */
    @GetMapping("/getCompanyRatingTrajectory")
    public HttpBaseResponse getCompanyRatingTrajectory(CompanyRatingDTO companyRatingDTO) {
        return new HttpBaseResponse(stockInfoService.selectCompanyRatingTrajectoryPage(companyRatingDTO));
    }

    /**
     * 持仓追溯
     * @param securityHldTracingDTO
     * @param request
     * @return
     */
    @GetMapping("/getSecurityHldTracing")
    public HttpBaseResponse getSecurityHldTracing(SecurityHldTracingDTO securityHldTracingDTO, HttpServletRequest request) {
        //LoginUserBO loginUserBO = (LoginUserBO) request.getSession().getAttribute(UserConstant.SESSION_USER);
        //securityHldTracingDTO.setUserId(loginUserBO.getUserId());
        Page page = PageHelper.startPage(securityHldTracingDTO.getPageNum(),securityHldTracingDTO.getPageSize());
        List<SecurityHldTracingPO> securityHldTracingPOList = stockInfoService.getSecurityHldTracing(securityHldTracingDTO);
        GridPageResp resp = new GridPageResp(securityHldTracingPOList,page.getTotal());
        return new HttpBaseResponse(resp);
    }

}
