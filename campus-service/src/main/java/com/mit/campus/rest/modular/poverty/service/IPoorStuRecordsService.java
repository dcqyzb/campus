package com.mit.campus.rest.modular.poverty.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.PoorStuRecords;

/**
 * 
* 二级页面-学生历史助学金、奖学金详情
* @author shuyy
* @date 2018年9月6日
 */
public interface IPoorStuRecordsService extends IService<PoorStuRecords> {
	
	/**
	 * 
	* 查询某学生的资助记录
	* @param @param stuName
	* @param @param stuId
	* @param @return
	* @return List<PoorStudentRecords>
	* @throws
	* @author shuyy
	 */
	List<PoorStuRecords> getByStuNameAndStuId(String stuName, String stuId);
	
	
}
