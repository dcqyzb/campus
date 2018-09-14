package com.mit.campus.rest.modular.poverty.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.ShowPoorForecast;

/**
 * 
* 贫困生预测
* @author shuyy
* @date 2018年9月6日
 */
public interface IShowPoorForecastService extends IService<ShowPoorForecast> {

	/**
	* 查询所有贫困生预测
	* @param @return
	* @return List<ShowPoorForecast>
	* @throws
	 */
	List<ShowPoorForecast> findPoorForecast();
	
	/**
	 * 
	* 获取下阶段贫困生预测最多的前十个学院
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	* @author shuyy
	 */
	List<Map<String, Object>> getTopForeast();
}
