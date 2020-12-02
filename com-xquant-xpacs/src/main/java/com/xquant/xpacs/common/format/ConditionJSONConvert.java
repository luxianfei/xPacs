/**
 * ******************************************
 * 文件名称: ConditionConvert.java
 * 系统名称: xALMS资产负债管理与量化分析系统
 * 模块名称: 量化报送
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年07月11日 10:34:42
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @ClassName: ConditionConvert
 * @Description: 条件与JSON转换
 * @author: yt.zhou
 * @date: 2019年07月11日 10:34:42
 *
 */
public class ConditionJSONConvert {
    /**
     * @author: yt.zhou
     * @date: 2019年07月11日 10:35
     * @description: 将条件转换为json格式，a=1&&b=2,c=3&&d=4 ----> [{a:1,b:2},{c:3,b:4}]
     * @param condition
     * @return java.lang.String
     *
     */
    public static String condition2Json(String condition){
        List<Map<String,Object>> conditionList = new ArrayList<>();
        String[] conditions = condition.split(",");
        for(String str : conditions) {
            //&&之间的条件为交集，所有条件满足
            Map<String,Object> cm = new HashMap<>();
            String[] oneConditions = str.split("&&");
            for(String onCon : oneConditions) {
                String[] cd = onCon.split("=");
                if(cd.length == 2) {
                    cm.put(cd[0],cd[1]);
                }
            }
            conditionList.add(cm);

        }
        return JSONArray.toJSONString(conditionList);
    }

    /**
     * @author: yt.zhou
     * @date: 2019年07月11日 10:35
     * @description: 将json格式转换为条件 [{a:1,b:2},{c:3,b:4}]   ---->   a=1&&b=2,c=3&&d=4
     * @param json
     * @return java.lang.String
     *
     */
    public static String json2Condition(String json) {
        JSONArray jsonArray = JSONArray.parseArray(json);
        String cons = "";
        for(int i=0;i<jsonArray.size();i++) {
            String oneCon = "";
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Iterator<String> it = jsonObject.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String val = jsonObject.getString(key);
                if(!"".equals(oneCon)) {
                	oneCon += "&&";
                }
                oneCon += key + "=" +val;
            }

            if(cons != "") {
                cons += ",";
            }

            cons += oneCon;
        }
        return cons;
    }


    /**
     * @author: yt.zhou
     * @date: 2019年07月11日 13:56
     * @description: 将条件字符串，转换为条件集合
     * @param condition 条件字符串：a=k:b,c=k:d&&e=v:f
     * @return java.lang.String
     *
     */
    public static List<List<ConditionModel>> conditionStr2ConditionList(String condition){
        List<List<ConditionModel>> conditionList = new ArrayList<>();
        if(condition == null) {
        	return conditionList;
        }
        String[] conditions = condition.split(",");

        Set<String> opCodes = ConditionModel.Operator.getAllCode();

        for(String str : conditions) {
            //&&之间的条件为交集，所有条件满足
            List<ConditionModel> cm = new ArrayList<>();
            String[] oneConditions = str.split("&&");
            for(String onCon : oneConditions) {

                char[] chars = onCon.toCharArray();
                int prePos = 0;
                ConditionModel conditionModel = new ConditionModel();
                for(int i=0;i<chars.length;i++) {
                    String dest = "";
                    if(chars[i] == '!' && chars[i+1] == '=') {
                        conditionModel.setSource(onCon.substring(prePos,i));
                        conditionModel.setOperator(ConditionModel.Operator.UNEQUAL);
                        dest = onCon.substring(i+2);
                        i++;
                    } else if(opCodes.contains(String.valueOf(chars[i]))) {
                        conditionModel.setSource(onCon.substring(prePos,i));
                        conditionModel.setOperator(ConditionModel.Operator.getOperatorByCode(String.valueOf(chars[i])));
                        dest = onCon.substring(i+1);
                    }

                    if(!"".equals(dest)) {
                        String[] destArr = dest.split(":");
                        if(destArr.length == 1) {
                            //默认为key
                            conditionModel.setDestType(ConditionModel.DestType.KEY);
                            conditionModel.setDest(dest);
                        } else if(destArr.length == 2) {
                            conditionModel.setDestType(ConditionModel.DestType.getDestTypeByCode(destArr[0].toLowerCase()));
                            conditionModel.setDest(destArr[1]);
                        }
                    }
                }
                cm.add(conditionModel);
            }
            conditionList.add(cm);

        }
        return conditionList;
    }

    public static void main(String[] args) {
        String str = "c=a";
        List<List<ConditionModel>> lists = conditionStr2ConditionList(str);
        System.out.println(lists.size());
    }
}
