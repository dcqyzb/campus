package com.mit.campus.rest.modular.poverty.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.ShowPoorAbnormalConsumption;

/**
 * 
 * 贫困生异常消费
* @author shuyy
* @date 2018年9月6日
 */
public interface IShowPoorAbnormalConsumptionService extends IService<ShowPoorAbnormalConsumption> {
	
	
	/* 
	 * 获取异常消费前几名，按年
	 */
	Map<String, Object> findAbnormalConsumption(String year, int num);

	/* 
	 *  获取异常消费前几名，按年月
	 */
	Map<String, Object> findAbnormalConsumption(String year, String month, int num);
}
