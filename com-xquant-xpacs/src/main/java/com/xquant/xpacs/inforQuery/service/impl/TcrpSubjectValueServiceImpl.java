/**
 * ******************************************
 * 文件名称: TcrpSubjectValueServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月10日 14:12:32
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.inforQuery.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xquant.base.param.entity.po.SysParam;
import com.xquant.base.param.service.api.SysParamService;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.StringUtils;
import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpacs.common.format.table.TableColumnModel;
import com.xquant.xpacs.inforQuery.entity.bo.TcrpSubjectValueGroupBO;
import com.xquant.xpacs.inforQuery.entity.dto.SubjectValueDownLoadDTO;
import com.xquant.xpacs.inforQuery.service.api.ITcrpSubjectValueService;
import com.xquant.xpims.tcrp.entity.po.TcrpSubjectValue;
import com.xquant.xpims.tcrp.entity.po.TcrpSubjectValueExample;
import com.xquant.xpims.tcrp.entity.po.ext.TcrpSubjectValueExt;
import com.xquant.xpims.tcrp.mapper.TcrpSubjectValueMapper;
import com.xquant.xpims.tprt.entity.po.Tprt;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: TcrpSubjectValueServiceImpl
 * @Description: 估值表查询
 * @author: yt.zhou
 * @date: 2020年11月10日 14:12:32
 */
@Service
public class TcrpSubjectValueServiceImpl implements ITcrpSubjectValueService {

    @Autowired
    private TcrpSubjectValueMapper subjectValueMapper;
    @Autowired
    private ITprtService tprtService;
    @Autowired
    private SysParamService sysParamService;

    @Override
    public List<TcrpSubjectValue> getTcrpSubjectValuePortCodeDate(String portCode, String tDate) {
        TcrpSubjectValueExample example = new TcrpSubjectValueExample();
        example.createCriteria().andPortCodeEqualTo(portCode).andTDateEqualTo(tDate);
        example.setOrderByClause("acct_code");
        List<TcrpSubjectValue> subjectValues = subjectValueMapper.selectByExample(example);
        return subjectValues;
    }

    @Override
    public TcrpSubjectValueGroupBO getSubjectValueGroupForExport(String portCode, String tDate) {
        List<TcrpSubjectValue> subjectValues = this.getTcrpSubjectValuePortCodeDate(portCode, tDate);
        List<TcrpSubjectValue> subjectValues1 = new ArrayList<>();
        List<TcrpSubjectValue> subjectValues2 = new ArrayList<>();
        List<TcrpSubjectValue> subjectValues3 = new ArrayList<>();
        for(TcrpSubjectValue subjectValue : subjectValues) {

            TcrpSubjectValueExt subjectValueExt = new TcrpSubjectValueExt();

            BeanUtils.copyProperties(subjectValue, subjectValueExt);

            if("0".equals(subjectValueExt.getIsTotal())) {
                subjectValues1.add(subjectValueExt);
            } else {
                if(!subjectValueExt.getAcctCode().startsWith("9")) {
                    //9科目开头的 作为最后一块内容
                    subjectValues2.add(subjectValueExt);
                } else {
                    subjectValues3.add(subjectValueExt);
                }
            }
        }
        TcrpSubjectValueGroupBO subjectValueGroupBO = new TcrpSubjectValueGroupBO();
        subjectValueGroupBO.setSubjectValues1(subjectValues1);
        subjectValueGroupBO.setSubjectValues2(subjectValues2);
        subjectValueGroupBO.setSubjectValues3(subjectValues3);
        return subjectValueGroupBO;
    }

