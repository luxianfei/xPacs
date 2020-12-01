/**
 *******************************************
 * 文件名称: CashFlowQueryController.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 信息查询
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 现金流查询
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月5日 上午10:08:48
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.inforQuery.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.index.enums.PortCalcType;
import com.xquant.base.index.enums.PortType;
import com.xquant.common.bean.bo.TableColumnModel;
import com.xquant.common.util.NumberUtils;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.analysis.support.AnalysisResponseCacheAble;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.http.HttpDynamicColumnListResult;
import com.xquant.xpacs.base.plan.enums.CalculateType;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.common.format.model.AnalysisRow2CellDTO;
import com.xquant.xpacs.common.format.table.Row2CellTableDataFormat;
import com.xquant.xpacs.common.format.table.SimpleTableResultModel;
import com.xquant.xpacs.inforQuery.entity.dto.CashFlowQueryParamDTO;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import com.xquant.xpims.tprt.entity.po.Tprt;

/**
 * @ClassName: CashFlowQueryController
 * @Description: 现金流查询
 * @author: jingru.jiang
 * @date: 2020年11月5日 上午10:08:48
 *
 */
@RestController
@RequestMapping("/cashFlowQuery")
public class CashFlowQueryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashFlowQueryController.class);

    @Autowired
    private IAnalysisIndexCalcService analysisIndexCalcService;
    @Autowired
    private IAnalysisDataCacheService dataCacheService;
    @Autowired
    private IPlanInfoService planInfoService;
    @Autowired
    private ITprtService tprtService;
	
	/**
	 * @Title: getCashFlow
	 * @Description: 现金流计算
	 * @param: cashFlowQueryParamDTO 
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	@AnalysisResponseCacheAble
    @RequestMapping(value = "/getCashFlow", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse getCashFlow(CashFlowQueryParamDTO cashFlowQueryParamDTO) {
		cashFlowQueryParamDTO.setLandMid(false);
        String planCode = cashFlowQueryParamDTO.getPlanCode();
        // 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
        	// 获取计划下所有组合
        	List<Tprt> portList = tprtService.getPortListByPlanCode(planCode);
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
            for(Tprt tprt : portList) {
                portCodes.add(tprt.getPortCode());
            }
            cashFlowQueryParamDTO.setPortCode(portCodes);
            cashFlowQueryParamDTO.setPortType(PortType.port.getCode());
            cashFlowQueryParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算-计划及其下组合
        	List<String> portCodes = new ArrayList<>();
        	portCodes.add(planCode);
        	cashFlowQueryParamDTO.setPortCode(portCodes);
        	cashFlowQueryParamDTO.setPortType(PortType.plan.getCode());
        	cashFlowQueryParamDTO.setPortCalcType(PortCalcType.compositeAndContrast.name());
        }
        
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("cashFlow", cashFlowQueryParamDTO);

        List<Map<String,Object>> newDataList = new ArrayList<Map<String,Object>>();
        if(resultBO.getSuccessful()) {
        	//调用成功,修改计划列名称,过滤现金流为0的数据
            List<Map<String,Object>> dataList = resultBO.getResultList();
            for(Map<String, Object> data : dataList) {
            	if (planCode.equals(data.get("portCode"))) {
            		data.put("pName", "横向统计");
            	}
            	Double net = NumberUtils.convertToDouble(data.get("gbPCfNet"));
            	if (BigDecimal.ZERO.compareTo(new BigDecimal(net)) != 0) {
            		newDataList.add(data);
            	}
            }
        }

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), newDataList);

        return baseResponse;
    }
	
	/**
	 * @Title: cashFlowTable
	 * @Description: 现金流表行转列
	 * @param: requestId
	 * @param: row2CellDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	@RequestMapping(value = "/cashFlowTable", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse cashFlowTable(String requestId, @RequestBody AnalysisRow2CellDTO row2CellDTO) {
        List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
		// 按日期排序
        row2CellDTO.setSortField("endDate");
        row2CellDTO.setSortType("desc");
        
        Row2CellTableDataFormat dataFormat = new Row2CellTableDataFormat();
        SimpleTableResultModel tableResultModel = (SimpleTableResultModel) dataFormat.doFormat(dataList, row2CellDTO);

        // 列排序
        List<TableColumnModel> newColumnModelList = new ArrayList<TableColumnModel>();
        TableColumnModel columnModelPlan = new TableColumnModel();
        List<TableColumnModel> columnModelList = tableResultModel.getColumnModelList();
        
        for (TableColumnModel columnModel : columnModelList) {
        	if (!"横向统计".equals(columnModel.getLabel())) {
        		newColumnModelList.add(columnModel);
        	} else {
        		columnModelPlan = columnModel;
        	}
        }
        newColumnModelList.add(columnModelPlan);
        
        HttpDynamicColumnListResult dynamicColumnListResult = new HttpDynamicColumnListResult(tableResultModel.getDataList(), tableResultModel.getTotal(), newColumnModelList);

        return HttpBaseResponseUtil.getSuccessResponse(dynamicColumnListResult);
    }

	/**
	 * @Title: calcPlanCashFlow
	 * @Description: 计划层申赎统计
	 * @param: cashFlowQueryParamDTO
	 * @return: HttpBaseResponse   
	 * @throws
	 */
	@AnalysisResponseCacheAble
    @RequestMapping(value = "/calcPlanCashFlow", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse calcPlanCashFlow(CashFlowQueryParamDTO cashFlowQueryParamDTO) {
		cashFlowQueryParamDTO.setLandMid(false);
        String planCode = cashFlowQueryParamDTO.getPlanCode();
        
        List<String> portCodes = new ArrayList<>();
        portCodes.add(planCode);
        cashFlowQueryParamDTO.setPortCode(portCodes);
        
        // 判断计划现金流数据来源--导入/复合
        TplanInfo plan = planInfoService.getPlanInfo(planCode);
        
        // 将计划当成组合计算,直接取值
        if (CalculateType.directCalc.getCode().equals(plan.getCalculateType())) {
            cashFlowQueryParamDTO.setPortType(PortType.port.getCode());
            cashFlowQueryParamDTO.setPortCalcType(PortCalcType.single.name());
        } else { // 计划复合计算
        	cashFlowQueryParamDTO.setPortType(PortType.plan.getCode());
        	cashFlowQueryParamDTO.setPortCalcType(PortCalcType.single.name());
        }
        
        // 指标计算
        AnalysisIndexCalcResultBO resultBO = analysisIndexCalcService.calc("cashFlow", cashFlowQueryParamDTO);

        HttpBaseResponse baseResponse = new HttpBaseResponse(resultBO.getCode(), resultBO.getMessage(), resultBO.getResultList());

        return baseResponse;
    }
}
