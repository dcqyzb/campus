package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricHighPower;

/**
 * 
* 宿舍窃电嫌疑、违章电器嫌疑
* @author shuyy
* @date 2018年9月7日
 */
public interface IShowElectricHighPowerService extends IService<ShowElectricHighPower> {

	
	/**
	 * 
	* 按年查找所有楼栋违章电器使用嫌疑
	* @param @param date
	* @param @return
	* @return List<ShowElectricHighPower>
	* @throws
	* @author shuyy
	 */
	List<ShowElectricHighPower> findDormHighPowerByYear(String date);
	
	/**
	 * 
	* 按月查找所有楼栋违章电器使用嫌疑度
	* @param @param date
	* @param @return
	* @return List<ShowElectricHighPower>
	* @throws
	* @author shuyy
	 */
	List<ShowElectricHighPower> findDormHighPower(String date);
}
