package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricSteal;

/**
 * 
* 宿舍窃电嫌疑、违章电器嫌疑
* @author shuyy
* @date 2018年9月7日
 */
public interface IShowElectricStealService extends IService<ShowElectricSteal> {
	
	/**
	 * 
	* 获取某栋当前月的上一个月窃电嫌疑度高的前4，近两年用电变化
	* @param @param position
	* @param @return
	* @return List<List<ShowElectricSteal>>
	* @throws
	* @author shuyy
	 */
	List<List<ShowElectricSteal>> findStealElectricTop4(String position);

}
