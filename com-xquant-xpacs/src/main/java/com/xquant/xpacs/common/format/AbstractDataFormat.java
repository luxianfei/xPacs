/**
 * ******************************************
 * 文件名称: AbstractDataFormat.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V5.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2019年01月31日 16:16:48
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xquant.common.util.NumberUtils;
import com.xquant.common.util.StringUtils;
import com.xquant.xpacs.common.format.model.AnalysisBaseDTO;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: AbstractDataFormat
 * @Description: 前端数据格式化抽象类
 * @author: yt.zhou
 * @date: 2019年01月31日 16:16:48
 *
 */
public abstract class AbstractDataFormat implements IAnalysisDataFormat {

    /**
     * @author: yt.zhou
     * @date: 2019年03月26日 17:10
     * @description:    数据处理，
     * @param dataList  数据集
     * @param filterModel      筛选条件
     * @return java.lang.Object
     *
     */
    public BaseResultModel doFormat(List<Map<String,Object>> dataList,DataFilterModel filterModel,AnalysisBaseDTO baseDTO) {

        //1、做字段映射，有时候组合，基准同时计算时，组合基准返回数据的字段不一致，需要将不一致的字段转成相同的字段，这样在表格中方便显示
        if(baseDTO.getKeyMappings() != null && !baseDTO.getKeyMappings().isEmpty()) {
            dataList = this.doDataKeyMapping(dataList,baseDTO.getKeyMappings());
        }

        //合并数据
        if(baseDTO.getDataMergeModel() != null) {
            dataList = this.doMergeData(dataList,baseDTO.getDataMergeModel());
        }

        //2、过滤数据，
        dataList = filterList(dataList,filterModel);

        String sortField = baseDTO.getSortField();
        if(!StringUtils.isBlank(sortField)){
            String sortType = baseDTO.getSortType();
            //3、数据排序
            dataList = sort(dataList,sortField,sortType);
        }

        //4、取知道数据量的数据
        Long dataSize = baseDTO.getDataSize();
        if(dataSize != null) {
            int size = dataSize.longValue() > dataList.size() ? dataList.size() : dataSize.intValue();
            dataList = dataList.subList(0,size);
        }

        //5、根据配置处理数据
        return this.doComponentFormat(dataList,baseDTO);
    }

    @Override
    public BaseResultModel doFormat(List<Map<String,Object>> dataList, AnalysisBaseDTO baseDTO) {
        DataFilterModel filterModel = baseDTO.getDataFilter() == null ?
                this.convertDataFilterModel(baseDTO.getFilter(),null) : baseDTO.getDataFilter();
        return this.doFormat(dataList,filterModel,baseDTO);
    }

    /**
     * @author: yt.zhou
     * @date: 2020年06月19日 13:43
     * @description:    数据过滤
     * @param list      数据集
     * @param filterModel 过滤模型
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    private List<Map<String,Object>> filterList(List<Map<String,Object>> list,DataFilterModel filterModel){
        //是否需要筛选
        if(DataFilterTypeEnum.ALL.getCode().equals(filterModel.getfType()) || filterModel.getConditionList() == null
                || filterModel.getConditionList().isEmpty()) {
            return list;
        }

        List<Map<String,Object>> filterResult = new ArrayList<>();
        List<List<ConditionModel>> conditionList = filterModel.getConditionList();
        for(Map<String,Object> data : list) {
            //循环筛选条件，这是数据层面的筛选条件的集合，即符合集合中的一个数据的条件即可，进行数据与筛选条件的数据对比
            for(List<ConditionModel> groupCondition : conditionList){
                //是否符合条件，这里是单个数据的条件集合，需要满足所有条件的数据才符合，当data对应前端传入的key的值相同，则表示符合
                boolean conform = true;
                for(ConditionModel singleCondition : groupCondition) {
                    String sourceKey = singleCondition.getSource();//源key
                    if(sourceKey == null) {
                        continue;
                    }
                    String dest = singleCondition.getDest();//目标key
                    //目标是key,或者value,
                    ConditionModel.DestType destType = singleCondition.getDestType();
                    //条件 = != > < %
                    ConditionModel.Operator operator = singleCondition.getOperator();

                    //源值，对比值
                    String sourceVal = StringUtils.defaultIfNull(data.get(sourceKey));

                    //目标值，被对比值
                    String targetVal;
                    if(destType == ConditionModel.DestType.VALUE) {
                        //目标值类型是value,表示固定值
                        targetVal = dest;
                    } else {
                        //目标值是key，表示通过该key取值
                        targetVal = StringUtils.defaultIfNull(data.get(dest));
                    }

                    if(!operator.isOkCondition(sourceVal,targetVal)) {
                        conform = false;
                        break;
                    }

                }

                if(conform) {
                    //条件组，满足一个条件即可
                    filterResult.add(data);
                    break;
                }
            }
        }
        return filterResult;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年07月03日 18:35
     * @description: 数据集排序
     * @param dataList  数据集
     * @param sortField 排序字段
     * @param sortType  排序类型，asc,desc
     *
     */
    public List<Map<String,Object>> sort(List<Map<String,Object>> dataList, final String sortField, final String sortType){
        return dataList.stream().sorted((o1, o2) -> {
            String s1 = StringUtils.defaultIfNull(o1.get(sortField));
            String s2 = StringUtils.defaultIfNull(o2.get(sortField));

            Double l1 = NumberUtils.convertToDouble(s1,null);
            Double l2 = NumberUtils.convertToDouble(s2,null);

            int c;
            if(l1 == null || l2 == null) {
                c = s1.compareTo(s2);
            } else {
                c = l1.compareTo(l2);
            }

            //是否倒序
            boolean desc = "desc".equalsIgnoreCase(sortType);
            if(c == 0) {
                return 0;
            }
            return c > 0 ? ( desc ? -1 : 1 ): (desc ? 1 : -1);
        }).collect(Collectors.toList());
    }


