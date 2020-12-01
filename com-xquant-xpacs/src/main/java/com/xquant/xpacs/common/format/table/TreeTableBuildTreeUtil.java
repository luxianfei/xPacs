/**
 * ******************************************
 * 文件名称: BuildTreeUtil.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: 1.0.0
 * @author: yt.zhou
 * 开发时间: 2019年04月12日 18:16:11
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.common.format.table;

import com.xquant.common.bean.bo.TreeTableDataModel;
import com.xquant.common.util.StringUtils;

import java.util.*;

/**
 * @ClassName: BuildTreeUtil
 * @Description: 树形表格构建树
 * @author: yt.zhou
 * @date: 2019年04月12日 18:16:11
 *
 */
public class TreeTableBuildTreeUtil {
    /**
     * @author: yt.zhou
     * @date: 2019年01月31日 14:17
     * @description: 树形表格数据封装
     * @param list          数据集
     * @param nodeKey       节点key   通过nodeKey与parentNodeKey，来做父子关系
     * @param parentNodeKey 父节点key
     * @return java.util.List<java.util.Map>
     *
     */
    public static List<TreeTableDataModel> buildTree(List<Map<String, Object>> list, String nodeKey, String parentNodeKey){
        //相同父节点的数据放在一起
        Map<String,List<TreeTableDataModel>> nodeParentIdDataMap = new HashMap<>();
        Set<String> nodeParentIds = new HashSet<>();
        Set<String> nodeIds = new HashSet<>();
        for(Map<String, Object> data : list){
            //节点ID
            String nodeVal = StringUtils.defaultIfNull(data.get(nodeKey));
            //父节点ID
            String parentNodeVal = StringUtils.defaultIfNull(data.get(parentNodeKey));

            List<TreeTableDataModel> nodeList = nodeParentIdDataMap.get(parentNodeVal);
            if(nodeList == null){
                nodeList = new ArrayList<>();
                nodeParentIdDataMap.put(parentNodeVal,nodeList);
            }
            TreeTableDataModel treeTableModel = new TreeTableDataModel();
            treeTableModel.putAll(data);

            nodeList.add(treeTableModel);

            nodeParentIds.add(parentNodeVal);
            nodeIds.add(nodeVal);
        }

        //所有父节点Id  移除掉所有子节点的Id 剩余下的就是根节点的父Id,正常情况，剩余的只会有一个数据
        nodeParentIds.removeAll(nodeIds);

        //根节点的id
        if(nodeParentIds.size() == 0) {
        	return new ArrayList<>();
        }
        String rootParentId = nodeParentIds.toArray(new String[]{})[0];

        //得到根节点
        List<TreeTableDataModel> modelList = nodeParentIdDataMap.get(rootParentId);

        recursive(modelList,nodeParentIdDataMap,nodeKey);
        modelList.get(0).put("expanded",true);
        return modelList;
    }

    /**
     * @author: yt.zhou
     * @date: 2019年01月31日 15:01
     * @description: 递归循环，封装树形结构的数据
     * @param parentModelList
     * @param nodeParentIdDataMap
     * @return java.util.List<java.util.Map>
     *
     */
    private static void recursive(List<TreeTableDataModel> parentModelList,Map<String,List<TreeTableDataModel>> nodeParentIdDataMap,String nodeKey){

        for(TreeTableDataModel parentNode : parentModelList){
            //得到子节点数据
            String nodeId = StringUtils.defaultIfNull(parentNode.get(nodeKey));
            List<TreeTableDataModel> children = nodeParentIdDataMap.get(nodeId);
            if(children == null){
//                parentNode.put("children",new ArrayList<TreeTableDataModel>());
                parentNode.setChildren(new ArrayList<TreeTableDataModel>());
                continue;
            }

            parentNode.setChildren(children);
            recursive(children,nodeParentIdDataMap,nodeKey);
        }
    }

}
