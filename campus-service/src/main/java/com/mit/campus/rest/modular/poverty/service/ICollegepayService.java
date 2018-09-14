package com.mit.campus.rest.modular.poverty.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.CollegePay;


public interface ICollegepayService extends IService<CollegePay> {
	
	/**
	 * 
	* 获取学院平均消费的数据模式
	* @param @param collegeName
	* @param @param startYear
	* @param @param endYear
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	Page getCollegeAverage(String collegeName, String startYear,String endYear, int pageNum, int pageSize);

	
	/**
	 * 
	* 查询某学院某一年十二个月的各项消费金额
	* @param @param collegeName
	* @param @param Year
	* @param @return
	* @return List<CollegePay>
	* @throws
	* @author shuyy
	 */
	List<CollegePay> getByCollegeAndYear(String collegeName, String Year);
}
