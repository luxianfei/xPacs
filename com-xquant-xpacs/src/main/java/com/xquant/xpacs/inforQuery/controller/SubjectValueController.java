/**
 * ******************************************
 * 文件名称: SubjectValueController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月10日 15:10:40
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.inforQuery.controller;

import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.base.plan.service.api.IPlanInfoService;
import com.xquant.xpacs.common.excel.ExcelDocumentConverter;
import com.xquant.xpacs.inforQuery.entity.bo.TcrpSubjectValueGroupBO;
import com.xquant.xpacs.inforQuery.entity.dto.SubjectValueBachDownLoadDTO;
import com.xquant.xpacs.inforQuery.entity.dto.SubjectValueDownLoadDTO;
import com.xquant.xpacs.inforQuery.service.api.ITcrpSubjectValueService;
import com.xquant.xpims.tplan.entity.po.TplanInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: SubjectValueController
 * @Description: 估值相关
 * @author: yt.zhou
 * @date: 2020年11月10日 15:10:40
 */
@RestController
@RequestMapping("/subjectValue")
public class SubjectValueController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubjectValueController.class);

    @Autowired
    private ITcrpSubjectValueService subjectValueService;
    @Autowired
    private IPlanInfoService planInfoService;


    /**
     * @author: yt.zhou
     * @date: 2020年11月10日 17:57
     * @description:    估值表下载
     * @param downLoadDTO  下载信息
     * @return com.xquant.xpacs.base.http.HttpBaseResponse
     *
     */
    @PostMapping("/excel/downLoad")
    public HttpBaseResponse downLoadSubjectValueExcel(@RequestBody SubjectValueDownLoadDTO downLoadDTO, HttpServletRequest request, HttpServletResponse response) {
        File file = subjectValueService.getSubjectValueExcel(downLoadDTO.getPortCode(), downLoadDTO.gettDate());

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            String fileName;
            String encodeFileName;
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                fileName = URLEncoder.encode(file.getName(), "UTF-8");
                encodeFileName = fileName;
            } else {
                // 非IE浏览器的处理：
                fileName = new String(file.getName().getBytes("UTF-8"), "ISO-8859-1");
                encodeFileName = URLEncoder.encode(fileName, "ISO-8859-1");
            }


            response.addHeader("Content-Disposition",
                    "attachment;filename=" + fileName );
            response.addHeader("fileName", encodeFileName);
            response.setHeader("Access-Control-Expose-Headers", "FileName");

            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");

            inputStream = new FileInputStream(file);

            outputStream = response.getOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                outputStream.write(read);
            }

            outputStream.flush();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            LOGGER.error("IO异常", e);
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("IO异常", e);
            }

        }
        return null;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月11日 13:37
     * @description: 估值文件预览
     * @param portCode  组合代码
     * @param tDate     日期
     * @param request
     * @param response
     * @return org.springframework.http.ResponseEntity<com.xquant.xpacs.base.http.HttpBaseResponse>
     *
     */
    @GetMapping("/excel/preview")
    public ResponseEntity<HttpBaseResponse> previewSubjectValueExcel(String portCode, String tDate, HttpServletRequest request, HttpServletResponse response) {
        File file = subjectValueService.getSubjectValueExcel(portCode, tDate);
        StringWriter writer = null;
        try {
            Document document = ExcelDocumentConverter.process(file);

            writer = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");

            serializer.transform(new DOMSource(document),new StreamResult(writer));

            String content = writer.toString();

            Map<String, String> result = new HashMap<>();
            result.put("htmlContent", content);
            HttpBaseResponse httpBaseResponse = HttpBaseResponseUtil.getSuccessResponse(result);
            return ResponseEntity.ok(httpBaseResponse);

        } catch (Exception e) {
            LOGGER.error("估值表预览异常", e);
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                LOGGER.error("IO", e);
            }
        }
        return null;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月11日 15:31
     * @description: 查询组合+日期的估值信息
     * @param portCode  组合代码
     * @param tDate     日期
     * @return org.springframework.http.ResponseEntity<java.util.List<com.xquant.xpims.tcrp.entity.po.ext.TcrpSubjectValueExt>>
     *
     */
    @GetMapping("/getSubjectValue")
    public ResponseEntity<HttpBaseResponse> getSubjectValue(String portCode, String tDate) {
        TcrpSubjectValueGroupBO subjectValueGroupBO = subjectValueService.getSubjectValueGroupForExport(portCode, tDate);

        HttpBaseResponse response = HttpBaseResponseUtil.getSuccessResponse(subjectValueGroupBO);

        return ResponseEntity.ok(response);
    }

    /**
     * @author: yt.zhou
     * @date: 2020年11月11日 16:54
     * @description: 批量下载估值表
     * @param
     * @return org.springframework.http.ResponseEntity<com.xquant.xpacs.base.http.HttpBaseResponse>
     *
     */
    @PostMapping("/bachDownLoad")
    public ResponseEntity<HttpBaseResponse> bachDownLoad(@RequestBody SubjectValueBachDownLoadDTO downLoadDTO,
                                                         HttpServletRequest request, HttpServletResponse response) {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(response.getOutputStream());


            TplanInfo tplanInfo = planInfoService.getPlanInfo(downLoadDTO.getPlanCode());

            String fileName = "估值表查询_" + tplanInfo.getPlanName() + downLoadDTO.getBegDate() + "~" + downLoadDTO.getEndDate() + ".zip";
            String encodeFileName;
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                encodeFileName = fileName;
            } else {
                // 非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                encodeFileName = URLEncoder.encode(fileName,"ISO-8859-1");
            }


            response.addHeader("Content-Disposition",
                    "attachment;filename=" + fileName);
            response.addHeader("fileName", encodeFileName);
            response.setHeader("Access-Control-Expose-Headers", "FileName");


            List<File> files = subjectValueService.bachCreateExcelForZip(downLoadDTO.getSubjectValues());

            for(File file : files){
                String fName = file.getName();
                zipOutputStream.putNextEntry(new ZipEntry(fName));

                FileInputStream inputStream = new FileInputStream(file);
                int read;
                while ((read = inputStream.read()) != -1) {
                    zipOutputStream.write(read);
                }

                inputStream.close();
            }


            return null;

        } catch (IOException e) {
            LOGGER.error("估值表批量下载失败", e);
            return ResponseEntity.ok(HttpBaseResponseUtil.getFailResponse("估值表下载失败"));
        } finally {
            try {
                if(zipOutputStream != null) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("IO异常", e);
            }
        }
    }
}
