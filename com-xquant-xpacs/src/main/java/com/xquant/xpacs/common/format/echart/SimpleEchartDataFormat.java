/**
 * ******************************************
 * 文件名称: CommonEchartDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V5.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2019年03月21日 09:42:32
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.echart;

import com.xquant.common.util.StringUtils;
import com.xquant.xpacs.common.format.AbstractDataFormat;
import com.xquant.xpacs.common.format.BaseResultModel;
import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;
import com.xquant.xpacs.common.format.model.AnalysisChartDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName: SourceEchartDataFormat
 * @Description: echarts数据封装,数据结果为二维数组
 * @author: yt.zhou
 * @date: 2019年03月21日 09:42:32
 *
 */
public class SimpleEchartDataFormat extends AbstractDataFormat {
    //Echart图例模型集合，即一个图表包含的所有图例
    protected List<EchartLegendModel> detailModelList = new ArrayList<>();
    //x轴集合
    protected Set<String> xAxisSet = new LinkedHashSet<>();
    //图例+x轴的数据,即每个点对应的数据
    protected Map<String,Map> legendXaxis = new HashMap<>(1024);


    public SimpleEchartOption formatData(List<Map<String,Object>> list, List<EchartLegendModel> legendModelList){

        SimpleEchartOption option = new SimpleEchartOption();

        //2、先初始化数据
        initChartData(list,legendModelList);

        Object[][] source = new Object[xAxisSet.size()+1][];
        List<Map> series = new ArrayList<>();

        SimpleEchartOption.DataSet dataSet = new SimpleEchartOption.DataSet();

        dataSet.setSource(source);
        option.setDataset(dataSet);
        option.setSeries(series);
        /*option.setxAxis(new ArrayList<>());
        option.setyAxis(new ArrayList<>());*/

        //3、将数据转换成二维数组
        formatSource(option);

        /*formatYAxis(option);
        formatXAxis(option);*/

        //4、转换图形展现显示
        formatSeries(option);

        return option;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年02月01日 10:12
     * @description: 初始化echart需要的数据
     * @param list  数据集
     *
     */
    protected void initChartData(List<Map<String,Object>> list,List<EchartLegendModel> modelList) {
        Set<String> legendSet = new HashSet<>();
        Iterator<Map<String,Object>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String,Object> data = iterator.next();

            for (EchartLegendModel model : modelList) {
                if(!data.containsKey(model.getValKey())) {
                    continue;
                }
                String xAxis = StringUtils.defaultIfNull(data.get(model.getxAxisKey()));
                xAxisSet.add(xAxis);

                String legend = null;
                if ("value".equals(model.getLtype())) {
                    //图例是固定值的
                    legend = model.getLegend();
                } else if ("key".equals(model.getLtype())) {
                    //图例是从数据中获取的
                    legend = StringUtils.defaultIfNull(data.get(model.getLegend()));
                }

                if ("".equals(legend)) {
                    continue;
                }

                if(!data.containsKey(model.getValKey())) {
                    continue;
                }

                String lxKey = legend + "_" + xAxis;
                legendXaxis.put(lxKey, data);

                if(!legendSet.contains(legend)) {
                    legendSet.add(legend);

                    EchartLegendModel legendModel = new EchartLegendModel();
                    BeanUtils.copyProperties(model,legendModel);

                    legendModel.setLegend(legend);

                    detailModelList.add(legendModel);
                }
            }
        }
    }

