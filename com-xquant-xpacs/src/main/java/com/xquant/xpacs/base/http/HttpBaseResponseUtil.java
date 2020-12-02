/**
 * ******************************************
 * 文件名称: HttpBaseResponseUtil.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 13:32:19
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.http;

import com.xquant.common.bean.HttpListResult;
import com.xquant.common.bean.HttpPageListResult;
import com.xquant.common.enums.EnumRespMsg;

import java.util.List;

/**
 * @ClassName: HttpBaseResponseUtil
 * @Description: http结果对象
 * @author: yt.zhou
 * @date: 2020年11月03日 13:32:19
 */
public class HttpBaseResponseUtil {
    /**
     * @author: yt.zhou
     * @date: 2020年11月03日 11:27
     * @description: 无数据集返回的成功结果
     *
     */
    public static HttpBaseResponse getSuccessResponse() {
        return new HttpBaseResponse();
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月03日 11:27
     * @description: 有数据集返回的结果
     *
     */
    public static <T> HttpBaseResponse getSuccessResponse(T result) {
        return new HttpBaseResponse(result);
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月03日 11:27
     * @description: 集合数据结果
     *
     */
    public static <T> HttpBaseResponse getListResponse(List<T> dataList) {
        return new HttpBaseResponse(new HttpListResult<>(dataList));
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月03日 11:27
     * @description: 分页数据结果
     * @param dataList  数据集
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    public static <T> HttpBaseResponse getPageListResponse(List<T> dataList) {
        return new HttpBaseResponse(new HttpPageListResult(dataList));
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月03日 11:28
     * @description:  分页数据结果，并且带总塑量
     * @param dataList  数据集
     * @param count     总数
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    public static <T> HttpBaseResponse getPageListResponse(List<T> dataList, long count) {
        return new HttpBaseResponse(new HttpPageListResult(dataList, count));
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月16日 19:26
     * @description:  默认失败结果
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    public static HttpBaseResponse getFailResponse() {
        return new HttpBaseResponse(EnumRespMsg.ERROR.getRespCode(), EnumRespMsg.ERROR.getRespMsg());
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月16日 19:24
     * @description:    业务处理异常的结果
     * @param message   结果信息
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    public static HttpBaseResponse getFailResponse(String message) {
        return new HttpBaseResponse(EnumRespMsg.ERROR.getRespCode(), message);
    }
}
