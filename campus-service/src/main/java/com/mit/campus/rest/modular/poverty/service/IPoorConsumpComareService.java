package com.mit.campus.rest.modular.poverty.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.PoorConsumpComare;

/**
 * 
* 二级页面，消费对比
* @author shuyy
* @date 2018年9月6日
 */
public interface IPoorConsumpComareService extends IService<PoorConsumpComare> {
	
	/**
	 * 
	* 获取贫困生消费对比的数据模式
	* @param @param college
	* @param @param major
	* @param @param poorType
	* @param @param increase
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	Page getPoorComparison(String college, String major, String poorType,String increase,int pageNum, int pageSize);
	
	/**
	 * 
	*  获取贫困生消费对比前十名
	* @param @param increase
	* @param @return
	* @return List<PoorConsumpComare>
	* @throws
	* @author shuyy
	 */
	/**
	 * 
	*  获取贫困生消费对比前十名
	* @param @param increase
	* @param @return
	* @return List<PoorConsumpComare>
	* @throws
	* @author shuyy
	 */
	List<PoorConsumpComare> getPoorConsumpTop10(String increase);
	
	/**
	 * 
	* 按学号查找学生在评定时间年份的所有月消费记录
	* @param @param studentID
	* @param @return
	* @return List<PoorStuRecords>
	* @throws
	* @author shuyy
	 */
	List<PoorConsumpComare> findStuMonthConsumpRecord(String studentID);
}
