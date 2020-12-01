/**
 * ******************************************
 * 文件名称: ConditionModel.java
 * 系统名称: xALMS资产负债管理与量化分析系统
 * 模块名称: 量化报送
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年07月11日 13:57:39
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format;

import com.xquant.common.util.NumberUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: ConditionModel
 * @Description: 条件模型  a=b，source=a,dest=b,destType=key
 * @author: yt.zhou
 * @date: 2019年07月11日 13:57:39
 *
 */
public class ConditionModel {
    //源数据key
    private String source;
    //目标数据key
    private String dest;
    //目标数据类型，key,value
    private DestType destType;
    //条件，等于，不等于
    private Operator operater;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public DestType getDestType() {
        return destType;
    }

    public void setDestType(DestType destType) {
        this.destType = destType;
    }

    public Operator getOperater() {
        return operater;
    }

    public void setOperater(Operator operater) {
        this.operater = operater;
    }

    public enum Operator{
        EQUAL("=","等于"){
            @Override
            public boolean isOkCondition(String source, String dest) {
                Double d1 = NumberUtils.convertToDouble(source,null);
                Double d2 = NumberUtils.convertToDouble(dest,null);
                if(d1 == null || d2 == null) {
                    return source.equals(dest);
                } else {
                    return d1.equals(d2);
                }
            }
        },
        UNEQUAL("!=","不等于"){
            @Override
            public boolean isOkCondition(String source, String dest) {
                return !source.equals(dest);
            }
        },
        LIKE("%","包含"){
            @Override
            public boolean isOkCondition(String source, String dest) {
                return source.contains(dest);
            }
        },
        GT(">","大于") {
            //大于，值对double类型做比较
            @Override
            public boolean isOkCondition(String source, String dest) {
                Double d1 = NumberUtils.convertToDouble(source,null);
                Double d2 = NumberUtils.convertToDouble(dest,null);
                if(d1 == null || d2 == null) {
                    return false;
                } else {
                    return d1.doubleValue() > d2.doubleValue();
                }
            }
        },
        LT("<","小于") {
            //小于，值对double类型做比较
            @Override
            public boolean isOkCondition(String source, String dest) {
                Double d1 = NumberUtils.convertToDouble(source,null);
                Double d2 = NumberUtils.convertToDouble(dest,null);
                if(d1 == null || d2 == null) {
                    return false;
                } else {
                    return d1.doubleValue() < d2.doubleValue();
                }
            }
        }
        ;
        private String code;
        private String name;

        Operator(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static Operator getOperatorByCode(String code) {
            Operator[] values = values();
            for(Operator operator : values) {
                if(operator.code.equals(code)) {
                	return operator;
                }
            }
            return null;
        }
        /**
         * @author: yt.zhou
         * @date: 2019年07月22日 9:08
         * @description:  获取所有
         * @return com.xquant.product.xqa.app.common.util.ConditionModel.Operator[]
         *
         */
        public static Operator[] getOperators() {
            return values();
        }
        /**
         * @author: yt.zhou
         * @date: 2019年07月22日 9:10
         * @description:  获取所有的比较符号
         * @return java.util.Set<java.lang.String>
         *
         */
        public static Set<String> getAllCode(){
            Operator[] operators = getOperators();
            Set<String> codes = new HashSet<>();
            for(Operator operator : operators) {
                codes.add(operator.getCode());
            }
            return codes;
        }
        /**
         * @author: yt.zhou
         * @date: 2019年07月11日 14:41
         * @description: 判断是否满足条件
         * @param source
         * @param dest
         * @return boolean 满足条件 true
         *
         */
        public abstract boolean isOkCondition(String source,String dest);
    }

    public enum DestType{
        KEY("k"),
        VALUE("v");
        private String code;

        public String getCode() {
            return code;
        }

        DestType(String code) {
            this.code = code;
        }
        public static DestType getDestTypeByCode(String code) {
            DestType[] values = values();
            for(DestType destType : values) {
                if(destType.code.equals(code)) {
                    return destType;
                }
            }
            return null;
        }
    }
}
