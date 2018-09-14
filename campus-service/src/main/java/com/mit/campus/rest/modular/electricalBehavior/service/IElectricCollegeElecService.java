package com.mit.campus.rest.modular.electricalBehavior.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeElec;

/**
 * 
* 二级页面-学院用电分析
* @author shuyy
* @date 2018年9月7日
 */
public interface IElectricCollegeElecService extends IService<ElectricCollegeElec> {
	
	/**
	 * 
	* 二级页面-分页查询学院月用电量
	* @param @param collegeName
	* @param @param startMonth
	* @param @param endMonth
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	Page getCollegeElecByCondition(String collegeName, String startMonth, String endMonth, 
			int pageNum, int pageSize) ;

}
