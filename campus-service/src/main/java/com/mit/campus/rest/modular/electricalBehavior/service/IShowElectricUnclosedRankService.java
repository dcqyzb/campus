package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricUnclosedRank;

/**
 * 
* 未关电器分布表
* @author shuyy
* @date 2018年9月7日
 */
public interface IShowElectricUnclosedRankService extends IService<ShowElectricUnclosedRank> {

	/**
	 * 
	* 按时间获取未关电器占比
	* @param @param year
	* @param @return
	* @return List<ShowElectricUnclosedRank>
	* @throws
	* @author shuyy
	 */
	List<ShowElectricUnclosedRank> findUnclosedRank(String year);
	
	/**
	 * 
	* 按时间获取未关电器占比
	* @param @param year
	* @param @param month
	* @param @return
	* @return List<ShowElectricUnclosedRank>
	* @throws
	* @author shuyy
	 */
	List<ShowElectricUnclosedRank> findUnclosedRank(String year, String month);
}
