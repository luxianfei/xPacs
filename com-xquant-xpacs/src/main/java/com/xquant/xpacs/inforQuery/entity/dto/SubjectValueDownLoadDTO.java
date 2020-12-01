/**
 * ******************************************
 * 文件名称: SubjectValueDownLoadDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月11日 16:54:48
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.inforQuery.entity.dto;

/**
 * @ClassName: SubjectValueDownLoadDTO
 * @Description: 估值表下载参数
 * @author: yt.zhou
 * @date: 2020年11月11日 16:54:48
 */
public class SubjectValueDownLoadDTO {
    /**组合代码*/
    private String portCode;
    /**估值日期*/
    private String tDate;

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }
}
