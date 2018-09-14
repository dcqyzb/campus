package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ElectricDormInfoMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricDormInfo;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricDormInfoService;
import com.mit.campus.rest.modular.enrolldecision.dao.StudentInfoMapper;
import com.mit.campus.rest.modular.enrolldecision.model.StudentInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 宿舍信息实体类
 * 
 * @author shuyy
 * @date 2018年9月7日
 */
@Service
public class ElectricDormInfoServiceImpl extends ServiceImpl<ElectricDormInfoMapper, ElectricDormInfo>
		implements IElectricDormInfoService {

	@Autowired
	private ElectricDormInfoMapper electricDormInfoMapper;

	@Autowired
	private StudentInfoMapper studentInfoMapper;

	/**
	 * 
	 * 
	 * 二级页面-条件查询寝室用电信息
	 * 
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
	 * @throws @author shuyy
	 */
	@Override
	public Page findElectricByConition(String collegeName, String dormName, String roomName, String startMonth,
			String endMonth, String belong, int pageNum, int pageSize) {
		int totalNum = 0;
		int totalPage = 0;
		Page<ElectricDormInfo> page = new Page<>();
		// 查询结果
		List<ElectricDormInfo> list = null;
		// 排序规则
		String orderBy = "";
		EntityWrapper<ElectricDormInfo> wrapper = new EntityWrapper<>();
		if (belong.equals("highPower")) {
			wrapper.orderBy("date", false).orderBy("highPowerSusp", false).orderBy("highPowerSum", false)
					.orderBy("dormID");
		} else if (belong.equals("unclose")) {
			// 未关电器
			wrapper.orderBy("date", false).orderBy("uncloseNum", false).orderBy("dormID");
		} else {
			// 窃电
			wrapper.orderBy("date", false).orderBy("stealSusp", false).orderBy("dormID");
		}
		// 院系条件
		if (!StringUtils.isBlank(collegeName)) {
			wrapper.eq("collegeName", collegeName);
		}
		// 楼栋及宿舍条件
		if (!StringUtils.isBlank(dormName)) {
			if (!StringUtils.isBlank(roomName)) {
				wrapper.and(" dormId like {0}", dormName + "-%" + roomName + "%");
			} else {
				wrapper.and("dormID like {0}", dormName + "-%");
			}
		} else {
			if (!StringUtils.isBlank(roomName)) {
				wrapper.and("dormID like {0}", "%-%" + roomName + "%");
			}
		}
		// 发生时间区间条件，设置默认值
		if (StringUtils.isBlank(startMonth)) {
			startMonth = " ";
		}
		if (StringUtils.isBlank(endMonth)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			endMonth = format.format(new Date());
		}
		wrapper.between("date", startMonth, endMonth);
		// 查询语句
		if (pageSize > 0) {
			page.setSize(pageSize);
			page.setCurrent(pageNum);
			list = electricDormInfoMapper.selectPage(page, wrapper);
		} else {
			// 导出查询
			list = electricDormInfoMapper.selectList(wrapper);
		}
		page.setRecords(list);
		return page;
	}

	/**
	 * 
	 * 二级页面-获取该宿舍某时间的学生详情
	 * @param @param dormID
	 * @param @param date
	 * @param @return
	 * @return List<StudentInfo>
	 * @throws @author shuyy
	 */
	@Override
	public List<StudentInfo> getDormStudents(String dormID, String date) {
		// 查询到的所有学生
		List<StudentInfo> all_stus = new ArrayList<StudentInfo>();
		// 单个学生
		List<StudentInfo> stus = null;
		EntityWrapper<ElectricDormInfo> wrapper = new EntityWrapper<>();
		wrapper.eq("dormID", dormID).eq("date", date);
		try {
			// 查询到宿舍
			List<ElectricDormInfo> list = electricDormInfoMapper.selectList(wrapper);
			if (!list.isEmpty()) {
				// 宿舍中的学生学号
				String[] stuIDs = list.get(0).getStudents().split(",");
				for (int i = 0, len = stuIDs.length; i < len; i++) {
					String stu_sql;
					EntityWrapper<StudentInfo> entiryStudent = new EntityWrapper<>();
					entiryStudent.eq("studentID", stuIDs[i]);
					stus = studentInfoMapper.selectList(entiryStudent);
					if (stus.size() > 0) {
						// 加到返回结果中
						all_stus.add(stus.get(0));
					}
				}
				return all_stus;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
