/**
 *******************************************
 * 文件名称: ExcelSheetToHtmlConverter.java
 * 系统名称: xPIMS
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0.1
 * @author: deming.ye
 * 开发时间: 2018年5月30日 上午9:45:54
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.common.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.converter.AbstractExcelConverter;
import org.apache.poi.hssf.converter.ExcelToHtmlUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hwpf.converter.HtmlDocumentFacade;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.usermodel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @ClassName: ExcelSheetToHtmlConverter
* @Description: TODO(这里用一句话描述这个类的作用)
* @author: deming.ye
* @date: 2018年5月30日 上午9:45:54
*
*/
public class ExcelDocumentConverter extends AbstractExcelConverter {

   private static final POILogger logger = POILogFactory.getLogger( ExcelDocumentConverter.class );

   private String cssClassContainerCell = null;

   private String cssClassContainerDiv = null;

   private String cssClassPrefixCell = "c";

   private String cssClassPrefixDiv = "d";

   private String cssClassPrefixRow = "r";

   private String cssClassPrefixTable = "t";

   private Map<Short, String> excelStyleToClass = new LinkedHashMap<Short, String>();

   private final HtmlDocumentFacade htmlDocumentFacade;

   private boolean useDivsToSpan = false;
   // sheetName:rowNum
   private Map<String, List<Integer>> additionalMergeRowIndexs = null;
   // sheetName:cells<xIndex, yIndex, mergeSize>
   private Map<String, List<Map<String, Integer>>> additionalMergeCell = null;
   // page width: xxx px
   private short pageWidth = -1;

   public ExcelDocumentConverter(Document doc) {
       htmlDocumentFacade = new HtmlDocumentFacade(doc);
   }

   public ExcelDocumentConverter(HtmlDocumentFacade htmlDocumentFacade) {
       this.htmlDocumentFacade = htmlDocumentFacade;
   }

   /**
    * Converts Excel file (2007) into HTML file.
    *
    * @param xlsFile
    *            file to process
    * @return DOM representation of result HTML
    */
   public static Document process(File xlsxFile) throws Exception {
       final XSSFWorkbook workbook = loadXls(xlsxFile);
       ExcelDocumentConverter excelDocumentConverter = new ExcelDocumentConverter(
               XMLHelper.getDocumentBuilderFactory().newDocumentBuilder()
                       .newDocument());
       excelDocumentConverter.processWorkbook(workbook);
       return excelDocumentConverter.getDocument();
   }

   public void processWorkbook(XSSFWorkbook workbook) {
//		final SummaryInformation summaryInformation = workbook
//				.getSummaryInformation();
//		if (summaryInformation != null) {
//			processDocumentInformation(summaryInformation);
//		}

       if (isUseDivsToSpan()) {
           // prepare CSS classes for later usage
           this.cssClassContainerCell = htmlDocumentFacade
                   .getOrCreateCssClass(cssClassPrefixCell,
                           "padding:0;margin:0;align:left;vertical-align:top;");
           this.cssClassContainerDiv = htmlDocumentFacade.getOrCreateCssClass(
                   cssClassPrefixDiv, "position:relative;");
       }

       for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
           XSSFSheet sheet = workbook.getSheetAt(s);
           processSheet(sheet);
       }

