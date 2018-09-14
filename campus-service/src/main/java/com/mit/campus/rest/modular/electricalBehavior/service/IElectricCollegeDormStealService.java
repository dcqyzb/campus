package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormSteal;

/**
 * 
* 二级页面-学院窃电宿舍，某月某学院发现窃电的宿舍信息
* @author shuyy
* @date 2018年9月7日
 */
public interface IElectricCollegeDormStealService extends IService<ElectricCollegeDormSteal> {
	
	 /**
     * 
    * 二级页面-按宿舍时间查询该宿舍历史窃电详情
    * @param @param dormId
    * @param @param date
    * @param @return
    * @return List<ElectricCollegeDormSteal>
    * @throws
    * @author shuyy
     */
	List<ElectricCollegeDormSteal> getDormStealHistory(String dormId, String date);

}
