/**
 * ******************************************
 * 文件名称: ITcrpSubjectValueService.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月10日 14:12:10
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.inforQuery.service.api;

import com.xquant.xpacs.inforQuery.entity.bo.TcrpSubjectValueGroupBO;
import com.xquant.xpacs.inforQuery.entity.dto.SubjectValueDownLoadDTO;
import com.xquant.xpims.tcrp.entity.po.TcrpSubjectValue;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.List;

/**
 * @ClassName: ITcrpSubjectValueService
 * @Description: 估值表查询相关
 * @author: yt.zhou
 * @date: 2020年11月10日 14:12:10
 */
public interface ITcrpSubjectValueService {

    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 14:14
     * @description:    查询组合某天的估值信息
     * @param portCode  组合代码
     * @param tDate     日期
     * @return java.util.List<com.xquant.xpims.tcrp.entity.po.TcrpSubjectValue>
     *
     */
    List<TcrpSubjectValue> getTcrpSubjectValuePortCodeDate(String portCode, String tDate);

    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 15:03
     * @description:   查询估值信息，按导出excel模块分组，分成3块
     * @param portCode  组合代码
     * @param tDate     日期
     * @return com.xquant.xpacs.inforQuery.entity.bo.TcrpSubjectValueGroupBO
     *
     */
    TcrpSubjectValueGroupBO getSubjectValueGroupForExport(String portCode, String tDate);

    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 15:25
     * @description:    估值表excel生成
     * @param portCode  组合代码
     * @param tDate     日期
     *
     */
    Workbook buildSubjectValueExcel(String portCode, String tDate);

    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 17:50
     * @description: 创建excel
     * @param portCode  组合代码
     * @param tDate     日期
     *
     */
    void createExcel(String portCode, String tDate);

    /**
     * @author: yt.zhou
     * @date: 2020年11月11日 11:36
     * @description:    获取组合指定日期的估值表Excel文件
     * @param portCode  组合
     * @param tDate     日期
     * @return java.io.File
     *
     */
    File getSubjectValueExcel(String portCode, String tDate);

    /**
     * @author: yt.zhou
     * @date: 2020年11月11日 16:58
     * @description:  批量下载excel文件，并且打包成zip
     * @param subjectValueDownLoadDTOS
     * @return java.io.File
     *
     */
    List<File> bachCreateExcelForZip(List<SubjectValueDownLoadDTO> subjectValueDownLoadDTOS);
}