    @Override
    public Workbook buildSubjectValueExcel(String portCode, String tDate) {

        Tprt tprt = tprtService.getTprtByPortCode(portCode);



        XSSFWorkbook workbook = new XSSFWorkbook();

        //样式1，标题样式
        XSSFCellStyle s1 = workbook.createCellStyle();
        s1.setAlignment(HorizontalAlignment.CENTER);
        s1.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setUnderline(Font.U_SINGLE);
        font.setFontHeightInPoints((short) 20);
        //0,128,0
        XSSFColor xssfColor = new XSSFColor(new java.awt.Color(0, 128, 0), new DefaultIndexedColorMap());
        ((XSSFFont) font).setColor(xssfColor);
        s1.setFont(font);


        //样式2，9号字体
        XSSFCellStyle s2 = workbook.createCellStyle();
        //9号字体
        Font fontSize9 = workbook.createFont();
        fontSize9.setFontHeightInPoints((short) 9);
        fontSize9.setFontName("宋体");
        s2.setFont(fontSize9);
        s2.setVerticalAlignment(VerticalAlignment.CENTER);
        s2.setBorderTop(BorderStyle.THIN);
        s2.setBorderBottom(BorderStyle.THIN);
        s2.setBorderLeft(BorderStyle.THIN);
        s2.setBorderRight(BorderStyle.THIN);
        s2.setBottomBorderColor(xssfColor);
        s2.setTopBorderColor(xssfColor);
        s2.setLeftBorderColor(xssfColor);
        s2.setRightBorderColor(xssfColor);

        //样式2，9号字体,数字
        XSSFCellStyle s3 = workbook.createCellStyle();
        s3.setFont(fontSize9);
        s3.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFDataFormat dataFormat = workbook.createDataFormat();
        s3.setDataFormat(dataFormat.getFormat("#,##0.00"));
        s3.setBorderTop(BorderStyle.THIN);
        s3.setBorderBottom(BorderStyle.THIN);
        s3.setBorderLeft(BorderStyle.THIN);
        s3.setBorderRight(BorderStyle.THIN);
        s3.setBottomBorderColor(xssfColor);
        s3.setTopBorderColor(xssfColor);
        s3.setLeftBorderColor(xssfColor);
        s3.setRightBorderColor(xssfColor);

        //9号加粗字体
        XSSFCellStyle s4 = workbook.createCellStyle();
        Font fontSize9Blod = workbook.createFont();
        fontSize9Blod.setBold(true);
        fontSize9Blod.setFontHeightInPoints((short)9);
        fontSize9Blod.setFontName("宋体");
        s4.setFont(fontSize9Blod);
        s4.setVerticalAlignment(VerticalAlignment.CENTER);
        s4.setBorderTop(BorderStyle.THIN);
        s4.setBorderBottom(BorderStyle.THIN);
        s4.setBorderLeft(BorderStyle.THIN);
        s4.setBorderRight(BorderStyle.THIN);
        s4.setBottomBorderColor(xssfColor);
        s4.setTopBorderColor(xssfColor);
        s4.setLeftBorderColor(xssfColor);
        s4.setRightBorderColor(xssfColor);

        //6号字体
        XSSFCellStyle s5 = workbook.createCellStyle();
        Font fontSize6 = workbook.createFont();
        fontSize6.setFontHeightInPoints((short)6);
        fontSize6.setFontName("宋体");
        s5.setFont(fontSize6);
        s5.setVerticalAlignment(VerticalAlignment.CENTER);

        //11号字体，绿色
        XSSFCellStyle s6 = workbook.createCellStyle();
        Font fontSize11 = workbook.createFont();
        fontSize11.setFontHeightInPoints((short)11);
        fontSize11.setFontName("宋体");
        ((XSSFFont) fontSize11).setColor(xssfColor);
        s6.setFont(fontSize11);
        s6.setVerticalAlignment(VerticalAlignment.CENTER);

        //样式7，9号字体, 上边双边框
        XSSFCellStyle s7 = workbook.createCellStyle();
        s7.setFont(fontSize9);
        s7.setVerticalAlignment(VerticalAlignment.CENTER);
        s7.setBorderTop(BorderStyle.DOUBLE);
        s7.setBorderBottom(BorderStyle.THIN);
        s7.setBorderLeft(BorderStyle.THIN);
        s7.setBorderRight(BorderStyle.THIN);
        s7.setBottomBorderColor(xssfColor);
        s7.setTopBorderColor(xssfColor);
        s7.setLeftBorderColor(xssfColor);
        s7.setRightBorderColor(xssfColor);

        //样式8 右边双边框
        XSSFCellStyle s8 = workbook.createCellStyle();
        s8.setFont(fontSize9);
        s8.setVerticalAlignment(VerticalAlignment.CENTER);
        s8.setBorderTop(BorderStyle.THIN);
        s8.setBorderBottom(BorderStyle.THIN);
        s8.setBorderLeft(BorderStyle.THIN);
        s8.setBorderRight(BorderStyle.DOUBLE);
        s8.setBottomBorderColor(xssfColor);
        s8.setTopBorderColor(xssfColor);
        s8.setLeftBorderColor(xssfColor);
        s8.setRightBorderColor(xssfColor);

        //样式9 右，上双边框
        XSSFCellStyle s9 = workbook.createCellStyle();
        s9.setFont(fontSize9);
        s9.setVerticalAlignment(VerticalAlignment.CENTER);
        s9.setBorderTop(BorderStyle.DOUBLE);
        s9.setBorderBottom(BorderStyle.THIN);
        s9.setBorderLeft(BorderStyle.THIN);
        s9.setBorderRight(BorderStyle.DOUBLE);
        s9.setBottomBorderColor(xssfColor);
        s9.setTopBorderColor(xssfColor);
        s9.setLeftBorderColor(xssfColor);
        s9.setRightBorderColor(xssfColor);

        //样式9 下双边框
        XSSFCellStyle s10 = workbook.createCellStyle();
        s10.setFont(fontSize9);
        s10.setVerticalAlignment(VerticalAlignment.CENTER);
        s10.setBorderTop(BorderStyle.THIN);
        s10.setBorderBottom(BorderStyle.DOUBLE);
        s10.setBorderLeft(BorderStyle.THIN);
        s10.setBorderRight(BorderStyle.THIN);
        s10.setBottomBorderColor(xssfColor);
        s10.setTopBorderColor(xssfColor);
        s10.setLeftBorderColor(xssfColor);
        s10.setRightBorderColor(xssfColor);

        //样式9 右，下双边框
        XSSFCellStyle s11 = workbook.createCellStyle();
        s11.setFont(fontSize9);
        s11.setVerticalAlignment(VerticalAlignment.CENTER);
        s11.setBorderTop(BorderStyle.THIN);
        s11.setBorderBottom(BorderStyle.DOUBLE);
        s11.setBorderLeft(BorderStyle.THIN);
        s11.setBorderRight(BorderStyle.DOUBLE);
        s11.setBottomBorderColor(xssfColor);
        s11.setTopBorderColor(xssfColor);
        s11.setLeftBorderColor(xssfColor);
        s11.setRightBorderColor(xssfColor);


        XSSFSheet sheet = workbook.createSheet();
        //隐藏网格线
        sheet.setDisplayGridlines(false);

        //第一行标题，对应excel中1-2行
        Row row0 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);

