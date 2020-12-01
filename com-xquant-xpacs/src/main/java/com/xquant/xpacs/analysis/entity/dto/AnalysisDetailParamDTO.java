/**
 * ******************************************
 * 文件名称: AnalysisDetailParamDTO.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月27日 13:11:42
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.entity.dto;

import java.util.List;

/**
 * @ClassName: AnalysisDetailParamDTO
 * @Description: 明细
 * @author: yt.zhou
 * @date: 2020年10月27日 13:11:42
 */
public class AnalysisDetailParamDTO extends AnalysisBaseParamDTO {
    /**金融工具i_code,a_type,m_type拼接*/
    private List<String> instrumentList;
    /**金融工具是否拆分，0不拆分，1拆分*/
    private int isAssetSplit;

    public List<String> getInstrumentList() {
        return instrumentList;
    }

    public void setInstrumentList(List<String> instrumentList) {
        this.instrumentList = instrumentList;
    }

    public int getIsAssetSplit() {
        return isAssetSplit;
    }

    public void setIsAssetSplit(int isAssetSplit) {
        this.isAssetSplit = isAssetSplit;
    }
}
