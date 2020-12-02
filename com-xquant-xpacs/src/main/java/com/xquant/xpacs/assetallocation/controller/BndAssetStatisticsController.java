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

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.xquant.base.param.entity.po.SysParam;
import com.xquant.common.enums.EnumRespMsg;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.StringUtils;
import com.xquant.common.util.excel.ExcelExportUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.entity.dto.TableChartExportBaseDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.assetallocation.util.JxlsUtils;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.component.security.service.api.ISecurityService;
import com.xquant.xpims.security.tbnd.entity.po.Tbnd;
import com.xquant.xpims.security.tbnd.entity.po.TbndKey;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @ClassName: StkAssetAllocationStatisticsController
 * @Description: 股票资产统计信息
 * @author: yt.zhou
 * @date: 2020年11月25日 16:32:45
 */
@RestController
@RequestMapping("/bndAssetStatistics")
public class BndAssetStatisticsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BndAssetStatisticsController.class);
    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IAnalysisIndexAsyncCalcService indexAsyncCalcService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IAnalysisDataCacheService dataCacheService;

    /**
     * @param analysisBaseParamDTO
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     * @author: yt.zhou
     * @date: 2020年11月25日 16:34
     * @description: 股票统计信息计算，计算股票市值，占比，市盈率，市净率，大中小盘市值占比
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcBndStatistics", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpBaseResponse calcBndStatistics(AnalysisBaseParamDTO analysisBaseParamDTO) {
        String moduleNo = "bndStatistics";
        analysisBaseParamDTO.setBegDate(analysisBaseParamDTO.getEndDate());
        Future<AnalysisIndexCalcResultBO> resultFuture = indexAsyncCalcService.asyncCalc(moduleNo, analysisBaseParamDTO);

        //债券主体评级
        String rateModuleNo = "bndMainRate";
        Future<AnalysisIndexCalcResultBO> rateResultFuture = indexAsyncCalcService.asyncCalc(rateModuleNo, analysisBaseParamDTO);

        AnalysisIndexCalcResultBO resultBO = new AnalysisIndexCalcResultBO();
        try {
            resultBO = resultFuture.get();
            AnalysisIndexCalcResultBO rateResult = rateResultFuture.get();
            if (rateResult.getSuccessful()) {
                List<Map<String, Object>> dataList = resultBO.getResultList();
                if (dataList.size() > 0) {
                    dataList.get(0).put("bndMainRate", rateResult.getResultList());
                }
            }
        } catch (InterruptedException e) {
            resultBO.setCode(EnumRespMsg.ERROR.getRespCode());
            resultBO.setMessage(EnumRespMsg.ERROR.getRespMsg());
            LOGGER.error("异步计算异常", e);
        } catch (ExecutionException e) {
            resultBO.setCode(EnumRespMsg.ERROR.getRespCode());
            resultBO.setMessage(EnumRespMsg.ERROR.getRespMsg());
            LOGGER.error("异步计算异常", e);
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());
        return baseResponse;
    }

    /**
     * @param baseParamDTO
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     * @author: yt.zhou
     * @date: 2020年11月26日 18:54
     * @description: 前十大重仓债券信息
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcBndTopTenDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpBaseResponse calcBndTopTenDetail(AnalysisBaseParamDTO baseParamDTO) {
        String moduleNo = "bndTopTenDetail";
        baseParamDTO.setBegDate(baseParamDTO.getEndDate());
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc(moduleNo, baseParamDTO);

        // 对债券结果查询债券类型，pClass
        if (resultBO.getSuccessful()) {
            List<Map<String, Object>> dataList = resultBO.getResultList();

            Map<String, Map<String, Object>> keyData = new HashMap<>();

            List<TbndKey> tbndKeys = new ArrayList<>();
            for (Map<String, Object> data : dataList) {

                String iCode = StringUtils.defaultIfNull(data.get("iCode"));
                String aType = StringUtils.defaultIfNull(data.get("aType"));
                String mType = StringUtils.defaultIfNull(data.get("mType"));

                TbndKey tbndKey = new TbndKey();
                tbndKey.setiCode(iCode);
                tbndKey.setaType(aType);
                tbndKey.setmType(mType);
                tbndKeys.add(tbndKey);

                String key = iCode + "_" + aType + "_" + mType;
                keyData.put(key, data);
            }

            List<Tbnd> tbnds = securityService.getTbndInfoList(tbndKeys);
            for (Tbnd tbnd : tbnds) {
                String iCode = tbnd.getiCode();
                String aType = tbnd.getaType();
                String mType = tbnd.getmType();

                String key = iCode + "_" + aType + "_" + mType;
                Map<String, Object> data = keyData.get(key);
                if (data != null) {
                    data.put("bpClass", tbnd.getbPClass());
                }
            }

        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());
        return baseResponse;
    }

    /**
     * 导出债券表格
     *
     * @param exportBaseDTO
     * @param response
     * @return
     */
    @PostMapping("/exportBndAssetStatistics")
    public HttpBaseResponse exportBndAssetStatistics(@RequestBody TableChartExportBaseDTO exportBaseDTO, HttpServletResponse response) throws IOException {
        Map<String, Object> dataMap = new HashMap<>(32);
        // 缓存表格数据获取
        List<String> requestIds = exportBaseDTO.getRequestIds();

        //统计信息
        String statisticsRequestId = requestIds.get(0);
        List<Map<String, Object>> statisticsDataList = dataCacheService.getCache(statisticsRequestId);
        Map<String, Object> bndInfo = statisticsDataList.get(0);
        dataMap.put("bndInfo", statisticsDataList.get(0));
        Object bndMainRate = bndInfo.get("bndMainRate");
        dataMap.put("bndMainRate", bndMainRate);
        List<Map<String, Object>> dataList = dataCacheService.getCache(requestIds.get(1));
        dataMap.put("list1", dataList);

        // 图片数据获取
        BASE64Decoder decoder = new BASE64Decoder();
        List<String> chartDatas = exportBaseDTO.getChartDatas();
        int i = 0;
        for (String chartData : chartDatas) {
            try {
                byte[] imgBytes = decoder.decodeBuffer(chartData);
                dataMap.put("chart" + (++i), imgBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String fileName = "债券资产统计.xlsx";
        JxlsUtils.downLoadExcel("bndStatisticsTemplate.xlsx", fileName, response, dataMap);
        return null;

    }


}
