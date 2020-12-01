/**
 * ******************************************
 * 文件名称: AnalysisChartReq.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月01日 17:50:15
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.model;

import com.xquant.xpacs.common.format.echart.EchartLegendModel;

import java.util.List;

/**
 * @ClassName: AnalysisChartReq
 * @Description: echart图表参数
 * @author: yt.zhou
 * @date: 2020年07月01日 17:50:15
 */
public class AnalysisChartDTO extends AnalysisBaseDTO {
    /**图例集合信息*/
    private List<EchartLegendModel> legends;

    public List<EchartLegendModel> getLegends() {
        return legends;
    }

    public void setLegends(List<EchartLegendModel> legends) {
        this.legends = legends;
    }
}
