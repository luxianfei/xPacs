/**
 * ******************************************
 * 文件名称: AnalysisDataResolver.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月30日 10:33:21
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.wrapper;

import com.xquant.xpacs.common.format.BaseResultModel;
import com.xquant.xpacs.common.format.IAnalysisDataFormat;
import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AnalysisDataResolver
 * @Description: 分析功能数据解析器
 * @author: yt.zhou
 * @date: 2020年10月30日 10:33:21
 */
public class AnalysisDataAdapter {
    /**数据转换器*/
    private IAnalysisDataFormat analysisDataFormat;
    /**转换参数*/
    private AnalysisBaseDTO analysisBaseDTO;

    public IAnalysisDataFormat getAnalysisDataFormat() {
        return analysisDataFormat;
    }

    public void setAnalysisDataFormat(IAnalysisDataFormat analysisDataFormat) {
        this.analysisDataFormat = analysisDataFormat;
    }

    public AnalysisBaseDTO getAnalysisBaseDTO() {
        return analysisBaseDTO;
    }

    public void setAnalysisBaseDTO(AnalysisBaseDTO analysisBaseDTO) {
        this.analysisBaseDTO = analysisBaseDTO;
    }

    public AnalysisDataAdapter(IAnalysisDataFormat analysisDataFormat, AnalysisBaseDTO analysisBaseDTO) {
        this.analysisDataFormat = analysisDataFormat;
        this.analysisBaseDTO = analysisBaseDTO;
    }

    /***
     * @author: yt.zhou
     * @date: 2020年10月30日 10:36
     * @description: 数据转换
     * @param dataList
     * @return com.xquant.xpims.common.format.BaseResultModel
     *
     */
    public BaseResultModel doFormat(List<Map<String,Object>> dataList) {
        return analysisDataFormat.doFormat(dataList,analysisBaseDTO);
    }
}