        String title = tprt.getpName() + "组合委托资产资产估值表" + tDate.replace("-","");
        cell0.setCellValue(title);

        cell0.setCellStyle(s1);




        //第二行 空行,对应excel第3行
        Row row2 = sheet.createRow(2);
        Cell row2Cell0 = row2.createCell(0);
        row2Cell0.setCellValue("");

        //第三行 日期，对应excel第4行
        Row row3 = sheet.createRow(3);
        Cell row3Cell0 = row3.createCell(0);
        row3Cell0.setCellValue("日期：" + tDate);
        row3Cell0.setCellStyle(s6);


        //第四行，表格标题，对应excel第5-7行
        Row row4 = sheet.createRow(4);

        Cell row4Cell0 = row4.createCell(0);
        row4Cell0.setCellValue("科目代码");
        row4Cell0.setCellStyle(s7);


        Cell row4Cell1 = row4.createCell(1);
        row4Cell1.setCellValue("科目名称");
        row4Cell1.setCellStyle(s7);


        Cell row4Cell2 = row4.createCell(2);
        row4Cell2.setCellValue("数量");
        row4Cell2.setCellStyle(s7);


        Cell row4Cell3 = row4.createCell(3);
        row4Cell3.setCellValue("单位成本");
        row4Cell3.setCellStyle(s7);


        Cell row4Cell4 = row4.createCell(4);
        row4Cell4.setCellValue("成本");
        row4Cell4.setCellStyle(s7);

        Row row5 = sheet.createRow(5);
        Cell row5Cell4 = row5.createCell(4);
        row5Cell4.setCellValue("本币");
        row5Cell4.setCellStyle(s2);

        Row row6 = sheet.createRow(6);
        Cell row6Cell4 = row6.createCell(4);
        row6Cell4.setCellValue("十亿千百十万千百十元角分");
        row6Cell4.setCellStyle(s5);


        Cell row4Cell5 = row4.createCell(5);
        row4Cell5.setCellValue("成本占比");
        row4Cell5.setCellStyle(s7);


        Cell row4Cell6 = row4.createCell(6);
        row4Cell6.setCellValue("行情");
        row4Cell6.setCellStyle(s7);


        Cell row4Cell7 = row4.createCell(7);
        row4Cell7.setCellValue("市值");
        row4Cell7.setCellStyle(s7);

