/**
 * ******************************************
 * 文件名称: TprtServiceImpl.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年07月09日 16:50:46
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant.xpacs.base.port.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xquant.xpacs.base.port.service.api.ITprtService;
import com.xquant.xpims.tprt.entity.po.Tprt;
import com.xquant.xpims.tprt.entity.po.TprtExample;
import com.xquant.xpims.tprt.entity.po.TprtExample.Criteria;
import com.xquant.xpims.tprt.mapper.ext.TprtExtMapper;

/**
 * @ClassName: TprtServiceImpl
 * @Description: 组合相关接口
 * @author: yt.zhou
 * @date: 2020年07月09日 16:50:46
 */
@Service
public class TprtServiceImpl implements ITprtService {

    @Autowired
    private TprtExtMapper tprtMapper;

    @Autowired
    private TprtExtMapper tprtExtMapper;

    @Override
    public List<Tprt> getPortListByPlanCode(String planCode) {
        TprtExample example = new TprtExample();
        example.createCriteria().andPlanCodeEqualTo(planCode);
        List<Tprt> list = tprtMapper.selectByExample(example);
        return list;
    }

    @Override
    public Map<String, String> getLatestNavDateByPlanCode(String planCodes) {
        List<String> planCodeList = Arrays.asList(planCodes);
        return tprtExtMapper.getMaxNavDateByPlanCode(planCodeList);
    }

    @Override
    public Tprt getTprtByPortCode(String portCode) {
        return tprtMapper.selectByPrimaryKey(portCode);
    }

    @Override
    public Map<String, String> getLatestNavDateByPortCode(String portCode) {
        List<String> portCodes = new ArrayList<>();
        portCodes.add(portCode);
        return tprtExtMapper.getMaxNavDateByPortCode(portCodes);
    }

	/**
	 * <p>Title: getRetainPortListByPlanCode</p>
	 * <p>Description: </p>
	 * @param planCode
	 * @param tDate
	 * @return
	 * @see com.xquant.xpacs.base.port.service.api.ITprtService#getRetainPortListByPlanCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Tprt> getRetainPortListByPlanCode(String planCode, String tDate) {
		TprtExample example = new TprtExample();
        Criteria criteria = example.createCriteria();
        criteria.andPlanCodeEqualTo(planCode);
        criteria.andOverDateGreaterThanOrEqualTo(tDate);
        
        Criteria criteria2 = example.createCriteria();
        criteria2.andPlanCodeEqualTo(planCode);
        criteria2.andOverDateIsNull();
        example.or(criteria2);
        
        List<Tprt> list = tprtMapper.selectByExample(example);
        return list;
	}
}
