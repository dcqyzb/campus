package com.mit.campus.rest.modular.poverty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.ShowPoorCollegeAverage;

/**
 * 
* 学院平均消费
* @author shuyy
* @date 2018年9月6日
 */
public interface IShowPoorCollegeAverageService extends IService<ShowPoorCollegeAverage> {
	
	/**
	* 
	* 各学院指定年份的消费总和统计
	* @param @param year
	* @param @return
	* @return List<Integer>
	* @throws
	 */
	List<Integer> findCollegeAverAge(String year);
	
	/**
	 * 
	* 
	* 各学院指定年份的消费总和统计， 按月统计
	* @param @param year
	* @param @param month
	* @param @return
	* @return ShowPoorCollegeAverage
	* @throws
	 */
	ShowPoorCollegeAverage findCollegeAverage(String year, String month);
	

}
