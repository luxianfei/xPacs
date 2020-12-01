/**
 * ******************************************
 * 文件名称: SimpleEchartOption.java
 * 系统名称: xALMS资产负债管理与量化分析系统
 * 模块名称: 量化报送
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年07月09日 09:48:23
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.echart;

import com.xquant.xpacs.common.format.BaseResultModel;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SimpleEchartOption
 * @Description: 图表option配置
 * @author: yt.zhou
 * @date: 2019年07月09日 09:48:23
 *
 */
public class SimpleEchartOption extends BaseResultModel {
    //数据集source
    private DataSet dataset;
    //图表信息，series
    private List<Map> series;
    /*//y轴
    private List<Map> yAxis;
    //x轴
    private List<Map> xAxis;
    //图表标题
    private Map<String,Object> title;
    //鼠标事件
    private Map<String,Object> tooltip;
    //图例配置
    private Map<String,Object> legend;
    */
//    private Map<String,Object> grid;

    public DataSet getDataset() {
        return dataset;
    }

    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
    }

    public List<Map> getSeries() {
        return series;
    }

    public void setSeries(List<Map> series) {
        this.series = series;
    }

    public static class DataSet{
        private Object[][] source;

        public Object[][] getSource() {
            return source;
        }

        public void setSource(Object[][] source) {
            this.source = source;
        }
    }
}
