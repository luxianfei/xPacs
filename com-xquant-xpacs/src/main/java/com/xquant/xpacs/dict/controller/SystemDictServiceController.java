/**
 *******************************************
 * 文件名称: SystemDictServiceController.java
 * 系统名称: xPIMS受托投资服务管理系统
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 数据字典
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年6月22日 下午4:59:42
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.dict.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.base.dictionary.entity.dto.EnumDictionary;
import com.xquant.xpacs.base.http.HttpBaseResponse;
import com.xquant.xpacs.base.http.HttpBaseResponseUtil;
import com.xquant.xpacs.dict.service.api.ISystemDictService;

/**
 * @ClassName: SystemDictServiceController
 * @Description: 数据字典
 * @author: jingru.jiang
 * @date: 2020年6月22日 下午4:59:42
 *
 */
@RestController
@RequestMapping("/systemDict")
public class SystemDictServiceController {
	@Autowired
	private ISystemDictService systemDictService;

	/**
	 * @Title: getDictByType
	 * @Description: 根据类型获取数据字典
	 * @param: subType
	 * @return: HttpBaseResponse  
	 * @throws
	 */
	@GetMapping("/getDictByType")
	public HttpBaseResponse getDictByType(String subType) {
		List<EnumDictionary> list = systemDictService.getDictList(subType);
		HttpBaseResponse baseResponse = HttpBaseResponseUtil.getListResponse(list);
		return baseResponse;
	}
}
