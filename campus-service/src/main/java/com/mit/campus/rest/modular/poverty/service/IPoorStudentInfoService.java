package com.mit.campus.rest.modular.poverty.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.PoorStudentInfo;

/**
 * 
* 贫困学生信息
* @author shuyy
* @date 2018年9月6日
 */
public interface IPoorStudentInfoService extends IService<PoorStudentInfo> {
	
	/**
	 * 
	* 二级页面-贫困生人数分布查询
	* @param @param collegeName
	* @param @param major
	* @param @param poorRank
	* @param @param startYear
	* @param @param endYear
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	 */
	Page getPoorNum(String collegeName, String major, String poorRank, String startYear,String endYear, int pageNum,
			int pageSize);
	
	/**
	 * 
	* 获取贫困生生源地分布
	* @param @param birthPlace
	* @param @param college
	* @param @param startYear
	* @param @param endYear
	* @param @param poorRank
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	 */
	Page getPoorArea(String birthPlace, String college, String startYear,String endYear, String poorRank, int pageNum,
			int pageSize);

}
