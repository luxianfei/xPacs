/**
 * ******************************************
 * 文件名称: EmphasisAssetCompareServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月27日 17:13:04
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.impl;

import com.xquant.common.exception.BusinessServiceException;
import com.xquant.common.util.SortUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisDetailParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.service.api.IEmphasisAssetCompareAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @ClassName: EmphasisAssetCompareServiceImpl
 * @Description: 重点资产对比
 * @author: yt.zhou
 * @date: 2020年10月27日 17:13:04
 */
@Component
public class EmphasisAssetCompareAsyncServiceImpl implements IEmphasisAssetCompareAsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmphasisAssetCompareAsyncServiceImpl.class);
    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;

    @Async("analysisAsyncExecutor")
    @Override
    public Future<List<Map<String,Object>>> topTenStockCompare(AnalysisBaseParamDTO calcBaseParam) {
        AnalysisIndexCalcResultBO indexCalcResultBO = analysisIndexCalcService.calc("topTenStockCompare",calcBaseParam);

        List<Map<String,Object>> resultList = new ArrayList<>();
        if(indexCalcResultBO.getSuccessful()) {
            //做排序
            resultList = SortUtils.sort(indexCalcResultBO.getResultList(),"holdRatio","desc");

            resultList = this.calcTopTenGbTwRtnAndMerge(calcBaseParam, resultList);
        }

        return new AsyncResult<>(resultList);
    }

    @Async("analysisAsyncExecutor")
    @Override
    public Future<List<Map<String, Object>>> topTenBndCompare(AnalysisBaseParamDTO calcBaseParam) {
        AnalysisIndexCalcResultBO indexCalcResultBO = analysisIndexCalcService.calc("topTenBondCompare",calcBaseParam);

        List<Map<String,Object>> resultList = new ArrayList<>();
        if(indexCalcResultBO != null && indexCalcResultBO.getSuccessful()) {
            //做排序
            resultList = SortUtils.sort(indexCalcResultBO.getResultList(), "holdRatio", "desc");

            resultList = this.calcTopTenGbTwRtnAndMerge(calcBaseParam, resultList);
        }


        return new AsyncResult<>(resultList);
    }

    @Async("analysisAsyncExecutor")
    @Override
    public Future<List<Map<String, Object>>> gradeCompare(AnalysisBaseParamDTO calcBaseParam) {
        AnalysisIndexCalcResultBO indexCalcResultBO = analysisIndexCalcService.calc("ModuleGradeCompare",calcBaseParam);

        List<Map<String,Object>> resultList = new ArrayList<>();
        if(indexCalcResultBO.getSuccessful()) {
            //做排序
            resultList = SortUtils.sort(indexCalcResultBO.getResultList(), "nodeId","asc");

        }

        return new AsyncResult<>(resultList);
    }

    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 16:10
     * @description:    计算金融工具收益率,并且合并数据
     * @param calcBaseParam
     * @param resultList
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    private List<Map<String,Object>> calcTopTenGbTwRtnAndMerge(AnalysisBaseParamDTO calcBaseParam, List<Map<String,Object>> resultList) {
        List<String> instrumentList = new ArrayList<>();
        int i = 0;
        Map<String, Map<String,Object>> instrumentResult = new HashMap<>();
        for(Map<String,Object> result : resultList) {
            String instrument = result.get("iCode") + "," + result.get("aType") + "," + result.get("mType");
            instrumentList.add(instrument);
            result.put("index", i++);

            instrumentResult.put(instrument,result);
        }

        if(!instrumentList.isEmpty()) {
            //调用金融工具收益率计算,
            AnalysisDetailParamDTO detailParamDTO = new AnalysisDetailParamDTO();
            BeanUtils.copyProperties(calcBaseParam, detailParamDTO);
            detailParamDTO.setInstrumentList(instrumentList);
            detailParamDTO.setLandMid(false);
            detailParamDTO.setIsAssetSplit(0);

            AnalysisIndexCalcResultBO detailResultBo = analysisIndexCalcService.calc("topTenGbTwRtn", detailParamDTO);
            if(!detailResultBo.getSuccessful()) {
                throw new BusinessServiceException("",detailResultBo.getMessage());
            }

            //合并
            List<Map<String,Object>> twRtnResultList = detailResultBo.getResultList();
            for(Map<String,Object> twRtnResult : twRtnResultList) {
                String instrument = twRtnResult.get("iCode") + "," + twRtnResult.get("aType") + "," + twRtnResult.get("mType");
                Map<String,Object> pfTwResult = instrumentResult.get(instrument);
                if(pfTwResult != null) {
                    pfTwResult.putAll(twRtnResult);
                }
            }
        }

        return resultList;
    }
}