        Cell row5Cell7 = row5.createCell(7);
        row5Cell7.setCellValue("本币");
        row5Cell7.setCellStyle(s2);

        Cell row6Cell7 = row6.createCell(7);
        row6Cell7.setCellValue("十亿千百十万千百十元角分");
        row6Cell7.setCellStyle(s5);

        Cell row4Cell8 = row4.createCell(8);
        row4Cell8.setCellValue("市值占比");
        row4Cell8.setCellStyle(s7);


        Cell row4Cell9 = row4.createCell(9);
        row4Cell9.setCellValue("市值");
        row4Cell9.setCellStyle(s7);

        Cell row5Cell9 = row5.createCell(9);
        row5Cell9.setCellValue("本币");
        row5Cell9.setCellStyle(s2);

        Cell row6Cell9 = row6.createCell(9);
        row6Cell9.setCellValue("十亿千百十万千百十元角分");
        row6Cell9.setCellStyle(s5);


        Cell row4Cell10 = row4.createCell(10);
        row4Cell10.setCellValue("停牌信息");
        row4Cell10.setCellStyle(s7);


        Cell row4Cell11 = row4.createCell(11);
        row4Cell11.setCellValue("权益信息");
        row4Cell11.setCellStyle(s9);



        TcrpSubjectValueGroupBO subjectValueGroupBO = this.getSubjectValueGroupForExport(portCode, tDate);
        List<TableColumnModel> columnModels = this.getSubjectValueTableColumns();

        short rowHeight = 400;

        //第一块
        List<TcrpSubjectValue> subjectValues1 = subjectValueGroupBO.getSubjectValues1();
        int dataRowStart = 7;
        for(TcrpSubjectValue subjectValue : subjectValues1) {
            Row dataRow = sheet.createRow(dataRowStart);
            dataRow.setHeight(rowHeight);
            for(int i = 0; i < columnModels.size(); i++) {
                Cell cell = dataRow.createCell(i);

                TableColumnModel columnModel = columnModels.get(i);
                JSONObject object = (JSONObject) JSONObject.toJSON(subjectValue);

                Object v = object.get(columnModel.getProp());
                if(v == null || columnModel.getcType() == TableColumnModel.ColumnType.STRING) {
                    cell.setCellValue(StringUtils.defaultIfNull(v));
                } else if (columnModel.getcType() == TableColumnModel.ColumnType.NUMBER) {
                    cell.setCellValue(NumberUtils.convertToDouble(v));
                }


                if(i == columnModels.size() - 1) {
                    cell.setCellStyle(s8);
                } else {
                    cell.setCellStyle(columnModel.getcType() == TableColumnModel.ColumnType.NUMBER ? s3 : s2);
                }



            }
            dataRowStart++;
        }

        //第一块结束行，空行
        Row firstEndRow = sheet.createRow(dataRowStart);
        Cell firstEndRowCell0 = firstEndRow.createCell(0);
        firstEndRowCell0.setCellValue("");
        dataRowStart++;


        //第二块
        List<TcrpSubjectValue> subjectValues2 = subjectValueGroupBO.getSubjectValues2();
        for(TcrpSubjectValue subjectValue : subjectValues2) {
            Row dataRow = sheet.createRow(dataRowStart);
            dataRow.setHeight(rowHeight);
            for(int i = 0; i < columnModels.size(); i++) {
                Cell cell = dataRow.createCell(i);

                TableColumnModel columnModel = columnModels.get(i);
                JSONObject object = (JSONObject) JSONObject.toJSON(subjectValue);

                Object v = object.get(columnModel.getProp());
                if(v == null || columnModel.getcType() == TableColumnModel.ColumnType.STRING) {
                    cell.setCellValue(StringUtils.defaultIfNull(v));
                } else if (columnModel.getcType() == TableColumnModel.ColumnType.NUMBER) {
                    cell.setCellValue(NumberUtils.convertToDouble(v));
                }



                if(i == 0) {
                    //第一列字体加粗
                    cell.setCellStyle(s4);
                } else if(i == columnModels.size() - 1) {
                    //最后一列，右边框为双边框
                    cell.setCellStyle(s8);
                } else {
                    cell.setCellStyle(columnModel.getcType() == TableColumnModel.ColumnType.NUMBER ? s3 : s2);
                }


            }
            dataRowStart++;
        }

        //第二块结束行，空行
        Row secondEndRow = sheet.createRow(dataRowStart);
        Cell secondEndRowCell0 = secondEndRow.createCell(0);
        secondEndRowCell0.setCellValue("");
        dataRowStart++;

