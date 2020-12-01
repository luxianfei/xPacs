/**
 * ******************************************
 * 文件名称: IAnalysisDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月30日 10:22:59
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IAnalysisDataFormat
 * @Description: 分析功能数据转换接口
 * @author: yt.zhou
 * @date: 2020年10月30日 10:22:59
 */
public interface IAnalysisDataFormat {
    /**
     * @author: yt.zhou
     * @date: 2020年10月30日 10:23
     * @description:    分析功能数据转换
     * @param dataList  数据集合
     * @param baseDTO   转换参数
     * @return com.xquant.xpims.common.format.BaseResultModel
     *
     */
    BaseResultModel doFormat(List<Map<String, Object>> dataList, AnalysisBaseDTO baseDTO);
}
