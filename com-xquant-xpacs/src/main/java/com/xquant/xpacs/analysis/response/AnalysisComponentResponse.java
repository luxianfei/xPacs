/**
 * ******************************************
 * 文件名称: AnalysisComponentResponse.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月30日 11:44:20
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.response;

import com.xquant.common.enums.EnumRespMsg;
import com.xquant.xpacs.common.format.BaseResultModel;

/**
 * @ClassName: AnalysisComponentResponse
 * @Description: 分析功能组件返回结果
 * @author: yt.zhou
 * @date: 2020年10月30日 11:44:20
 */
public class AnalysisComponentResponse {
    /**返回结果代码*/
    private String code;
    /**结果信息*/
    private String message;
    /**数据集*/
    private BaseResultModel result;

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

    public BaseResultModel getResult() {
        return result;
    }

    public void setResult(BaseResultModel result) {
        this.result = result;
    }

    public AnalysisComponentResponse() {
        this.code = EnumRespMsg.SUCCESS.getRespCode();
        this.message = EnumRespMsg.SUCCESS.getRespMsg();
    }

    public AnalysisComponentResponse(BaseResultModel result) {
        this.code = EnumRespMsg.SUCCESS.getRespCode();
        this.message = EnumRespMsg.SUCCESS.getRespMsg();
        this.result = result;
    }
}