    /**
     * @author: yt.zhou
     * @date: 2020年06月19日 13:49
     * @description:    组件自身的数据处理
     * @param list      数据集
     * @param analysisBaseDTO   模型
     * @return java.lang.Object
     *
     */
    protected abstract BaseResultModel doComponentFormat(List<Map<String,Object>> list,AnalysisBaseDTO analysisBaseDTO);

    /**
     * @author: yt.zhou
     * @date: 2020年06月29日 16:26
     * @description: 过滤条件转换
     * @param filter
     * @param defaultFilter
     * @return com.xquant.index.base.format.DataFilterModel
     *
     */
    public DataFilterModel convertDataFilterModel(String filter,String defaultFilter) {
        DataFilterModel filterModel = new DataFilterModel();
        filter = StringUtils.isBlank(filter) ? defaultFilter : filter;

        if(StringUtils.isBlank(filter)) {
            filterModel.setfType(DataFilterTypeEnum.ALL.getCode());
            return filterModel;
        }

        Map<String,Object> conditions = JSONObject.parseObject(filter,Map.class);
        filterModel.setfType(DataFilterTypeEnum.FILTER.getCode());

        //条件格式：a=b,c=d&&e=f
        String conditionStr = "";
        Object condition = conditions.get("condition");
        if(condition instanceof JSONArray) {
            //修改之前，原来的条件，是以JSONArray格式的，做兼容
            JSONArray jsonArray = (JSONArray) condition;
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> it = jsonObject.keySet().iterator();
                String groupCondition = "";
                while (it.hasNext()) {
                    String key = it.next();
                    String value = jsonObject.get(key).toString();
                    if(!"".equals(groupCondition)) {
                        groupCondition += "&&";
                    }
                    groupCondition += key + "=" + value;
                }
                if(!"".equals(conditionStr)) {
                    conditionStr += ",";
                }
                conditionStr += groupCondition;
            }
        } else {
            conditionStr = StringUtils.defaultIfNull(conditions.get("condition"));
        }
        if(!StringUtils.isBlank(conditionStr)) {
            try {
                List<List<ConditionModel>> conditionList = ConditionJSONConvert.conditionStr2ConditionList(conditionStr);
                filterModel.setConditionList(conditionList);
            } catch (Exception e) {

            }

        }

        return filterModel;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年07月08日 15:15
     * @description:    字段映射处理
     * @param list
     * @param keyMappings
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    private List<Map<String,Object>> doDataKeyMapping(List<Map<String,Object>> list,List<DataKeyMapping> keyMappings) {
        for(Map<String,Object> data : list) {
            for(DataKeyMapping keyMapping : keyMappings) {
                String name = keyMapping.getName();
                String mapping = keyMapping.getMapping();
                if(data.containsKey(name)) {
                    data.put(mapping,data.get(name));
                    data.remove(name);
                }
            }
        }
        return list;
    }

