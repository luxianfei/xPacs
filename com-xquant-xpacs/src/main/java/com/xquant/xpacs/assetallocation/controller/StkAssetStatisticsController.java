/**
 * ******************************************
 * 文件名称: StkAssetAllocationStatisticsController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月25日 16:32:45
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.assetallocation.controller;

import com.xquant.base.param.entity.po.SysParam;
import com.xquant.base.param.service.api.SysParamService;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.StringUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.investmentVarieties.service.api.StockInfoService;
import com.xquant.xpims.security.tstk.entity.po.ext.TstkExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @ClassName: StkAssetAllocationStatisticsController
 * @Description: 股票资产统计信息
 * @author: yt.zhou
 * @date: 2020年11月25日 16:32:45
 */
@RestController
@RequestMapping("/stkAssetStatistics")
public class StkAssetStatisticsController {
    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IAnalysisIndexAsyncCalcService analysisIndexAsyncCalcService;
    @Autowired
    private StockInfoService stockInfoService;
    @Autowired
    private SysParamService sysParamService;

    /**
     * @author: yt.zhou
     * @date: 2020年11月25日 16:34
     * @description: 股票统计信息计算，计算股票市值，占比，市盈率，市净率，大中小盘市值占比
     * @param analysisBaseParamDTO
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcStkStatistics", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcStkStatistics(AnalysisBaseParamDTO analysisBaseParamDTO) {
        String moduleNo = "stkStatistics";
        analysisBaseParamDTO.setBegDate(analysisBaseParamDTO.getEndDate());

        //持仓
        Future<AnalysisIndexCalcResultBO> staticticsFutrue = analysisIndexAsyncCalcService.asyncCalc(moduleNo, analysisBaseParamDTO);


        //股票持仓明细，用于计算大中小盘的占比
        String stkDetailModuleNo = "stkHldDetail";
        Future<AnalysisIndexCalcResultBO> hldDetailFutrue = analysisIndexAsyncCalcService.asyncCalc(stkDetailModuleNo, analysisBaseParamDTO);


        AnalysisIndexCalcResultBO resultBO = null;
        try {
            resultBO = staticticsFutrue.get();
            AnalysisIndexCalcResultBO detailResult = hldDetailFutrue.get();

            //大盘股比例
            SysParam largeSysParam = sysParamService.getSysParam("LARGE_CAP_STOCK");
            //中盘股比例
            SysParam midSysParam = sysParamService.getSysParam("MID_CAP_STOCK");

            Double largeD = largeSysParam == null ? 10D : NumberUtils.convertToDouble(largeSysParam.getpValue(), 10D);
            Double midD = midSysParam == null ? 20D : NumberUtils.convertToDouble(midSysParam.getpValue(), 20D);

            Map<String, Set<String>> pClassMap = this.calcPclassWeight(analysisBaseParamDTO.getEndDate(), largeD, midD);

            double dpgWeight = 0D;
            double zpgWeight = 0D;
            double xpgWeight = 0D;
            List<Map<String, Object>> detailList = detailResult.getResultList();
            for(Map<String, Object> data : detailList) {
                String iCode = StringUtils.defaultIfNull(data.get("iCode"));
                String aType = StringUtils.defaultIfNull(data.get("aType"));
                String mType = StringUtils.defaultIfNull(data.get("mType"));

                //权重
                double gbHldWMtmRoot = NumberUtils.convertToDouble(data.get("gbHldWMtmRoot"));
                String stkInfo = iCode + "_" + aType + "_" + mType;
                if(pClassMap.get("dpg").contains(stkInfo)) {
                    //大盘股
                    dpgWeight += gbHldWMtmRoot;
                } else if (pClassMap.get("zpg").contains(stkInfo)) {
                    zpgWeight += gbHldWMtmRoot;
                } else {
                    xpgWeight += gbHldWMtmRoot;
                }
            }

            if(resultBO.getSuccessful()) {
                List<Map<String,Object>> dataList = resultBO.getResultList();
                Map<String, Object> data = dataList.get(0);
                data.put("dpgWeight", dpgWeight);
                data.put("zpgWeight", zpgWeight);
                data.put("xpgWeight", xpgWeight);
                data.put("lageCap", largeD + "%");
                data.put("midCap", midD + "%");
                data.put("littleCap", (100 - midD) + "%");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());
        return baseResponse;
    }


    /**
     * @author: yt.zhou
     * @date: 2020年11月25日 21:04
     * @description: 计算股票大中小盘占比
     * @param   tDate 日期
     * @param   largeD  大盘股比例 10 表示市值在全市场前10%的股票属于大盘股
     * @param   midD    中盘股比例 20 标识市值在10% - 20% 的股票属于中盘股
     *
     */
    private Map<String, Set<String>> calcPclassWeight(String tDate, Double largeD, Double midD) {
        //全市场股票市值
        List<TstkExt> tstkList = stockInfoService.getStkMtm(tDate);

        //按市值排序，倒序
        tstkList.sort(Comparator.comparing(TstkExt::getMtm).reversed());
        int size = tstkList.size();
        Set<String> dpgStk = new HashSet<>();
        Set<String> zpgStk = new HashSet<>();
        Set<String> xpgStk = new HashSet<>();
        for(int i=0;i<size;i++){
            String stkInfo = tstkList.get(i).getiCode() + "_" + tstkList.get(i).getaType() + "_" + tstkList.get(i).getmType();
            int num = i+1;
            if(num <= largeD){
                dpgStk.add(stkInfo);
            }else if(num <= midD){
                zpgStk.add(stkInfo);
            }else{
                xpgStk.add(stkInfo);
            }
        }
        Map<String, Set<String>> pclassMap = new HashMap<>();
        pclassMap.put("dpg", dpgStk);
        pclassMap.put("zpg", zpgStk);
        pclassMap.put("xpg", xpgStk);

        return pclassMap;
    }
}
