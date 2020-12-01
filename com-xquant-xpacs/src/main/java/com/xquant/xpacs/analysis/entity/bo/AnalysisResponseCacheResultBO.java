/**
 * ******************************************
 * 文件名称: AnalysisResponseCacheResultBO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月28日 13:37:25
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.entity.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AnalysisResponseCacheResultBO
 * @Description: 分析功能缓存数据结果
 * @author: yt.zhou
 * @date: 2020年10月28日 13:37:25
 */
public class AnalysisResponseCacheResultBO implements Serializable {
    private static final long serialVersionUID = 44854009722202691L;
    /**缓存结果Id*/
    private String requestId;
    /**缓存数据，controller返回的数据*/
    private List<Map<String,Object>> data;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
