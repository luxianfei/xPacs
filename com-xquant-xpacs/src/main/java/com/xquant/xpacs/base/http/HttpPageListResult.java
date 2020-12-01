/**
 * ******************************************
 * 文件名称: HttpPageListResult.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 11:24:48
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.http;

import java.util.List;

/**
 * @ClassName: HttpPageListResult
 * @Description: 分页集合数据结果
 * @author: yt.zhou
 * @date: 2020年11月03日 11:24:48
 */
public class HttpPageListResult extends HttpListResult {
    /**结果总数*/
    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public HttpPageListResult(List data) {
        super(data);
        this.count = data.size();
    }

    public HttpPageListResult(List data, long count) {
        super(data);
        this.count = count;
    }
}
