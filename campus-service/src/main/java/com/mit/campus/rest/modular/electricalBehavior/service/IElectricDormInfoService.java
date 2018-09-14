package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricDormInfo;
import com.mit.campus.rest.modular.enrolldecision.model.StudentInfo;

/**
 * 
* 宿舍信息实体类
* @author shuyy
* @date 2018年9月7日
 */
public interface IElectricDormInfoService extends IService<ElectricDormInfo> {
	
	/**
	 * 
	* 
	* 二级页面-条件查询寝室用电信息
	* @param @param collegeName
	* @param @param dormName
	* @param @param roomName
	* @param @param startMonth
	* @param @param endMonth
	* @param @param belong
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	Page findElectricByConition(String collegeName, String dormName, String roomName,
			String startMonth, String endMonth,String belong, int pageNum,int pageSize);
	
	/**
	 * 
	 * 二级页面-获取该宿舍某时间的学生详情
	 * @param @param dormID
	 * @param @param date
	 * @param @return
	 * @return List<StudentInfo>
	 * @throws @author shuyy
	 */
	List<StudentInfo> getDormStudents(String dormID, String date);

}
