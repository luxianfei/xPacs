/**
 * ******************************************
 * 文件名称: EmphasisAssetCompareController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月27日 09:05:36
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.controller;

import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IEmphasisAssetCompareAsyncService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @ClassName: EmphasisAssetCompareController
 * @Description: 重点资产对比
 * @author: yt.zhou
 * @date: 2020年10月27日 09:05:36
 */
@RestController
@RequestMapping("/emphasisAssetCompare")
public class EmphasisAssetCompareController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmphasisAssetCompareController.class);

    @Autowired
    private IEmphasisAssetCompareAsyncService emphasisAssetCompareAsyncService;

    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 09:15
     * @description: 前十大重仓股票对比
     * @param calcBaseParam 指标计算参数
     * @return org.springframework.http.ResponseEntity<com.xquant.common.bean.GridPageResp<com.xquant.xpims.common.format.table.SimpleTableResultModel>>
     *
     */
    @AnalysisResponseCacheAble
    @GetMapping("/topTenStockCompare")
    public List<Map<String,Object>> topTenStockCompare(AnalysisBaseParamDTO calcBaseParam) throws ExecutionException, InterruptedException {
        List<String> portCodes = calcBaseParam.getPortCode();
        List<Future<List<Map<String,Object>>>> futureList = new ArrayList<>();
        for(String portCode : portCodes) {
            AnalysisBaseParamDTO singleCalcParam = new AnalysisBaseParamDTO();
            BeanUtils.copyProperties(calcBaseParam, singleCalcParam);
            //计算获取前十大股票，并且返回他们的市值占比
            List<String> singlePortCode = new ArrayList<>();
            singlePortCode.add(portCode);
            singleCalcParam.setPortCode(singlePortCode);

            Future<List<Map<String,Object>>> asyncResult = emphasisAssetCompareAsyncService.topTenStockCompare(singleCalcParam);

            futureList.add(asyncResult);
        }

        List<Map<String,Object>> dataList = new ArrayList<>();
        for(Future<List<Map<String,Object>>> future : futureList) {
            List<Map<String,Object>> result = future.get();
            dataList.addAll(result);
        }

        return dataList;
    }


    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 09:15
     * @description: 前十大重仓债券对比
     * @param calcBaseParam 指标计算参数
     * @return org.springframework.http.ResponseEntity<com.xquant.common.bean.GridPageResp<com.xquant.xpims.common.format.table.SimpleTableResultModel>>
     *
     */
    @AnalysisResponseCacheAble
    @GetMapping("/topTenBndCompare")
    public List<Map<String,Object>> topTenBndCompare(AnalysisBaseParamDTO calcBaseParam) throws ExecutionException, InterruptedException {


        List<Future<List<Map<String,Object>>>> futureList = new ArrayList<>();
        List<String> portCodes = calcBaseParam.getPortCode();
        for(String portCode : portCodes) {

            AnalysisBaseParamDTO singleCalcParam = new AnalysisBaseParamDTO();
            BeanUtils.copyProperties(calcBaseParam, singleCalcParam);
            //计算获取前十大股票，并且返回他们的市值占比
            List<String> singlePortCode = new ArrayList<>();
            singlePortCode.add(portCode);
            singleCalcParam.setPortCode(singlePortCode);

            Future<List<Map<String,Object>>> asyncResult = emphasisAssetCompareAsyncService.topTenBndCompare(singleCalcParam);
            futureList.add(asyncResult);
        }

        List<Map<String,Object>> dataList = new ArrayList<>();
        for(Future<List<Map<String,Object>>> future : futureList) {
            List<Map<String,Object>> result = future.get();
            dataList.addAll(result);
        }

        return dataList;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年10月27日 09:15
     * @description: 前十大重仓债券对比
     * @param calcBaseParam 指标计算参数
     * @return org.springframework.http.ResponseEntity<com.xquant.common.bean.GridPageResp<com.xquant.xpims.common.format.table.SimpleTableResultModel>>
     *
     */
    @AnalysisResponseCacheAble
    @GetMapping("/gradeCompare")
    public List<Map<String,Object>> gradeCompare(AnalysisBaseParamDTO calcBaseParam) throws ExecutionException, InterruptedException {

        List<Future<List<Map<String,Object>>>> futureList = new ArrayList<>();
        List<String> portCodes = calcBaseParam.getPortCode();
        for(String portCode : portCodes) {

            AnalysisBaseParamDTO singleCalcParam = new AnalysisBaseParamDTO();
            BeanUtils.copyProperties(calcBaseParam, singleCalcParam);
            //计算获取前十大股票，并且返回他们的市值占比
            List<String> singlePortCode = new ArrayList<>();
            singlePortCode.add(portCode);
            singleCalcParam.setPortCode(singlePortCode);

            Future<List<Map<String,Object>>> asyncResult = emphasisAssetCompareAsyncService.gradeCompare(singleCalcParam);
            futureList.add(asyncResult);
        }

        List<Map<String,Object>> dataList = new ArrayList<>();
        for(Future<List<Map<String,Object>>> future : futureList) {
            List<Map<String,Object>> result = future.get();
            dataList.addAll(result);
        }

        return dataList;
    }
}