    /**
     * @author: yt.zhou
     * @date: 2019年02月01日 12:59
     * @description:  数据格式封装，转换为二维数组
     *      二维数组Object[m][n],m=x轴数据量+1，n=图例个数+1
     * 		source: [
     *                  ['product', '2015', '2016', '2017'],
     *                  ['Matcha Latte', 43.3, 85.8, 93.7],
     *                  ['Milk Tea', 83.1, 73.4, 55.1],
     *                  ['Cheese Cocoa', 86.4, 65.2, 82.5],
     *                  ['Walnut Brownie', 72.4, 53.9, 39.1]
     *              ]
     * @return java.lang.Object[][]
     *
     */
    protected Object[][] formatSource(SimpleEchartOption option){

        DecimalFormat format = new DecimalFormat("#.######");
        format.setGroupingUsed(false);
        Object[][] source = option.getDataset().getSource();

        //图例排序
//        Collections.sort(detailModelList, Comparator.comparing(EchartLegendModel::getLegend));

        //二维数组第一个数据是 product + 图例
        Object[] source0 = new Object[detailModelList.size() + 1];
        source0[0] = "product";
        for(int i = 0;i<detailModelList.size();i++){
            source0[i+1] = detailModelList.get(i).getLegend();
        }
        source[0] = source0;

        //封装每个x轴坐标点的数据
        int t = 0;
        Iterator<String> it = xAxisSet.iterator();
        while (it.hasNext()){
            String xAxis = it.next();
            Object[] data = new Object[detailModelList.size() + 1];
            data[0] = xAxis;
            for(int i = 0; i<detailModelList.size();i++) {
                EchartLegendModel legendModel = detailModelList.get(i);
                String legend = legendModel.getLegend();
                String echartType = legendModel.getEchartType();
                //默认值，如果是线形图，用空，其他用0
                String defaultVal = "line".equals(echartType) ? "" : "0";
                String lxKey = legend+"_"+xAxis;
                Map map = legendXaxis.get(lxKey);
                if(map == null){
                    data[i+1] = defaultVal;
                } else {
                    Object val = map.get(legendModel.getValKey());
                    if(val == null || StringUtils.isBlank(val.toString())) {
                        data[i+1] = defaultVal;
                    } else {
                        BigDecimal decimalVal = new BigDecimal(val.toString());
                        if(legendModel.getFormat() != null) {
                            Double idxUnitRate = legendModel.getFormat().getIdxUnitRate();
                            if(idxUnitRate != null) {
                                decimalVal = decimalVal.multiply(new BigDecimal(idxUnitRate));
                            }
                        }
                        data[i+1] = format.format(decimalVal);
                    }
                }
            }
            source[++t] = data;
        }
        option.getDataset().setSource(source);
        return source;
    }

    public List<Map> formatSeries(SimpleEchartOption option){
        List<Map> series = option.getSeries();
        boolean isPie = false;
        for(int i = 0;i<detailModelList.size();i++) {
            EchartLegendModel legendModel = detailModelList.get(i);
            Map ser = new HashMap();
            ser.put("type",legendModel.getEchartType() == null ? "bar" : legendModel.getEchartType());
            ser.put("yAxisIndex",legendModel.getyAxisIndex());

            if(legendModel.getStack() != null) {
                //堆叠
                ser.put("stack",legendModel.getStack());
                ser.put("areaStyle","{}");
            }
            if("line".equals(legendModel.getEchartType())) {
                //线型图，支持断点续连
                ser.put("connectNulls","true");
                ser.put("symbol","none");
            }
            if("pie".equals(legendModel.getEchartType())) {
                ser.put("radius","65%");
                //饼图不显示图例
//                option.setLegend(null);
                //饼图，不显示X,Y轴
                /*for(Map xMap : option.getxAxis()) {
                    xMap.put("show",false);
                }

                for(Map yMap : option.getyAxis()) {
                    yMap.put("show",false);
                }*/
                isPie = true;
            }

            series.add(ser);
        }

        option.setSeries(series);
        /*if(isPie) {
            //饼图鼠标触发条件
            Map<String,Object> toolTip = new HashMap<>();
            toolTip.put("trigger","item");
            option.setTooltip(toolTip);
        }*/
        return series;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年04月01日 14:59
     * @description:  是否两个Y轴，封装yAxis
     * @return java.util.List<java.util.Map>
     *
     */
    /*public List<Map> formatYAxis(SimpleEchartOption option) {
        //如果X,Y轴互换，即Y轴作为横轴
        List<Map> yAxis = option.getyAxis();
        boolean doubleY = false;
        for(int i = 0;i<detailModelList.size();i++) {
            if(detailModelList.get(i).getyAxisIndex()==1){
                doubleY = true;
            }
        }
        yAxis.add(new HashMap());
        //两个Y轴
        if(doubleY) {
            yAxis.add(new HashMap());
        }

        option.setyAxis(yAxis);
        return yAxis;
    }*/

    /**
     * @author: yt.zhou
     * @date: 2019年04月10日 11:10
     * @description:  x轴类型，默认是以x轴作为横轴
     * @return java.lang.Object
     *
     */
    /*public List<Map> formatXAxis(SimpleEchartOption option) {
        List<Map> xAxisList = option.getxAxis();
        //如果X,Y轴互换，即Y轴作为横轴
        Map xAxis = new HashMap();
        xAxis.put("type","category");
        xAxisList.add(xAxis);

        option.setxAxis(xAxisList);
        return xAxisList;
    }*/

    @Override
    protected BaseResultModel doComponentFormat(List<Map<String, Object>> list, AnalysisBaseDTO baseDTO) {
        return this.formatData(list,((AnalysisChartDTO) baseDTO).getLegends());
    }
}
