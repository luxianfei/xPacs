/**
 * ******************************************
 * 文件名称: AnalysisIndexCalcServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年10月19日 16:18:30
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xquant.common.exception.BusinessServiceException;
import com.xquant.xpacs.analysis.entity.bo.AnalysisIndexCalcResultBO;
import com.xquant.xpacs.analysis.entity.dto.AnalysisBaseParamDTO;
import com.xquant.xpacs.analysis.service.api.IAnalysisIndexCalcService;
import com.xquant.xpacs.common.remoting.IndexClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @ClassName: AnalysisIndexCalcServiceImpl
 * @Description: 分析功能指标计算
 * @author: yt.zhou
 * @date: 2020年10月19日 16:18:30
 */
@Component
public class AnalysisIndexCalcServiceImpl implements IAnalysisIndexCalcService {

    @Autowired
    private IndexClient indexClient;
    @Value("${engine.index.remoting.appKey:xquant.xpacs}")
    private String appKey;
    /**指标方案代码key*/
    private String indexModuleCodeKey = "moduleNo";

    /**
     * @author: yt.zhou
     * @date: 2020年10月19日 16:21
     * @description: 指标计算
     * @param moduleNo  指标方案代码
     * @param baseParamDTO 参数
     *
     */
    @Override
    public AnalysisIndexCalcResultBO calc(String moduleNo, AnalysisBaseParamDTO baseParamDTO) {

        AnalysisIndexCalcResultBO analysisIndexCalcResult = null;

        Map<String,String[]> params;
        try {
            params = this.convertBean2Map(baseParamDTO);
        } catch (IllegalAccessException e) {
            throw new BusinessServiceException("","参数转换失败" + e.getMessage());
        }

        params.put("appKey", new String[]{appKey});
        //执行指标方案计算
        String calcResult = indexClient.calcModule(moduleNo, params);

        analysisIndexCalcResult = JSONObject.parseObject(calcResult, AnalysisIndexCalcResultBO.class);

        return analysisIndexCalcResult;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年10月19日 17:20
     * @description: 将bean转换为map<String,String[]>对象
     * @param baseParamDTO 待转换的bean
     * @return java.util.Map<java.lang.String,java.lang.String[]>
     *
     */
    private Map<String, String[]> convertBean2Map(AnalysisBaseParamDTO baseParamDTO) throws IllegalAccessException {
        Class clazz = baseParamDTO.getClass();

        List<Field> fieldList = new ArrayList<>();

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));

            clazz = clazz.getSuperclass();
        }

        //转换为参数
        Map<String,String[]> params = new HashMap<>();
        for(Field field : fieldList) {
            field.setAccessible(true);

            Object object = field.get(baseParamDTO);

            if(object != null) {
                String[] array;
                if(object instanceof List) {
                    List listObject = (List) object;
                    array = new String[listObject.size()];
                    int i = 0;
                    for(Object obj : listObject) {
                        array[i++] = obj.toString();
                    }
                } else {
                    array = new String[]{object.toString()};
                }
                params.put(field.getName(),array);
            }

        }

        return params;
    }

    @Override
    public AnalysisIndexCalcResultBO defaultCalc(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();

        Map<String, String[]> calcParams = new HashMap<>(params);
        calcParams.put("appKey", new String[]{appKey});
        calcParams.put("landMid", new String[]{"true"});

        String moduleNo = request.getParameter(indexModuleCodeKey);

        String calcResult = indexClient.calcModule(moduleNo, calcParams);

        AnalysisIndexCalcResultBO analysisIndexCalcResult = JSONObject.parseObject(calcResult, AnalysisIndexCalcResultBO.class);

        return analysisIndexCalcResult;
    }
}