       htmlDocumentFacade.updateStylesheet();
   }

   public void processWorkbook(XSSFWorkbook workbook, final String sheetName) {
       if (isUseDivsToSpan()) {
           this.cssClassContainerCell = htmlDocumentFacade
                   .getOrCreateCssClass(cssClassPrefixCell,
                           "padding:0;margin:0;align:left;vertical-align:top;");
           this.cssClassContainerDiv = htmlDocumentFacade.getOrCreateCssClass(
                   cssClassPrefixDiv, "position:relative;");
       }

       for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
           XSSFSheet sheet = workbook.getSheetAt(s);
           if (sheet.getSheetName().equals(sheetName))
               processSheet(sheet);
       }

       htmlDocumentFacade.updateStylesheet();
   }

   public void processWorkbook(XSSFWorkbook workbook, final String sheetName, final short pageWidth) {
       if (isUseDivsToSpan()) {
           this.cssClassContainerCell = htmlDocumentFacade
                   .getOrCreateCssClass(cssClassPrefixCell,
                           "padding:0;margin:0;align:left;vertical-align:top;");
           this.cssClassContainerDiv = htmlDocumentFacade.getOrCreateCssClass(
                   cssClassPrefixDiv, "position:relative;");
       }

       for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
           XSSFSheet sheet = workbook.getSheetAt(s);
           if (sheet.getSheetName().equals(sheetName))
               processSheet(sheet, pageWidth);
       }

       htmlDocumentFacade.updateStylesheet();
   }

   public void processWorkbook(XSSFWorkbook workbook, final int sheetIndex) {
       if (isUseDivsToSpan()) {
           this.cssClassContainerCell = htmlDocumentFacade
                   .getOrCreateCssClass(cssClassPrefixCell,
                           "padding:0;margin:0;align:left;vertical-align:top;");
           this.cssClassContainerDiv = htmlDocumentFacade.getOrCreateCssClass(
                   cssClassPrefixDiv, "position:relative;");
       }

       XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
       if (sheet != null)
           processSheet(sheet);

       htmlDocumentFacade.updateStylesheet();
   }

   protected void processSheet(XSSFSheet sheet, final short pageWidth) {
       /** remove header by deming.ye */
//		processSheetHeader(htmlDocumentFacade.getBody(), sheet);

       final int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
       if (physicalNumberOfRows <= 0)
           return;

       Element table = htmlDocumentFacade.createTable();
       htmlDocumentFacade.addStyleClass(table, cssClassPrefixTable,
               "border-collapse:collapse;border-spacing:0;" +
               (pageWidth == -1 ? "" : "width:" + pageWidth + "px;"));

       Element tableBody = htmlDocumentFacade.createTableBody();

       final CellRangeAddress[][] mergedRanges = buildMergedRangesMap(sheet);

       final List<Element> emptyRowElements = new ArrayList<Element>(
               physicalNumberOfRows);
       int maxSheetColumns = 1;
       for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
           XSSFRow row = sheet.getRow(r);

           if (row == null)
               continue;

           if (!isOutputHiddenRows() && row.getZeroHeight())
               continue;

           Element tableRowElement = htmlDocumentFacade.createTableRow();
           htmlDocumentFacade.addStyleClass(tableRowElement,
                   cssClassPrefixRow, "height:" + (row.getHeight() / 20f)
                           + "pt;");

           int maxRowColumnNumber = processRow(mergedRanges, row,
                   tableRowElement);

           if (maxRowColumnNumber == 0) {
               emptyRowElements.add(tableRowElement);
           } else {
               if (!emptyRowElements.isEmpty()) {
                   for (Element emptyRowElement : emptyRowElements) {
                       tableBody.appendChild(emptyRowElement);
                   }
                   emptyRowElements.clear();
               }

               tableBody.appendChild(tableRowElement);
           }
           maxSheetColumns = Math.max(maxSheetColumns, maxRowColumnNumber);
       }

       processColumnWidths(sheet, maxSheetColumns, table);

       if (isOutputColumnHeaders()) {
           processColumnHeaders(sheet, maxSheetColumns, table);
       }

       table.appendChild(tableBody);

       htmlDocumentFacade.getBody().appendChild(table);
   }

   protected void processSheet(XSSFSheet sheet) {
       this.processSheet(sheet, pageWidth);
   }

   public static XSSFWorkbook loadXls(File xlsxFile) throws IOException {
       final FileInputStream inputStream = new FileInputStream(xlsxFile);
       try {
           return new XSSFWorkbook(inputStream);
       } finally {
           IOUtils.closeQuietly(inputStream);
       }
   }

   protected String buildStyle(XSSFWorkbook workbook, XSSFCellStyle cellStyle) {
       StringBuilder style = new StringBuilder();

       style.append("white-space:pre-wrap;");
//		style.append("white-space:nowrap;");
       ExcelToHtmlUtils.appendAlign(style, cellStyle.getAlignment());

       if (cellStyle.getFillPattern() == FillPatternType.NO_FILL) {
           // no fill
       }
       else if (cellStyle.getFillPattern() == FillPatternType.SOLID_FOREGROUND) {
           final XSSFColor foregroundColor = cellStyle
                   .getFillForegroundColorColor();
           if (foregroundColor != null)
               style.append("background-color:" + getColor(foregroundColor) + ";");
       }
       else {
           final XSSFColor backgroundColor = cellStyle
                   .getFillBackgroundColorColor();
           if (backgroundColor != null)
               style.append("background-color:" + getColor(backgroundColor) + ";");
       }

       buildStyle_border(workbook, style, "top", cellStyle.getBorderTop(),
               cellStyle.getTopBorderXSSFColor());
       buildStyle_border(workbook, style, "right", cellStyle.getBorderRight(),
               cellStyle.getRightBorderXSSFColor());
       buildStyle_border(workbook, style, "bottom",
               cellStyle.getBorderBottom(), cellStyle.getBottomBorderXSSFColor());
       buildStyle_border(workbook, style, "left", cellStyle.getBorderLeft(),
               cellStyle.getLeftBorderXSSFColor());

       XSSFFont font = cellStyle.getFont();
       buildStyle_font(workbook, style, font);

       return style.toString();
   }

   private void buildStyle_border(XSSFWorkbook workbook, StringBuilder style,
                                  String type, BorderStyle bStyle, XSSFColor borderColor) {

       StringBuilder borderStyle = new StringBuilder();
       borderStyle.append(ExcelToHtmlUtils.getBorderWidth(bStyle));
       borderStyle.append(' ');
       borderStyle.append(ExcelToHtmlUtils.getBorderStyle(bStyle));

       if (borderColor != null) {
           borderStyle.append(' ');
           borderStyle.append(getColor(borderColor));
       }

       style.append("border-" + type + ":" + borderStyle + ";");
   }

   public static String getColor(XSSFColor color) {
       String result = "#000000";
       if (!color.isAuto()) {
           if (color.getCTColor() != null && color.getCTColor().xgetRgb() != null) {
               int red = 0, green = 0, blue = 0;
               byte[] rgb = color.getRGB();
               red = (rgb[0] < 0) ? (rgb[0] + 256) : rgb[0];
               green = (rgb[1] < 0) ? (rgb[1] + 256) : rgb[1];
               blue = (rgb[2] < 0) ? (rgb[2] + 256) : rgb[2];
               red = (rgb[0] < 0) ? (rgb[0] + 256) : rgb[0];
               green = (rgb[1] < 0) ? (rgb[1] + 256) : rgb[1];
               blue = (rgb[2] < 0) ? (rgb[2] + 256) : rgb[2];
               result = toHex(red, green, blue);
           } else {
           }
       } else {
           // 自动填充
       }

       if (result.equals("#ffffff"))
           return "white";

       if (result.equals("#c0c0c0"))
           return "silver";

       if (result.equals("#808080"))
           return "gray";

       if (result.equals("#000000"))
           return "black";

       return result;
   }

   public static String toHex(int r, int g, int b) {
       return "#" + toBrowserHexValue(r) + toBrowserHexValue(g)
               + toBrowserHexValue(b);
   }

   private static String toBrowserHexValue(int number) {
       StringBuilder builder = new StringBuilder(
               Integer.toHexString(number & 0xff));
       while (builder.length() < 2) {
           builder.append("0");
       }
       return builder.toString().toUpperCase();
   }

   void buildStyle_font(XSSFWorkbook workbook, StringBuilder style,
                        XSSFFont font) {

       if(font.getBold()) {
           style.append("font-weight:bold;");
       }

       final XSSFColor fontColor = font.getXSSFColor();
       if (fontColor != null)
           style.append("color: " + getColor(fontColor)
                   + "; ");

       if (font.getFontHeightInPoints() != 0)
           style.append("font-size:" + font.getFontHeightInPoints() + "pt;");

       if (font.getItalic()) {
           style.append("font-style:italic;");
       }
   }

   public String getCssClassPrefixCell() {
       return cssClassPrefixCell;
   }

   public String getCssClassPrefixDiv() {
       return cssClassPrefixDiv;
   }

   public String getCssClassPrefixRow() {
       return cssClassPrefixRow;
   }

   public String getCssClassPrefixTable() {
       return cssClassPrefixTable;
   }

   @Override
   public Document getDocument() {
       return htmlDocumentFacade.getDocument();
   }

   protected String getStyleClassName(XSSFWorkbook workbook,
           XSSFCellStyle cellStyle) {
       final Short cellStyleKey = Short.valueOf(cellStyle.getIndex());
       String knownClass = excelStyleToClass.get(cellStyleKey);
       if (knownClass != null)
           return knownClass;

       String cssStyle = buildStyle(workbook, cellStyle);
       String cssClass = htmlDocumentFacade.getOrCreateCssClass(
               cssClassPrefixCell, cssStyle);
       excelStyleToClass.put(cellStyleKey, cssClass);
       return cssClass;
   }

   public boolean isUseDivsToSpan() {
       return useDivsToSpan;
   }

   protected boolean processCell(XSSFCell cell, Element tableCellElement,
                                 int normalWidthPx, int maxSpannedWidthPx, float normalHeightPt) {
       final XSSFCellStyle cellStyle = cell.getCellStyle();

       String value;
       switch (cell.getCellType()) {
           case STRING:
               // XXX: enrich
               value = cell.getRichStringCellValue().getString();
               break;
           case FORMULA:
           switch (cell.getCachedFormulaResultType()) {
               case STRING:
                   XSSFRichTextString str = cell.getRichStringCellValue();
                   if (str != null && str.length() > 0) {
                       value = (str.toString());
                   } else {
                       value = StringUtils.EMPTY;
                   }
                   break;
               case NUMERIC:
                   XSSFCellStyle style = cellStyle;
                   if (style == null) {
                       value = String.valueOf(cell.getNumericCellValue());
                   } else {
                       final short format = style.getDataFormat();
                       if(format == 31){
                           Date date = cell.getDateCellValue();
                           DateFormat formater = new SimpleDateFormat("yyyy年MM月dd");
                           value = formater.format(date);
                       } else {
                           value = (_formatter.formatRawCellContents(
                                   cell.getNumericCellValue(), style.getDataFormat(),
                                   style.getDataFormatString()));
                       }
                   }
                   break;
               case BOOLEAN:
                   value = String.valueOf(cell.getBooleanCellValue());
                   break;
               case ERROR:
                   value = ErrorEval.getText(cell.getErrorCellValue());
                   break;
               default:
                   logger.log(
                           POILogger.WARN,
                           "Unexpected cell cachedFormulaResultType ("
                                   + cell.getCachedFormulaResultType() + ")");
                   value = StringUtils.EMPTY;
                   break;
               }
               break;
           case BLANK:
               value = StringUtils.EMPTY;
               break;
           case NUMERIC:
               final short format = cell.getCellStyle().getDataFormat();
               if(format == 31){
                   Date date = cell.getDateCellValue();
                   DateFormat formater = new SimpleDateFormat("yyyy年MM月dd");
                   value = formater.format(date);
               } else {
                   value = _formatter.formatCellValue(cell);
               }
               break;
           case BOOLEAN:
               value = String.valueOf(cell.getBooleanCellValue());
               break;
           case ERROR:
               value = ErrorEval.getText(cell.getErrorCellValue());
               break;
           default:
               logger.log(POILogger.WARN,
                       "Unexpected cell type (" + cell.getCellType() + ")");
               return true;
       }

       final boolean noText = StringUtils.isEmpty(value);
       final boolean wrapInDivs = !noText && isUseDivsToSpan()
               && !cellStyle.getWrapText();

       final short cellStyleIndex = cellStyle.getIndex();
       if (cellStyleIndex != 0) {
           XSSFWorkbook workbook = cell.getRow().getSheet().getWorkbook();
           String mainCssClass = getStyleClassName(workbook, cellStyle);

           if (wrapInDivs) {
               tableCellElement.setAttribute("class", mainCssClass + " "
                       + cssClassContainerCell);
           } else {
               tableCellElement.setAttribute("class", mainCssClass);
           }

           if (noText) {
               /*
                * if cell style is defined (like borders, etc.) but cell text
                * is empty, add "&nbsp;" to output, so browser won't collapse
                * and ignore cell
                */
               value = "\u00A0";
           }
       }

       if (isOutputLeadingSpacesAsNonBreaking() && value.startsWith(" ")) {
           StringBuilder builder = new StringBuilder();
           for (int c = 0; c < value.length(); c++) {
               if (value.charAt(c) != ' ')
                   break;
               builder.append('\u00a0');
           }

           if (value.length() != builder.length())
               builder.append(value.substring(builder.length()));

           value = builder.toString();
       }

       Text text = htmlDocumentFacade.createText(value);

       if (wrapInDivs) {
           Element outerDiv = htmlDocumentFacade.createBlock();
           outerDiv.setAttribute("class", this.cssClassContainerDiv);

           Element innerDiv = htmlDocumentFacade.createBlock();
           StringBuilder innerDivStyle = new StringBuilder();
           innerDivStyle.append("position:absolute;min-width:");
           innerDivStyle.append(normalWidthPx);
           innerDivStyle.append("px;");
           if (maxSpannedWidthPx != Integer.MAX_VALUE) {
               innerDivStyle.append("max-width:");
               innerDivStyle.append(maxSpannedWidthPx);
               innerDivStyle.append("px;");
           }
           innerDivStyle.append("overflow:hidden;max-height:");
           innerDivStyle.append(normalHeightPt);
           innerDivStyle.append("pt;white-space:nowrap;");
           ExcelToHtmlUtils.appendAlign(innerDivStyle,
                   cellStyle.getAlignment());
           htmlDocumentFacade.addStyleClass(outerDiv, cssClassPrefixDiv,
                   innerDivStyle.toString());

           innerDiv.appendChild(text);
           outerDiv.appendChild(innerDiv);
           tableCellElement.appendChild(outerDiv);
       } else {
           tableCellElement.appendChild(text);
       }

       return StringUtils.isEmpty(value) && cellStyleIndex == 0;
   }

   protected void processColumnHeaders(XSSFSheet sheet, int maxSheetColumns,
                                       Element table) {
       Element tableHeader = htmlDocumentFacade.createTableHeader();
       table.appendChild(tableHeader);

       Element tr = htmlDocumentFacade.createTableRow();

       if (isOutputRowNumbers()) {
           // empty row at left-top corner
           tr.appendChild(htmlDocumentFacade.createTableHeaderCell());
       }

       for (int c = 0; c < maxSheetColumns; c++) {
           if (!isOutputHiddenColumns() && sheet.isColumnHidden(c))
               continue;

           Element th = htmlDocumentFacade.createTableHeaderCell();
           String text = getColumnName(c);
           th.appendChild(htmlDocumentFacade.createText(text));
           tr.appendChild(th);
       }
       tableHeader.appendChild(tr);
   }

   /**
    * Creates COLGROUP element with width specified for all columns. (Except
    * first if <tt>{@link #isOutputRowNumbers()}==true</tt>)
    */
   protected void processColumnWidths(XSSFSheet sheet, int maxSheetColumns,
                                      Element table) {
       // draw COLS after we know max column number
       Element columnGroup = htmlDocumentFacade.createTableColumnGroup();
       if (isOutputRowNumbers()) {
           columnGroup.appendChild(htmlDocumentFacade.createTableColumn());
       }
       for (int c = 0; c < maxSheetColumns; c++) {
           if (!isOutputHiddenColumns() && sheet.isColumnHidden(c))
               continue;

           Element col = htmlDocumentFacade.createTableColumn();
           col.setAttribute("width", String.valueOf(getColumnWidth(sheet, c)));
           columnGroup.appendChild(col);
       }
       table.appendChild(columnGroup);
   }

   protected void processDocumentInformation(
           SummaryInformation summaryInformation) {
       if (StringUtils.isNotEmpty(summaryInformation.getTitle()))
           htmlDocumentFacade.setTitle(summaryInformation.getTitle());

       if (StringUtils.isNotEmpty(summaryInformation.getAuthor()))
           htmlDocumentFacade.addAuthor(summaryInformation.getAuthor());

       if (StringUtils.isNotEmpty(summaryInformation.getKeywords()))
           htmlDocumentFacade.addKeywords(summaryInformation.getKeywords());

       if (StringUtils.isNotEmpty(summaryInformation.getComments()))
           htmlDocumentFacade.addDescription(summaryInformation.getComments());
   }

   /**
    * @return maximum 1-base index of column that were rendered, zero if none
    */
   protected int processRow(CellRangeAddress[][] mergedRanges, XSSFRow row,
                            Element tableRowElement) {
       final XSSFSheet sheet = row.getSheet();
       final short maxColIx = row.getLastCellNum();
       if (maxColIx <= 0)
           return 0;

       final List<Element> emptyCells = new ArrayList<Element>(maxColIx);

       if (isOutputRowNumbers()) {
           Element tableRowNumberCellElement = htmlDocumentFacade
                   .createTableHeaderCell();
           processRowNumber(row, tableRowNumberCellElement);
           emptyCells.add(tableRowNumberCellElement);
       }

       int maxRenderedColumn = 0;
       for (int colIx = 0; colIx < maxColIx; colIx++) {
           if (!isOutputHiddenColumns() && sheet.isColumnHidden(colIx))
               continue;

           CellRangeAddress range = ExcelToHtmlUtils.getMergedRange(
                   mergedRanges, row.getRowNum(), colIx);

           if (range != null
                   && (range.getFirstColumn() != colIx || range.getFirstRow() != row
                           .getRowNum()))
               continue;

           XSSFCell cell = row.getCell(colIx);

           int divWidthPx = 0;
           if (isUseDivsToSpan()) {
               divWidthPx = getColumnWidth(sheet, colIx);

               boolean hasBreaks = false;
               for (int nextColumnIndex = colIx + 1; nextColumnIndex < maxColIx; nextColumnIndex++) {
                   if (!isOutputHiddenColumns()
                           && sheet.isColumnHidden(nextColumnIndex))
                       continue;

                   if (row.getCell(nextColumnIndex) != null
                           && !isTextEmpty(row.getCell(nextColumnIndex))) {
                       hasBreaks = true;
                       break;
                   }

                   divWidthPx += getColumnWidth(sheet, nextColumnIndex);
               }

               if (!hasBreaks)
                   divWidthPx = Integer.MAX_VALUE;
           }

           Element tableCellElement = htmlDocumentFacade.createTableCell();

           if (range != null) {
               if (range.getFirstColumn() != range.getLastColumn())
                   tableCellElement.setAttribute(
                           "colspan",
                           String.valueOf(range.getLastColumn()
                                   - range.getFirstColumn() + 1));
               if (range.getFirstRow() != range.getLastRow())
                   tableCellElement.setAttribute(
                           "rowspan",
                           String.valueOf(range.getLastRow()
                                   - range.getFirstRow() + 1));
           }

           boolean emptyCell;
           if (cell != null) {
               emptyCell = processCell(cell, tableCellElement,
                       getColumnWidth(sheet, colIx), divWidthPx,
                       row.getHeight() / 20f);
           } else {
               emptyCell = true;
           }

           if (emptyCell) {
               emptyCells.add(tableCellElement);
           } else {
               for (Element emptyCellElement : emptyCells) {
                   tableRowElement.appendChild(emptyCellElement);
               }
               emptyCells.clear();

               tableRowElement.appendChild(tableCellElement);
               maxRenderedColumn = colIx;
           }
       }

       return maxRenderedColumn + 1;
   }

   protected boolean isTextEmpty(XSSFCell cell) {
       final String value;
       switch (cell.getCellType()) {
           case STRING:
               // XXX: enrich
               value = cell.getRichStringCellValue().getString();
               break;
           case FORMULA:
               switch (cell.getCachedFormulaResultType()) {
                   case STRING:
                       XSSFRichTextString str = cell.getRichStringCellValue();
                       if (str == null || str.length() <= 0)
                           return false;

                       value = str.toString();
                       break;
                   case NUMERIC:
                       XSSFCellStyle style = cell.getCellStyle();
                       if (style == null) {
                           return false;
                       }

                       value = (_formatter.formatRawCellContents(
                               cell.getNumericCellValue(), style.getDataFormat(),
                               style.getDataFormatString()));
                       break;
                   case BOOLEAN:
                       value = String.valueOf(cell.getBooleanCellValue());
                       break;
                   case ERROR:
                       value = ErrorEval.getText(cell.getErrorCellValue());
                       break;
                   default:
                       value = StringUtils.EMPTY;
                       break;
               }
               break;
           case BLANK:
               value = StringUtils.EMPTY;
               break;
           case NUMERIC:
               value = _formatter.formatCellValue(cell);
               break;
           case BOOLEAN:
               value = String.valueOf(cell.getBooleanCellValue());
               break;
           case ERROR:
               value = ErrorEval.getText(cell.getErrorCellValue());
               break;
           default:
               return true;
           }

       return StringUtils.isEmpty(value);
   }

   protected static int getColumnWidth(XSSFSheet sheet, int columnIndex) {
       return ExcelToHtmlUtils.getColumnWidthInPx(sheet
               .getColumnWidth(columnIndex));
   }

   protected void processRowNumber(XSSFRow row,
           Element tableRowNumberCellElement) {
       tableRowNumberCellElement.setAttribute("class", "rownumber");
       Text text = htmlDocumentFacade.createText(getRowName(row));
       tableRowNumberCellElement.appendChild(text);
   }

   protected String getRowName(XSSFRow row) {
       return String.valueOf(row.getRowNum() + 1);
   }

   public CellRangeAddress[][] buildMergedRangesMap(XSSFSheet sheet) {
       CellRangeAddress[][] mergedRanges = new CellRangeAddress[1][];
       for (int m = 0; m < sheet.getNumMergedRegions(); m++) {
           final CellRangeAddress cellRangeAddress = sheet.getMergedRegion(m);

           final int requiredHeight = cellRangeAddress.getLastRow() + 1;
           if (mergedRanges.length < requiredHeight) {
               CellRangeAddress[][] newArray = new CellRangeAddress[requiredHeight][];
               System.arraycopy(mergedRanges, 0, newArray, 0,
                       mergedRanges.length);
               mergedRanges = newArray;
           }

           for (int r = cellRangeAddress.getFirstRow(); r <= cellRangeAddress
                   .getLastRow(); r++) {
               final int requiredWidth = cellRangeAddress.getLastColumn() + 1;

               CellRangeAddress[] rowMerged = mergedRanges[r];
               if (rowMerged == null) {
                   rowMerged = new CellRangeAddress[requiredWidth];
                   mergedRanges[r] = rowMerged;
               } else {
                   final int rowMergedLength = rowMerged.length;
                   if (rowMergedLength < requiredWidth) {
                       final CellRangeAddress[] newRow = new CellRangeAddress[requiredWidth];
                       System.arraycopy(rowMerged, 0, newRow, 0,
                               rowMergedLength);

                       mergedRanges[r] = newRow;
                       rowMerged = newRow;
                   }
               }

               Arrays.fill(rowMerged, cellRangeAddress.getFirstColumn(),
                       cellRangeAddress.getLastColumn() + 1, cellRangeAddress);
           }
       }
       // merged title ranges
       mergedRanges = mergedTitleRanges(sheet, mergedRanges);
       mergedRanges = mergedCellRanges(sheet, mergedRanges);
       return mergedRanges;
   }

   private CellRangeAddress[][] mergedTitleRanges(XSSFSheet sheet, CellRangeAddress[][] mergedRanges) {
       CellRangeAddress[] mergedRowRanges = new CellRangeAddress[1];
       if (additionalMergeRowIndexs != null) {
           String sheetName = sheet.getSheetName();
           if (additionalMergeRowIndexs.containsKey(sheetName)) {
               List<Integer> rowIndexs = additionalMergeRowIndexs.get(sheetName);
               for (int rowIndex : rowIndexs) {
                   Row row = sheet.getRow(rowIndex);
                   final short maxColIx = row.getLastCellNum();
                   mergedRowRanges = new CellRangeAddress[maxColIx];
                   int firstCol = getFirstCol(row);
                   for (int i = 0; i < maxColIx; i ++) {
                       mergedRowRanges[i] = new CellRangeAddress(rowIndex, rowIndex, firstCol, row.getLastCellNum());
                   }
                   mergedRanges[rowIndex] = mergedRowRanges;
               }
           }
       }
       return mergedRanges;
   }

   private CellRangeAddress[][] mergedCellRanges(XSSFSheet sheet, CellRangeAddress[][] mergedRanges) {
       if (additionalMergeCell != null) {
           String sheetName = sheet.getSheetName();
           if (additionalMergeCell.containsKey(sheetName)
                   && additionalMergeCell.get(sheetName) != null) {
               List<Map<String, Integer>> mergedCells = additionalMergeCell.get(sheetName);
               for (Map<String, Integer> mergedCell : mergedCells) {
                   int xIndex = mergedCell.get("xIndex");
                   int yIndex = mergedCell.get("yIndex");
                   int mergedSize = mergedCell.get("mergeSize");
                   final int maxColIx = yIndex + mergedSize - 1;
                   int firstCol = yIndex;
                   if (mergedRanges.length <= xIndex) {
                       CellRangeAddress[][] newArray = new CellRangeAddress[xIndex + 1][];
                       System.arraycopy(mergedRanges, 0, newArray, 0,
                               mergedRanges.length);
                       mergedRanges = newArray;
                   }
                   final int requiredWidth = yIndex + mergedSize + 1;

                   CellRangeAddress[] rowMerged = mergedRanges[xIndex];
                   if (rowMerged == null) {
                       rowMerged = new CellRangeAddress[requiredWidth];
                       mergedRanges[xIndex] = rowMerged;
                   } else {
                       final int rowMergedLength = rowMerged.length;
                       if (rowMergedLength < requiredWidth) {
                           final CellRangeAddress[] newRow = new CellRangeAddress[requiredWidth];
                           System.arraycopy(rowMerged, 0, newRow, 0,
                                   rowMergedLength);

                           mergedRanges[xIndex] = newRow;
                           rowMerged = newRow;
                       }
                   }
                   for (int i = 0; i < mergedSize; i ++) {
                       mergedRanges[xIndex][yIndex + i] = new CellRangeAddress(xIndex, xIndex, firstCol, maxColIx);
                   }
               }
           }
       }
       return mergedRanges;
   }

   private int getFirstCol(Row row) {
       int ret = 0;
       for (int col = 0; col < row.getLastCellNum(); col ++) {
           if (StringUtils.isNotEmpty(row.getCell(col).getStringCellValue())) {
               ret = col;
               break;
           }
       }
       return ret;
   }

   protected void processSheetHeader(Element htmlBody, XSSFSheet sheet) {
       Element h2 = htmlDocumentFacade.createHeader2();
       h2.appendChild(htmlDocumentFacade.createText(sheet.getSheetName()));
       htmlBody.appendChild(h2);
   }

   public void setCssClassPrefixCell(String cssClassPrefixCell) {
       this.cssClassPrefixCell = cssClassPrefixCell;
   }

   public void setCssClassPrefixDiv(String cssClassPrefixDiv) {
       this.cssClassPrefixDiv = cssClassPrefixDiv;
   }

   public void setCssClassPrefixRow(String cssClassPrefixRow) {
       this.cssClassPrefixRow = cssClassPrefixRow;
   }

   public void setCssClassPrefixTable(String cssClassPrefixTable) {
       this.cssClassPrefixTable = cssClassPrefixTable;
   }

   public Map<String, List<Integer>> getAdditionalMergeRowIndexs() {
       return additionalMergeRowIndexs;
   }

   public void setAdditionalMergeRowIndexs(
           Map<String, List<Integer>> additionalMergeRowIndexs) {
       this.additionalMergeRowIndexs = additionalMergeRowIndexs;
   }

   public void setAdditionalMergeCell(
           Map<String, List<Map<String, Integer>>> additionalMergeCell) {
       this.additionalMergeCell = additionalMergeCell;
   }

   public short getPageWidth() {
       return pageWidth;
   }

   public void setPageWidth(short pageWidth) {
       this.pageWidth = pageWidth;
   }

   /**
    * Allows converter to wrap content into two additional DIVs with tricky
    * styles, so it will wrap across empty cells (like in Excel).
    * <p>
    * <b>Warning:</b> after enabling this mode do not serialize result HTML
    * with INDENT=YES option, because line breaks will make additional
    * (unwanted) changes
    */
   public void setUseDivsToSpan(boolean useDivsToSpan) {
       this.useDivsToSpan = useDivsToSpan;
   }

   /**
    * @Title: main
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param args
    */
   public static void main(String[] args) {
       // TODO Auto-generated method stub

   }

    public String getCssClassContainerCell() {
        return cssClassContainerCell;
    }

    public void setCssClassContainerCell(String cssClassContainerCell) {
        this.cssClassContainerCell = cssClassContainerCell;
    }

    public String getCssClassContainerDiv() {
        return cssClassContainerDiv;
    }

    public void setCssClassContainerDiv(String cssClassContainerDiv) {
        this.cssClassContainerDiv = cssClassContainerDiv;
    }

    public Map<Short, String> getExcelStyleToClass() {
        return excelStyleToClass;
    }

    public void setExcelStyleToClass(Map<Short, String> excelStyleToClass) {
        this.excelStyleToClass = excelStyleToClass;
    }

    public HtmlDocumentFacade getHtmlDocumentFacade() {
        return htmlDocumentFacade;
    }

    public Map<String, List<Map<String, Integer>>> getAdditionalMergeCell() {
        return additionalMergeCell;
    }

    @Override
    public boolean isOutputColumnHeaders() {
        return false;
    }

    @Override
    public boolean isOutputRowNumbers() {
        return false;
    }
}
