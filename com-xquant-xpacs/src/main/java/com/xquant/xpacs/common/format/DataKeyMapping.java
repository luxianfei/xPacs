/**
 * ******************************************
 * 文件名称: DataKeyMapping.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月08日 15:12:48
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

/**
 * @ClassName: DataKeyMapping
 * @Description: 指标字段映射
 * @author: yt.zhou
 * @date: 2020年07月08日 15:12:48
 */
public class DataKeyMapping {
    /**源字段名称*/
    private String name;
    /**映射字段名称*/
    private String mapping;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