        //第三块
        List<TcrpSubjectValue> subjectValues3 = subjectValueGroupBO.getSubjectValues3();
        for(TcrpSubjectValue subjectValue : subjectValues3) {
            Row dataRow = sheet.createRow(dataRowStart);
            dataRow.setHeight(rowHeight);
            for(int i = 0; i < columnModels.size(); i++) {
                Cell cell = dataRow.createCell(i);

                TableColumnModel columnModel = columnModels.get(i);
                JSONObject object = (JSONObject) JSONObject.toJSON(subjectValue);

                Object v = object.get(columnModel.getProp());
                if(v == null || columnModel.getcType() == TableColumnModel.ColumnType.STRING) {
                    cell.setCellValue(StringUtils.defaultIfNull(v));
                } else if (columnModel.getcType() == TableColumnModel.ColumnType.NUMBER) {
                    cell.setCellValue(NumberUtils.convertToDouble(v));
                }

                if(i == 0) {
                    //第一列字体加粗
                    cell.setCellStyle(s4);
                } else if(i == columnModels.size() - 1) {
                    cell.setCellStyle(s8);
                } else {
                    cell.setCellStyle(columnModel.getcType() == TableColumnModel.ColumnType.NUMBER ? s3 : s2);
                }
            }
            dataRowStart++;
        }

        CellRangeAddress rangeAddress = new CellRangeAddress(0,1,0,11);
        sheet.addMergedRegion(rangeAddress);

        CellRangeAddress rangeAddress2 = new CellRangeAddress(2,2,0,11);
        sheet.addMergedRegion(rangeAddress2);

        CellRangeAddress rangeAddress3 = new CellRangeAddress(3,3,0,11);
        sheet.addMergedRegion(rangeAddress3);

        CellRangeAddress rangeAddress4 = new CellRangeAddress(4,6,0,0);
        sheet.addMergedRegion(rangeAddress4);
        this.setExcelBorder(rangeAddress4, sheet);

        CellRangeAddress rangeAddress5 = new CellRangeAddress(4,6,1,1);
        sheet.addMergedRegion(rangeAddress5);
        this.setExcelBorder(rangeAddress5, sheet);

        CellRangeAddress rangeAddress6 = new CellRangeAddress(4,6,2,2);
        sheet.addMergedRegion(rangeAddress6);
        this.setExcelBorder(rangeAddress6, sheet);

        CellRangeAddress rangeAddress7 = new CellRangeAddress(4,6,3,3);
        sheet.addMergedRegion(rangeAddress7);
        this.setExcelBorder(rangeAddress7, sheet);

        CellRangeAddress rangeAddress9 = new CellRangeAddress(4,6,5,5);
        sheet.addMergedRegion(rangeAddress9);
        this.setExcelBorder(rangeAddress9, sheet);

        CellRangeAddress rangeAddress10 = new CellRangeAddress(4,6,6,6);
        sheet.addMergedRegion(rangeAddress10);
        this.setExcelBorder(rangeAddress10, sheet);

        CellRangeAddress rangeAddress11 = new CellRangeAddress(4,6,8,8);
        sheet.addMergedRegion(rangeAddress11);
        this.setExcelBorder(rangeAddress11, sheet);

        CellRangeAddress rangeAddress12 = new CellRangeAddress(4,6,10,10);
        sheet.addMergedRegion(rangeAddress12);
        this.setExcelBorder(rangeAddress12, sheet);

