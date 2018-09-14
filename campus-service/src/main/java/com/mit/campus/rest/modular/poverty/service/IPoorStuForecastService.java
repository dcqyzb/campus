package com.mit.campus.rest.modular.poverty.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.PoorStuForecast;

/**
 * 
* 贫困生预测
* @author shuyy
* @date 2018年9月6日
 */
public interface IPoorStuForecastService extends IService<PoorStuForecast> {
	
	/**
	 * 
	* 二级页面-分页查询贫困生预测名单
	* @param @param collegeName
	* @param @param major
	* @param @param poorRank
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	Page getPoorForecast(String collegeName, String major, String poorRank, int pageNum,int pageSize);

	

}
