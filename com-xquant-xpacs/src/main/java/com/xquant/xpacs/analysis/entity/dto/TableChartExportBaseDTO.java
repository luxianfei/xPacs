/**
 *******************************************
 * 文件名称: TableChartExportBaseDTO.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 表格及图片导出成excel基础参数
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月23日 上午10:28:45
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.analysis.entity.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TableChartExportBaseDTO
 * @Description: 表格及图片导出成excel基础参数
 * @author: jingru.jiang
 * @date: 2020年11月23日 上午10:28:45
 *
 */
public class TableChartExportBaseDTO {
	/**缓存数据ID*/
    private List<String> requestIds;
    /**图片base64字符集*/
    private List<String> chartDatas;

    public List<String> getRequestIds() {
        return requestIds;
    }

    public void setRequestIds(List<String> requestIds) {
        this.requestIds = requestIds;
    }

    public List<String> getChartDatas() {
    	if (chartDatas.isEmpty()) {
    		return chartDatas;
    	}
    	List<String> chartDataList = new ArrayList<String>();
    	for (String chartData : chartDatas) {
    		String base64Code = chartData.substring(22);
    		chartDataList.add(base64Code);
    	}
    	return chartDataList;
    }

    public void setChartDatas(List<String> chartDatas) {
        this.chartDatas = chartDatas;
    }
}
