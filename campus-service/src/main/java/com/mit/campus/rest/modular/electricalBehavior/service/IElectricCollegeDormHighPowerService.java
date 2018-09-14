package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormHighPower;

/**
 * 
* 二级页面-学院违章宿舍，某月某学院发现违章的宿舍信息
* @author shuyy
* @date 2018年9月7日
 */
public interface IElectricCollegeDormHighPowerService extends IService<ElectricCollegeDormHighPower> {
	
	 /**
     * 
    * 二级页面-按宿舍时间查询该宿舍历史违章详情
    * @param @param dormId
    * @param @param date
    * @param @return
    * @return List<ElectricCollegeDormHighPower>
    * @throws
    * @author shuyy
     */
	List<ElectricCollegeDormHighPower> getDormHighPowerHistory(String dormId, String date);
    
}