        CellRangeAddress rangeAddress13 = new CellRangeAddress(4,6,11,11);
        sheet.addMergedRegion(rangeAddress13);
        this.setExcelBorder(rangeAddress13, sheet);

        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);

        return workbook;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 17:36
     * @description: 估值表数据列
     * @return java.util.List<com.xquant.xpacs.common.format.table.TableColumnModel>
     *
     */
    private List<TableColumnModel> getSubjectValueTableColumns() {
        List<TableColumnModel> columnModels = new ArrayList<>();
        TableColumnModel c1 = new TableColumnModel();
        c1.setProp("acctCode");
        c1.setLabel("科目代码");
        c1.setcType(TableColumnModel.ColumnType.STRING);
        columnModels.add(c1);


        TableColumnModel c2 = new TableColumnModel();
        c2.setProp("acctName");
        c2.setLabel("科目名称");
        c2.setcType(TableColumnModel.ColumnType.STRING);
        columnModels.add(c2);

        TableColumnModel c3 = new TableColumnModel();
        c3.setProp("hCount");
        c3.setLabel("数量");
        c3.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c3);

        TableColumnModel c4 = new TableColumnModel();
        c4.setProp("hPortCostUnit");
        c4.setLabel("单位成本");
        c4.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c4);

        TableColumnModel c5 = new TableColumnModel();
        c5.setProp("hPortCost");
        c5.setLabel("本币成本");
        c5.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c5);

        TableColumnModel c6 = new TableColumnModel();
        c6.setProp("costNvRatio");
        c6.setLabel("成本占比");
        c6.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c6);

        TableColumnModel c7 = new TableColumnModel();
        c7.setProp("quotePrice");
        c7.setLabel("行情");
        c7.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c7);

        TableColumnModel c8 = new TableColumnModel();
        c8.setProp("hPortEval");
        c8.setLabel("本币市值");
        c8.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c8);

        TableColumnModel c9 = new TableColumnModel();
        c9.setProp("mtmNvRatio");
        c9.setLabel("市值占比");
        c9.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c9);

        TableColumnModel c10 = new TableColumnModel();
        c10.setProp("hPortEvalAdded");
        c10.setLabel("本币估值增值");
        c10.setcType(TableColumnModel.ColumnType.NUMBER);
        columnModels.add(c10);

        TableColumnModel c11 = new TableColumnModel();
        c11.setProp("stopInfo");
        c11.setLabel("停牌信息");
        c11.setcType(TableColumnModel.ColumnType.STRING);
        columnModels.add(c11);

        TableColumnModel c12 = new TableColumnModel();
        c12.setProp("rightInfo");
        c12.setLabel("权益信息");
        c12.setcType(TableColumnModel.ColumnType.STRING);
        columnModels.add(c12);

        return columnModels;
    }

    private void setExcelBorder(CellRangeAddress rangeAddress, Sheet sheet) {
        RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);

        /*XSSFColor xssfColor = new XSSFColor(new java.awt.Color(0, 128, 0), new DefaultIndexedColorMap());
       *//* RegionUtil.setBottomBorderColor(xssfColor.getTheme(), rangeAddress, sheet);
        RegionUtil.setTopBorderColor(xssfColor.getTheme(), rangeAddress, sheet);*//*

        int color = getIntFromColor(128,0,0);
        RegionUtil.setRightBorderColor(color, rangeAddress, sheet);
        RegionUtil.setLeftBorderColor(color, rangeAddress, sheet);*/
    }

    @Override
    public void createExcel(String portCode, String tDate) {
        String fileName = this.getSubjectValueFielName(portCode, tDate);

        Workbook workbook = this.buildSubjectValueExcel(portCode, tDate);

        //获取配置的路径
        SysParam sysParam = sysParamService.getSysParam("SUBJECT_EXCEL_PATH");
        String path = sysParam.getpValue();
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir.getAbsolutePath() + "/" + fileName);

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.flush();
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    @Override
    public File getSubjectValueExcel(String portCode, String tDate) {
        String fileName = this.getSubjectValueFielName(portCode, tDate);

        SysParam sysParam = sysParamService.getSysParam("SUBJECT_EXCEL_PATH");
        String path = sysParam.getpValue();
        File dir = new File(path);

        String absoluteFilePath = dir.getAbsolutePath() + "/" + fileName;

        File file = new File(absoluteFilePath);
        if(file.exists()) {
            return file;
        } else {
            this.createExcel(portCode, tDate);

            file = new File(absoluteFilePath);
        }

        return file;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月11日 11:37
     * @description: 获取估值表Excel的文件名称
     * @param portCode  组合代码
     * @param tDate     日期
     * @return java.lang.String
     *
     */
    private String getSubjectValueFielName(String portCode, String tDate) {
        Tprt tprt = tprtService.getTprtByPortCode(portCode);
        String fileName = "估值表查询_" + tprt.getpName() + "_" + tDate + ".xlsx";
        return fileName;
    }

    @Override
    public List<File> bachCreateExcelForZip(List<SubjectValueDownLoadDTO> subjectValueDownLoadDTOS) {
        List<File> files = new ArrayList<>();
        for(SubjectValueDownLoadDTO downLoadDTO : subjectValueDownLoadDTOS) {
            File file = this.getSubjectValueExcel(downLoadDTO.getPortCode(), downLoadDTO.gettDate());

            files.add(file);
        }
        return files;
    }
}
