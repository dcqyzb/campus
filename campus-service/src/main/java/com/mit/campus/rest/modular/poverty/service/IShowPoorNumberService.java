package com.mit.campus.rest.modular.poverty.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.ShowPoorNumber;
/**
 * 
* 精准资助贫困人数按学院分布统计数据
* @author shuyy
* @date 2018年9月6日
 */
public interface IShowPoorNumberService extends IService<ShowPoorNumber>{
	
	
	List<ShowPoorNumber> selectByYear(String year);
	
	/**
	 * 
	* 查找各学院的贫困生人数分布
	* @param @param year
	* @param @param top10
	* @param @return
	* @return List<ShowPoorNumber>
	* @throws
	 */
	List<ShowPoorNumber> findPoorNumber(String year,boolean top10);
}
