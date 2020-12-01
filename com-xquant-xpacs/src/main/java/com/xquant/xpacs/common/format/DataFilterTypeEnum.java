/**
 * ******************************************
 * 文件名称: DataFilterTypeEnum.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年03月27日 09:45:04
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

/**
 * @ClassName: DataFilterTypeEnum
 * @Description: 数据过滤枚举
 * @author: yt.zhou
 * @date: 2019年03月27日 09:45:04
 *
 */
public enum DataFilterTypeEnum {
    ALL("全部","ALL"),
    FILTER("过滤","FILTER")
    ;
    //名称
    private String name;
    //代码
    private String code;

    DataFilterTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
