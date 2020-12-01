/**
 * ******************************************
 * 文件名称: AnalysisIndexCalcResultBO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月20日 09:33:50
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.entity.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AnalysisIndexCalcResultBO
 * @Description: 分析功能指标方案计算结果
 * @author: yt.zhou
 * @date: 2020年10月20日 09:33:50
 */
public class AnalysisIndexCalcResultBO implements Serializable {
    private static final long serialVersionUID = -4566492762262803432L;
    /**结果代码 000000成功*/
    private String code;
    /**结果信息*/
    private String message;
    /**是否成功*/
    private Boolean successful;
    /**结果数据集*/
    private List<Map<String,Object>> resultList;
    /**数据缓存Id*/
    private String requestId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public List<Map<String, Object>> getResultList() {
        return resultList;
    }

    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
