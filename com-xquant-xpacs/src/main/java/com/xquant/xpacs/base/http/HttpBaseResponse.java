/**
 * ******************************************
 * 文件名称: BaseResponse.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 11:08:55
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.http;

import com.xquant.common.enums.EnumRespMsg;

import java.util.HashMap;

/**
 * @ClassName: BaseResponse
 * @Description: 基础返回结果，返回给前端的结果
 * @author: yt.zhou
 * @date: 2020年11月03日 11:08:55
 */
public class HttpBaseResponse {
    /**结果代码*/
    private String code;
    /**结果信息*/
    private String message;
    /**结果集，{ key:value,}*/
    private Object result;
    /**是否成功*/
    private boolean success;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return EnumRespMsg.SUCCESS.getRespCode().equals(this.code);
    }

    public HttpBaseResponse() {
        this(EnumRespMsg.SUCCESS.getRespCode(), EnumRespMsg.SUCCESS.getRespMsg());
    }

    public HttpBaseResponse(String code, String message) {
        this(code, message, new HashMap<>());
    }

    public HttpBaseResponse(String code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public HttpBaseResponse(Object result) {
        this(EnumRespMsg.SUCCESS.getRespCode(), EnumRespMsg.SUCCESS.getRespMsg(), result);
    }
}
