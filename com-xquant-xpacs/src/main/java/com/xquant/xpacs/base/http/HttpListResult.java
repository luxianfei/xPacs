/**
 * ******************************************
 * 文件名称: HttpListResult.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月03日 11:22:06
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.http;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: HttpListResult
 * @Description: 集合数据集
 * @author: yt.zhou
 * @date: 2020年11月03日 11:22:06
 */
public class HttpListResult<T> implements Serializable {

	private static final long serialVersionUID = -8385228155894126329L;
	private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public HttpListResult(List<T> data) {
        this.data = data;
    }

	public HttpListResult() {
		super();
	}
    
    
}
