package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricTrend;

/**
 * 
* 学院用电量
* @author shuyy
* @date 2018年9月7日
 */
public interface IShowElectricTrendService extends IService<ShowElectricTrend> {
	
	/**
	 * 
	* 学院用电
	* @param @param date
	* @param @return
	* @return Map<String,Object>
	* @throws
	* @author shuyy
	 */
	Map<String, Object> findAcademyElectricMap(String date);

}
