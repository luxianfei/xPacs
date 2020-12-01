/**
 * ******************************************
 * 文件名称: AssetAllocationController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月04日 09:47:00
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.assetallocation.controller;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.common.bean.bo.TableColumnModel;
import com.xquant.common.enums.EnumRespMsg;
import com.xquant.common.util.StringUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexAsyncCalcService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.assetallocation.entity.dto.AssetAllocationTrendParamDTO;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.http.HttpDynamicColumnListResult;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.common.format.model.AnalysisRow2CellDTO;
import com.xquant.xpacs.common.format.table.Row2CellTableDataFormat;
import com.xquant.xpacs.common.format.table.SimpleTableResultModel;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @ClassName: AssetAllocationController
 * @Description: 资产配置
 * @author: yt.zhou
 * @date: 2020年11月04日 09:47:00
 */
@RestController
@RequestMapping("/assetAllocation")
public class AssetAllocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetAllocationController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private ITprtService tprtService;
    @Autowired
    private IPlanInfoService tplanInfoService;
    @Autowired
    private IAnalysisDataCacheService dataCacheService;
    @Autowired
    private IAnalysisIndexAsyncCalcService analysisIndexAsyncCalcService;

    /**
     * @author: yt.zhou
     * @date: 2020年11月04日 09:48
     * @description: 资产配置统计
     * @param   statisticsParamDTO
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    @AnalysisResponseCacheAble
    @RequestMapping(value = "/calcAssetAllocationStatistics", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcAssetAllocationStatistics(AnalysisBaseParamDTO statisticsParamDTO) {

        String moduleNo = "assetAllocationStatistics";

        String planCode = statisticsParamDTO.getPlanCode();
        TplanInfo tplanInfo = tplanInfoService.getPlanInfo(planCode);

        AnalysisIndexCalcResultBO resultBO;
        if(CalculateType.directCalc.equals(tplanInfo.getCalculateType())) {
            //计划层数据是导入的,将计划当做组合计算
            List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);

            AnalysisBaseParamDTO calcParam = new AnalysisBaseParamDTO();
            BeanUtils.copyProperties(statisticsParamDTO, calcParam);

            for(Tprt tprt : portList) {
                calcParam.addPortCode(tprt.getPortCode());
            }

            calcParam.setPortType(PortType.port.getCode());
            calcParam.setPortCalcType(PortCalcType.single.name());

            resultBO = analysisIndexCalcService.calc(moduleNo, calcParam);
        } else {
            //计划作为复合计算, 复合+对比
            AnalysisBaseParamDTO calcParam = new AnalysisBaseParamDTO();
            BeanUtils.copyProperties(statisticsParamDTO, calcParam);
            calcParam.addPortCode(tplanInfo.getPlanCode());
            calcParam.setPortCalcType(PortCalcType.compositeAndContrast.name());
            calcParam.setPortType(PortType.plan.getCode());

            resultBO = analysisIndexCalcService.calc(moduleNo, calcParam);
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());

        return baseResponse;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月04日 13:54
     * @description:    资产配置统计表格数据
     * @param requestId 缓存Id
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    @GetMapping("/assetAllocationStatisticsTable")
    public HttpBaseResponse assetAllocationStatisticsTable(String requestId) {
        List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
        AnalysisRow2CellDTO row2CellDTO = new AnalysisRow2CellDTO();
        List<String> parentColumnKey = new ArrayList<>();
        parentColumnKey.add("nodeName");
        row2CellDTO.setParentColumnKey(parentColumnKey);

        List<String> setSameRowFlagKey = new ArrayList<>();
        setSameRowFlagKey.add("portCode");
        setSameRowFlagKey.add("pName");
        setSameRowFlagKey.add("endDate");

        row2CellDTO.setSameRowFlagKey(setSameRowFlagKey);

        List<String> uniqueKey = new ArrayList<>();
        uniqueKey.add("nodeId");
        row2CellDTO.setUniqueKey(uniqueKey);

        List<TableColumnModel> tableColumnModels = new ArrayList<>();

        TableColumnModel columnModel4 = new TableColumnModel();
        columnModel4.setLabel("组合名称");
        columnModel4.setProp("pName");
        columnModel4.setWidth(200);

        tableColumnModels.add(columnModel4);

        TableColumnModel columnModel5 = new TableColumnModel();
        columnModel5.setLabel("日期");
        columnModel5.setProp("endDate");
        columnModel5.setWidth(200);
        tableColumnModels.add(columnModel5);

        TableColumnModel columnModel = new TableColumnModel();
        columnModel.setLabel("期初");
        columnModel.setProp("gbHldWMtmRootPre");
        columnModel.setWidth(200);

        TableColumnModel columnModel2 = new TableColumnModel();
        columnModel2.setLabel("平均");
        columnModel2.setProp("gbPdHldAvgMtm");
        columnModel2.setWidth(200);

        TableColumnModel columnModel3 = new TableColumnModel();
        columnModel3.setLabel("期末");
        columnModel3.setProp("gbHldWMtmRoot");
        columnModel3.setWidth(200);

        tableColumnModels.add(columnModel);
        tableColumnModels.add(columnModel2);
        tableColumnModels.add(columnModel3);

        row2CellDTO.setTableColumns(tableColumnModels);

        Row2CellTableDataFormat dataFormat = new Row2CellTableDataFormat();
        SimpleTableResultModel tableResultModel = (SimpleTableResultModel) dataFormat.doFormat(dataList, row2CellDTO);

        HttpDynamicColumnListResult dynamicColumnListResult = new HttpDynamicColumnListResult(tableResultModel.getDataList(), tableResultModel.getColumnModelList());

        return HttpBaseResponseUtil.getSuccessResponse(dynamicColumnListResult);
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月12日 11:24
     * @description: 资产配置走势计算
     * @param allocationTrendParamDTO
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    @AnalysisResponseCacheAble
    @GetMapping("/calcAssetAllocationTrend")
    public HttpBaseResponse calcAssetAllocationTrend(AssetAllocationTrendParamDTO allocationTrendParamDTO) {
        String moduleNo = "assetAllocationTrend";

        //走落地
        allocationTrendParamDTO.setLandMid(true);

        String planCode = allocationTrendParamDTO.getPlanCode();
        TplanInfo tplanInfo = tplanInfoService.getPlanInfo(planCode);

        AnalysisIndexCalcResultBO resultBO;
        if(CalculateType.directCalc.equals(tplanInfo.getCalculateType())) {
            //计划层数据是导入的,将计划当做组合计算

            AssetAllocationTrendParamDTO calcParam = new AssetAllocationTrendParamDTO();
            BeanUtils.copyProperties(allocationTrendParamDTO, calcParam);

            //计算计划
            if(allocationTrendParamDTO.getCalcPlan()) {
                //将计划作为组合代码传递，
                calcParam.addPortCode(planCode);
            }

            calcParam.setPortType(PortType.port.getCode());
            calcParam.setPortCalcType(PortCalcType.single.name());

            resultBO = analysisIndexCalcService.calc(moduleNo, calcParam);
        } else {
            resultBO = new AnalysisIndexCalcResultBO();


            //计划作为复合计算, 计算传过来的组合

            Future<AnalysisIndexCalcResultBO> future = null;
            if(allocationTrendParamDTO.getPortCode() != null && !allocationTrendParamDTO.getPortCode().isEmpty()) {
                AssetAllocationTrendParamDTO calcParam = new AssetAllocationTrendParamDTO();
                BeanUtils.copyProperties(allocationTrendParamDTO, calcParam);
                calcParam.setPortCalcType(PortCalcType.single.name());
                calcParam.setPortType(PortType.port.getCode());

                future = analysisIndexAsyncCalcService.asyncCalc(moduleNo, calcParam);
            }

            //计算计划
            Future<AnalysisIndexCalcResultBO> future1 = null;
            if(allocationTrendParamDTO.getCalcPlan()) {
                AssetAllocationTrendParamDTO calcPlanParam = new AssetAllocationTrendParamDTO();
                BeanUtils.copyProperties(allocationTrendParamDTO, calcPlanParam);
                calcPlanParam.setPortCalcType(PortCalcType.single.name());
                calcPlanParam.setPortType(PortType.plan.getCode());
                calcPlanParam.setPortCode(new ArrayList<>());
                calcPlanParam.addPortCode(planCode);

                future1 = analysisIndexAsyncCalcService.asyncCalc(moduleNo, calcPlanParam);
            }


            try {
                List<Map<String,Object>> allDataList = new ArrayList<>();
                if(future != null) {
                    AnalysisIndexCalcResultBO result = future.get();
                    List<Map<String,Object>> dataList1 = result.getResultList();
                    allDataList.addAll(dataList1);
                }

                if(future1 != null) {
                    AnalysisIndexCalcResultBO result1 = future1.get();
                    List<Map<String,Object>> dataList2 = result1.getResultList();
                    allDataList.addAll(dataList2);
                }

                resultBO.setResultList(allDataList);
                resultBO.setCode(EnumRespMsg.SUCCESS.getRespCode());
                resultBO.setMessage(EnumRespMsg.SUCCESS.getRespMsg());

            } catch (InterruptedException e) {
                LOGGER.error("计算异常", e);
            } catch (ExecutionException e) {
                LOGGER.error("计算异常", e);
            }
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());

        return baseResponse;
    }
}
