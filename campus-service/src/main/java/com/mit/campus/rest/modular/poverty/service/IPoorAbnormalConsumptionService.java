package com.mit.campus.rest.modular.poverty.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.PoorAbnormalConsumption;

/**
 * 
*  异常消费信息表
* @author shuyy
* @date 2018年9月6日
 */
public interface IPoorAbnormalConsumptionService extends IService<PoorAbnormalConsumption> {
	
	
	/**
	 * 
	* 获取贫困生异常消费情况的数据模式
	* @param @param college
	* @param @param startYear
	* @param @param endYear
	* @param @param type
	* @param @param stuname
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	Page getAbnormalConsumption(String college, String startYear,String endYear, String type, String stuname,int pageNum,
			int pageSize);

	/**
	 * getTopAbnCon
	 * @描述： 异常消费人数最多的十个学院
	 * @作者： edgwong
	 * @日期：2018年3月30日
	 * @return
	 */
	List<Map<String, Object>> getTopAbnCon(String year);
}
