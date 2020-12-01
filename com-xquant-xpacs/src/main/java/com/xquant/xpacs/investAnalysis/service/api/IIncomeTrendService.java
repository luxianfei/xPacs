/**
 *******************************************
 * 文件名称: IIncomeTrendService.java
 * 系统名称: xPacs委托人投资监督服务平台
 * 模块名称: 参数管理
 * 软件版权: 杭州衡泰软件有限公司
 * @Description: 收益走势
 * @version: 1.0.0.1
 * @author: jingru.jiang
 * 开发时间: 2020年11月9日 下午2:49:44
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *
 ********************************************/
package com.xquant.xpacs.investAnalysis.service.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.xquant.xpacs.investAnalysis.entity.dto.IncomeTrendParamDTO;

/**
 * @ClassName: IIncomeTrendService
 * @Description: 收益走势
 * @author: jingru.jiang
 * @date: 2020年11月9日 下午2:49:44
 *
 */
public interface IIncomeTrendService {
	
	/**
	 * @Title: calcIncomeTrend
	 * @Description: 收益走势计算
	 * @param: incomeTrendParamDTO
	 * @return: Future<List<Map<String,Object>>>   
	 * @throws
	 */
	Future<List<Map<String,Object>>> calcIncomeTrend(IncomeTrendParamDTO incomeTrendParamDTO);

}
