/**
 * ******************************************
 * 文件名称: GroupTreeController.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月13日 09:20:56
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.group;

import com.xquant.group.base.entity.bo.TreeStoreNode;
import com.xquant.group.tree.service.api.GroupTreeService;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: GroupTreeController
 * @Description: 分组相关
 * @author: yt.zhou
 * @date: 2020年11月13日 09:20:56
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupTreeService gTreeService;

    @GetMapping("/getGroupTreeNode")
    public HttpBaseResponse getGroupTreeNode(String treeCode) {
        TreeStoreNode root = gTreeService.getGroupTreeNode(treeCode, null);
        HttpBaseResponse baseResponse = HttpBaseResponseUtil.getSuccessResponse(root);

        return baseResponse;
    }
}
