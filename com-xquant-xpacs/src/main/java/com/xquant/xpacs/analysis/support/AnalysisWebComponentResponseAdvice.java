/**
 * ******************************************
 * 文件名称: AnalysisWebComponentResponseAdvice.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月30日 15:28:17
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.support;

import com.xquant.common.bean.HttpListResult;
import com.xquant.common.bean.HttpPageListResult;
import com.xquant.common.bean.bo.TableColumnModel;
import com.xquant.common.bean.bo.TreeTableDataModel;
import com.xquant.common.util.UUIDUtil;
import com.xquant.common.util.excel.ExcelExportUtils;
import com.xquant.xpacs.analysis.enums.EnumWebComponent;
import com.xquant.xpacs.analysis.wrapper.AnalysisDataAdapter;
import com.xquant.xpacs.base.http.*;
import com.xquant.xpacs.common.format.BaseResultModel;
import com.xquant.xpacs.common.format.echart.SimpleEchartOption;
import com.xquant.xpacs.common.format.model.AnalysisGridPageDTO;
import com.xquant.xpacs.common.format.table.SimpleTableResultModel;
import com.xquant.xpacs.common.format.table.TreeTableResultModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AnalysisWebComponentResponseAdvice
 * @Description: 分析功能组件处理
 * @author: yt.zhou
 * @date: 2020年10月30日 15:28:17
 */
@ControllerAdvice
public class AnalysisWebComponentResponseAdvice implements ResponseBodyAdvice<Object> {
    private static final String EXPORT_FLAG = "1";
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if(returnType.getMethod().isAnnotationPresent(AnalysisResponse.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        AnalysisResponse analysisResponse = returnType.getMethod().getAnnotation(AnalysisResponse.class);

        HttpBaseResponse httpBaseResponse = HttpBaseResponseUtil.getSuccessResponse();

        if(body instanceof HttpBaseResponse) {
            //组件类型
            EnumWebComponent webComponent = analysisResponse.webComponent();

            //通过不同的组件类型，将结果集转换成不同的数据结果类型
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

            ServletWebRequest webRequest = new ServletWebRequest(servletRequest, servletResponse);
            AnalysisDataAdapter analysisDataAdapter = webComponent.getAnalysisDataAdapter(webRequest);

            HttpBaseResponse controllerBody = ((HttpBaseResponse) body);
            Object cResult = controllerBody.getResult();
            if(cResult instanceof HttpListResult) {
                // controller返回的是集合数据
                HttpListResult listResult = ((HttpListResult) cResult);

                BaseResultModel baseResultModel = null;
                if(analysisResponse.isAnalysis()) {
                    //分析功能，需要再次对数据做加工
                    List<Map<String, Object>> dataList = listResult.getData();
                    baseResultModel = analysisDataAdapter.doFormat(dataList);
                } else {
                    //非分析功能，直接对数据做处理
                    if(webComponent == EnumWebComponent.TABLE || webComponent == EnumWebComponent.ROW_2_CELL_TABLE) {
                        baseResultModel = new SimpleTableResultModel();
                        ((SimpleTableResultModel) baseResultModel).setDataList(listResult.getData());
                        if(listResult instanceof HttpPageListResult) {
                            ((SimpleTableResultModel) baseResultModel).setTotal((int)((HttpPageListResult) listResult).getCount());
                        }
                    } else if (webComponent == EnumWebComponent.TREE_TABLE) {
                        //树形表格
                        baseResultModel = new TreeTableResultModel();
                        ((TreeTableResultModel) baseResultModel).setDataList(listResult.getData());
                    } else if (webComponent == EnumWebComponent.E_CHART) {
                        baseResultModel = analysisDataAdapter.doFormat(listResult.getData());
                    }
                }



                String export = servletRequest.getParameter("export");
                if(EXPORT_FLAG.equals(export)) {
                    //导出
                    Workbook workbook = null;
                    if(webComponent == EnumWebComponent.TABLE || webComponent == EnumWebComponent.ROW_2_CELL_TABLE) {

                        //简单表格, 行转列表格，获取数据集
                        SimpleTableResultModel simpleTableResultModel = (SimpleTableResultModel)baseResultModel;
                        List<Map<String,Object>> componentDataList = simpleTableResultModel.getDataList();

                        List<Object> objectList = new ArrayList<>(componentDataList);
                        List<TableColumnModel> columnModels;
                        if(webComponent == EnumWebComponent.TABLE) {
                            //普通表格，列直接拿前端传过来的列信息
                            columnModels = ((AnalysisGridPageDTO)analysisDataAdapter.getAnalysisBaseDTO()).getTableColumns();
                        } else {
                            //行转列表格，需要拿转换后的列
                            columnModels = simpleTableResultModel.getColumnModelList();
                        }

                        workbook = ExcelExportUtils.buildSimpleTableHSSFWorkbook(columnModels, objectList);

                    } else if (webComponent == EnumWebComponent.TREE_TABLE) {
                        //行转列表格，获取数据集
                        TreeTableResultModel treeTableResultModel = (TreeTableResultModel) baseResultModel;

                        List<TreeTableDataModel> treeTableDataModels = treeTableResultModel.getDataList();
                        List<TableColumnModel> columnModels = treeTableResultModel.getColumnModelList();

                        workbook = ExcelExportUtils.buildTreeTableHSSFWorkbook(columnModels, treeTableDataModels);
                    }


                    String fileName = UUIDUtil.getUUID() + ".xlsx";

                    ExcelExportUtils.downLoadExcel(workbook,fileName,servletResponse);

                    return null;
                } else {
                    if(webComponent == EnumWebComponent.TABLE) {
                        //普通表格数据
                        SimpleTableResultModel resultModel = (SimpleTableResultModel) baseResultModel;
                        httpBaseResponse = HttpBaseResponseUtil.getPageListResponse(resultModel.getDataList(), resultModel.getTotal());
                    } else if (webComponent == EnumWebComponent.ROW_2_CELL_TABLE) {
                        //行转列表格数据
                        SimpleTableResultModel resultModel = (SimpleTableResultModel) baseResultModel;
                        HttpDynamicColumnListResult dynamicColumnListResult =
                                new HttpDynamicColumnListResult(resultModel.getDataList(), resultModel.getTotal(), resultModel.getColumnModelList());

                        httpBaseResponse = HttpBaseResponseUtil.getSuccessResponse(dynamicColumnListResult);
                    } else if (webComponent == EnumWebComponent.TREE_TABLE) {
                        //树形表格数据
                        TreeTableResultModel resultModel = (TreeTableResultModel) baseResultModel;
                        List<TreeTableDataModel> dataModelList = resultModel.getDataList();
                        HttpListResult httpListResult = new HttpListResult<>(dataModelList);
                        httpBaseResponse = new HttpBaseResponse(httpListResult);
                    } else if (webComponent == EnumWebComponent.E_CHART) {
                        //echart图表
                        SimpleEchartOption echartOption = (SimpleEchartOption) baseResultModel;
                        httpBaseResponse = HttpBaseResponseUtil.getSuccessResponse(echartOption);
                    }

                    return httpBaseResponse;
                }
            }

        }

        return httpBaseResponse;
    }
}
