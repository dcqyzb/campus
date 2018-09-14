package com.mit.campus.rest.modular.poverty.service.imp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.PoorStuRecordsMapper;
import com.mit.campus.rest.modular.poverty.model.PoorStuRecords;
import com.mit.campus.rest.modular.poverty.service.IPoorStuRecordsService;

/**
 * 
* 二级页面-学生历史助学金、奖学金详情
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class PoorStuRecordsServiceImpl extends ServiceImpl<PoorStuRecordsMapper, PoorStuRecords> implements IPoorStuRecordsService {

	@Autowired
	private PoorStuRecordsMapper poorStuRecordsMapper;
	
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
	@Override
	public List<PoorStuRecords> getByStuNameAndStuId(String stuName, String stuId) {
		EntityWrapper<PoorStuRecords> wrapper = new EntityWrapper<>();
		wrapper.orderBy("poorYear", false);
		//学生姓名
		if(!StringUtils.isBlank(stuName)){
			wrapper.eq("stuName", stuName);
		}
		//学生学号
		if(!StringUtils.isBlank(stuId)){
			wrapper.eq("studentID", stuId);
		}
		List<PoorStuRecords> list = poorStuRecordsMapper.selectList(wrapper);
		return list;
	}
	
	
}
