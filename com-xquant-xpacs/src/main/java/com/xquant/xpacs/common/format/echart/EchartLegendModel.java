/**
 * ******************************************
 * 文件名称: LegendValModel.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月21日 11:16:33
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.echart;

import com.xquant.common.bean.bo.TableColumnModel;

/**
 * @ClassName: LegendValModel
 * @Description: Echart图表模型
 * @author: yt.zhou
 * @date: 2019年03月21日 11:16:33
 *
 */
public class EchartLegendModel {
    //图例取值字段
    private String legend;
    //当前图例对应的取值字段
    private String valKey;
    //标识图例是固定的值，还是从数据中动态获取的值，key表示动态获取，value表示固定值
    private String ltype = "key";
    //图例展示的图表类型，bar,line,pie
    private String echartType;
    //对应Y轴的下标，0左轴，1右轴
    private int yAxisIndex = 0;
    //堆叠，相同的值的图例进行堆叠
    private String stack;
    //x轴取值key
    private String xAxisKey;

    private TableColumnModel.Format format;

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getValKey() {
        return valKey;
    }

    public void setValKey(String valKey) {
        this.valKey = valKey;
    }

    public String getLtype() {
        return ltype;
    }

    public void setLtype(String ltype) {
        this.ltype = ltype;
    }

    public String getEchartType() {
        return echartType;
    }

    public void setEchartType(String echartType) {
        this.echartType = echartType;
    }

    public int getyAxisIndex() {
        return yAxisIndex;
    }

    public void setyAxisIndex(int yAxisIndex) {
        this.yAxisIndex = yAxisIndex;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getxAxisKey() {
        return xAxisKey;
    }

    public void setxAxisKey(String xAxisKey) {
        this.xAxisKey = xAxisKey;
    }

    public TableColumnModel.Format getFormat() {
        return format;
    }

    public void setFormat(TableColumnModel.Format format) {
        this.format = format;
    }
}
