/**
 * ******************************************
 * 文件名称: EnumWebComponent.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月30日 09:55:24
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.enums;

import com.xquant.xpacs.analysis.wrapper.AnalysisDataAdapter;
import com.xquant.xpacs.common.format.echart.SimpleEchartDataFormat;
import com.xquant.xpacs.common.format.model.*;
import com.xquant.xpacs.common.format.table.Row2CellTableDataFormat;
import com.xquant.xpacs.common.format.table.SimpleTableDataFormat;
import com.xquant.xpacs.common.format.table.TreeTableDataFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * @ClassName: EnumWebComponent
 * @Description: 前端页面组件类型
 * @author: yt.zhou
 * @date: 2020年10月30日 09:55:24
 */
public enum EnumWebComponent {
    /**
     * @author: yt.zhou
     * @date: 2020年10月30日 09:57
     * @description: 普通表格
     *
     */
    TABLE {
        @Override
        public AnalysisDataAdapter getAnalysisDataAdapter(ServletWebRequest webRequest) {
            HttpServletRequest request = webRequest.getRequest();

            AnalysisGridPageDTO analysisBaseDTO = null;
            if(request.getMethod().equals(RequestMethod.GET.name())) {
                //GET请求
                analysisBaseDTO = new AnalysisGridPageDTO();
                ServletRequestDataBinder webDataBinder = new ServletRequestDataBinder(analysisBaseDTO);
                webDataBinder.bind(request);
            } else if (request.getMethod().equals(RequestMethod.POST.name())) {
                //POST请求，
                ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
                try {
                    EmptyBodyCheckingHttpInputMessage message = new EmptyBodyCheckingHttpInputMessage(inputMessage);
                    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                    analysisBaseDTO = (AnalysisGridPageDTO) converter.read(AnalysisGridPageDTO.class, message);
                } catch (IOException e) {
                    analysisBaseDTO = new AnalysisGridPageDTO();
                }
            }


            SimpleTableDataFormat dataFormat = new SimpleTableDataFormat();

            AnalysisDataAdapter adapter = new AnalysisDataAdapter(dataFormat, analysisBaseDTO);
            return adapter;
        }
    },
    /**行转列表格*/
    ROW_2_CELL_TABLE {
        @Override
        public AnalysisDataAdapter getAnalysisDataAdapter(ServletWebRequest webRequest) {
            HttpServletRequest request = webRequest.getRequest();

            AnalysisRow2CellDTO row2CellDTO = null;
            if(request.getMethod().equals(RequestMethod.GET.name())) {
                //GET请求
                row2CellDTO = new AnalysisRow2CellDTO();
                ServletRequestDataBinder webDataBinder = new ServletRequestDataBinder(row2CellDTO);
                webDataBinder.bind(request);
            } else if (request.getMethod().equals(RequestMethod.POST.name())) {
                //POST请求，
                ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
                try {
                    EmptyBodyCheckingHttpInputMessage message = new EmptyBodyCheckingHttpInputMessage(inputMessage);
                    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                    row2CellDTO = (AnalysisRow2CellDTO) converter.read(AnalysisRow2CellDTO.class, message);
                } catch (IOException e) {
                    row2CellDTO = new AnalysisRow2CellDTO();
                } finally {
                    try {
                        inputMessage.getBody().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Row2CellTableDataFormat dataFormat = new Row2CellTableDataFormat();

            AnalysisDataAdapter adapter = new AnalysisDataAdapter(dataFormat, row2CellDTO);

            return adapter;
        }
    },

    /**树形表格*/
    TREE_TABLE {
        @Override
        public AnalysisDataAdapter getAnalysisDataAdapter(ServletWebRequest webRequest) {
            HttpServletRequest request = webRequest.getRequest();

            AnalysisTreeDataDTO treeDataDTO = null;
            if(request.getMethod().equals(RequestMethod.GET.name())) {
                //GET请求
                treeDataDTO = new AnalysisTreeDataDTO();
                ServletRequestDataBinder webDataBinder = new ServletRequestDataBinder(treeDataDTO);
                webDataBinder.bind(request);
            } else if (request.getMethod().equals(RequestMethod.POST.name())) {
                //POST请求，
                ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
                try {
                    EmptyBodyCheckingHttpInputMessage message = new EmptyBodyCheckingHttpInputMessage(inputMessage);
                    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                    treeDataDTO = (AnalysisTreeDataDTO) converter.read(AnalysisTreeDataDTO.class, message);
                } catch (IOException e) {
                    treeDataDTO = new AnalysisTreeDataDTO();
                }
            }

            TreeTableDataFormat dataFormat = new TreeTableDataFormat();

            AnalysisDataAdapter adapter = new AnalysisDataAdapter(dataFormat, treeDataDTO);
            return adapter;
        }
    },

    //echart图表
    E_CHART {
        @Override
        public AnalysisDataAdapter getAnalysisDataAdapter(ServletWebRequest webRequest) {
            SimpleEchartDataFormat dataFormat = new SimpleEchartDataFormat();

            HttpServletRequest request = webRequest.getRequest();
            AnalysisChartDTO chartDTO;

            ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
            try {
                EmptyBodyCheckingHttpInputMessage message = new EmptyBodyCheckingHttpInputMessage(inputMessage);
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                chartDTO = (AnalysisChartDTO) converter.read(AnalysisChartDTO.class, message);
            } catch (IOException e) {
                chartDTO = new AnalysisChartDTO();
            }

            AnalysisDataAdapter adapter = new AnalysisDataAdapter(dataFormat, chartDTO);
            return adapter;
        }
    };

    /**
     * @author: yt.zhou
     * @date: 2020年10月30日 10:44
     * @description:    获取不同组件的数据转换适配器
     * @param webRequest   用于参数绑定，转换成
     * @return com.xquant.xpims.analysis.wrapper.AnalysisDataAdapter
     *
     */
    public abstract AnalysisDataAdapter getAnalysisDataAdapter(ServletWebRequest webRequest);

    private static class EmptyBodyCheckingHttpInputMessage implements HttpInputMessage {

        private final HttpHeaders headers;

        @Nullable
        private final InputStream body;

        public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
            this.headers = inputMessage.getHeaders();
            InputStream inputStream = inputMessage.getBody();
            if (inputStream.markSupported()) {
                inputStream.mark(1);
                this.body = (inputStream.read() != -1 ? inputStream : null);
                inputStream.reset();
            }
            else {
                PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
                int b = pushbackInputStream.read();
                if (b == -1) {
                    this.body = null;
                }
                else {
                    this.body = pushbackInputStream;
                    pushbackInputStream.unread(b);
                }
            }
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.headers;
        }

        @Override
        public InputStream getBody() {
            return (this.body != null ? this.body : StreamUtils.emptyInput());
        }

        public boolean hasBody() {
            return (this.body != null);
        }
    }


}