    /**
     * @author: yt.zhou
     * @date: 2020年07月10日 09:40
     * @description: 合并数据,将多条数据，根据指定维度合并成一条数据。
     * @param list
     * @param mergeModel
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *
     */
    private List<Map<String,Object>> doMergeData(List<Map<String,Object>> list,
    		DataMergeModel mergeModel) {
        //合并结果
        List<Map<String,Object>> mergeResult = new ArrayList<>();
        //维度与数据的关系
        Map<String,Map<String,Object>> dimensonDataMap = new HashMap<>();

        //不存在维度的数据
        List<Map<String,Object>> unExistsDimensonData = new ArrayList<>();

        List<String> dataFields = mergeModel.getDataField();
        List<String> dimension = mergeModel.getDimension();
        //结果字段包含维度字段
        dataFields.addAll(dimension);

        //1、同纬度数据合并
        loop1:for(Map<String,Object> data : list) {
            StringBuilder sbuilder = new StringBuilder();
            for(String d : dimension) {
                if(!data.containsKey(d)) {
                    unExistsDimensonData.add(data);
                    continue loop1;
                }
                //拼接每条数据的维度，key:value-key:value
                Object val = data.get(d);
                if(sbuilder.length() > 0) {
                    sbuilder.append("-");
                }
                sbuilder.append(d).append(":").append(val);
            }
            Map<String,Object> dimensonData = dimensonDataMap.get(sbuilder.toString());
            if(dimensonData == null) {
                dimensonData = new HashMap<>();
                mergeResult.add(dimensonData);
                dimensonDataMap.put(sbuilder.toString(),dimensonData);
            }
            //获取指定需要的字段
            for(String field : dataFields) {
                if(data.containsKey(field)) {
                    //行转列
                    if (CollectionUtils.isNotEmpty(mergeModel.getRowToCloumn())) {
                        String[] strings = mergeModel.getRowToCloumn().toArray(new String[2]);
                        if (field.equals(strings[1])) {
                            dimensonData.put(String.valueOf(data.get(strings[1])), data.get(strings[0]));
                        } else {
                            dimensonData.put(field, data.get(field));
                        }
                    } else {
                        dimensonData.put(field, data.get(field));
                    }

                }
            }
        }

        //2、不同维度数据合并，比如组合与基准合并
        List<DataMergeModel.InexistenceDimenson> inexistenceDimensons = mergeModel.getInexistenceDimensons();
        for(DataMergeModel.InexistenceDimenson inexistenceDimenson : inexistenceDimensons) {
            List<DataMergeModel.InexistenceModel> sourceList = inexistenceDimenson.getSource();
            List<DataMergeModel.InexistenceModel> targetList = inexistenceDimenson.getTarget();

            for(Map<String,Object> data : mergeResult) {

                for(Map<String,Object> targetData : unExistsDimensonData) {

                    boolean sameDimensonData = true;

                    for(int i=0;i<sourceList.size();i++) {
                        DataMergeModel.InexistenceModel source = sourceList.get(i);
                        DataMergeModel.InexistenceModel target = targetList.get(i);
                        String sourceKey = source.getKey();
                        String sourceValue = source.getValue();
                        String targetKey = target.getKey();
                        String targetValue = target.getValue();


                        Object sValue = data.get(sourceKey);
                        Object tValue = targetData.get(targetKey);

                        /*
                         * 合并逻辑分为两种情况，
                         *      1、当合并条件中没有指定值，即sourceValue==null时，则判断源数据与目标数据是否一致，表示满足条件
                         *      2、当合并条件中有指定值时，即sourceValue 不为空时，则sourValue与源数据中的值相同，
                         *      并且targetValue与目标数据的值相同，表示满足条件
                         *
                         *
                         */
                        boolean isOk = (sourceValue == null && sValue.equals(tValue)) ||
                                (sourceValue != null && sourceValue.equals(sValue) && targetValue.equals(tValue));
                        if(!isOk) {
                            sameDimensonData = false;
                            break;
                        }
                    }
                    if(sameDimensonData) {
                        //找到目标数据，合并指标
                        for(String field : dataFields) {
                            if(targetData.containsKey(field)) {
                                data.put(field,targetData.get(field));
                            }
                        }
                    }
                }
            }

        }

        return mergeResult;
    }
}
