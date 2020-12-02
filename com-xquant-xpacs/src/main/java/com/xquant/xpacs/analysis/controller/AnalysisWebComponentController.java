/**
 * ******************************************
 * 文件名称: AnalysisWebComponentController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月30日 15:17:46
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.controller;

import com.xquant.xpacs.analysis.enums.EnumWebComponent;
import com.xquant.xpacs.analysis.service.api.IAnalysisDataCacheService;
import com.xquant.xpacs.analysis.support.AnalysisResponse;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.common.format.model.AnalysisRow2CellDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AnalysisWebComponentController
 * @Description: 分析功能组件
 * @author: yt.zhou
 * @date: 2020年10月30日 15:17:46
 */
@RestController
@RequestMapping("/analysisWebComponent")
public class AnalysisWebComponentController {

    @Autowired
    private IAnalysisDataCacheService dataCacheService;
    /**
     * @author: yt.zhou
     * @date: 2020年10月30日 15:20
     * @description: 分析功能普通表格数据
     * @param   requestId 缓存ID，分析功能数据从缓存中读取数据
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    @AnalysisResponse(webComponent = EnumWebComponent.TABLE)
    @RequestMapping(value = "/defaultTable", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse defaultTable(String requestId) {
        List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getListResponse(dataList);
        return baseResponse;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年10月30日 15:20
     * @description: 分析功能普通表格数据
     * @param   requestId 缓存ID，分析功能数据从缓存中读取数据
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @see AnalysisRow2CellDTO
     *
     */
    @AnalysisResponse(webComponent = EnumWebComponent.ROW_2_CELL_TABLE)
    @RequestMapping(value = "/defaultRow2CellTable", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse defaultRow2CellTable(String requestId) {
        List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getListResponse(dataList);
        return baseResponse;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年10月30日 15:20
     * @description: 分析功能树形表格数据
     * @param   requestId 缓存ID，分析功能数据从缓存中读取数据
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    @AnalysisResponse(webComponent = EnumWebComponent.TREE_TABLE)
    @RequestMapping(value = "/defaultTreeTable", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse defaultTreeTable(String requestId) {
        List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getListResponse(dataList);
        return baseResponse;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月09日 14:30
     * @description:  echart走势图数据结果
     * @param requestId 缓存id,
     *                   更多post参数：@see com.xquant.xpacs.common.format.model.AnalysisChartDTO
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    @AnalysisResponse(webComponent = EnumWebComponent.E_CHART)
    @RequestMapping(value = "/defaultChart", method = { RequestMethod.GET, RequestMethod.POST })
    public HttpBaseResponse defaultChart(String requestId) {
        List<Map<String,Object>> dataList = dataCacheService.getCache(requestId);
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getListResponse(dataList);
        return baseResponse;
    }
}
