package com.xquant.xpacs.assetallocation.util;

import com.xquant.common.util.excel.ExcelExportUtils;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yao.lei
 */
public class JxlsUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportUtils.class);

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = new Context();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> fun = new HashMap<String, Object>(8);
        fun.put("utils", new JxlsUtils());
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(fun);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);
        //必须要这个，否者表格函数统计会错乱

        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);

    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templateName, OutputStream os, Map<String, Object> model) throws FileNotFoundException, IOException, FileNotFoundException {
        InputStream template = getTemplate(templateName);
        exportExcel(template, os, model);
    }

    public static void downLoadExcel(String templateName, String fileName, HttpServletResponse response, Map<String, Object> dataMap) {
        ServletOutputStream outputStream = null;

        try {
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            String headerFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.addHeader("Content-Disposition", "attachment;filename=" + headerFileName);
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("fileName", encodeFileName);
            response.setHeader("Access-Control-Expose-Headers", "FileName");
            response.setContentType("application/octet-stream");
            outputStream = response.getOutputStream();
            exportExcel(templateName, outputStream, dataMap);
            outputStream.flush();
        } catch (IOException var14) {
            LOGGER.error("excel输出异常", var14);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                }
            }

        }

    }

    /**
     * 获取jxls模版文件
     *
     * @param name 文件名
     * @return 流
     */
    public static InputStream getTemplate(String name) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("template/" + name);
        return classPathResource.getInputStream();
    }

    public Object divide(Object o,double unitRate) {
        if (o instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) o;
            return bigDecimal.divide(new BigDecimal(unitRate));
        } else if (o instanceof Double) {
            double d = (double) o;
            return d / unitRate;
        } else if (o instanceof Integer) {
            int integer = (int) o;
            return integer / unitRate;
        }
        return null;
    }

    public Object multiply(Object o, double unitRate) {
        if (o instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) o;
            return bigDecimal.multiply(new BigDecimal(unitRate));
        } else if (o instanceof Double) {
            double d = (double) o;
            return d * unitRate;
        } else if (o instanceof Integer) {
            int integer = (int) o;
            return integer * unitRate;
        }
        return null;
    }
}
