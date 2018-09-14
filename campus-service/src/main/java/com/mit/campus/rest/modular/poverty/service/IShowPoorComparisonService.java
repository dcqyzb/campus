package com.mit.campus.rest.modular.poverty.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.ShowPoorComparison;

/**
 * 
* 学生消费对比
* @author shuyy
* @date 2018年9月6日
 */
public interface IShowPoorComparisonService extends IService<ShowPoorComparison> {

	/**
	 * 
	* 
	* 通过年获取学生消费对比
	* @param @param year
	* @param @return
	* @return List<ShowPoorComparison>
	* @throws
	 */
	List<ShowPoorComparison> findPoorComparisonByYear(String year);
}
