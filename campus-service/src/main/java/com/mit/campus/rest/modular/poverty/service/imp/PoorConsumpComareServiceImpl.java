package com.mit.campus.rest.modular.poverty.service.imp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.PoorConsumpComareMapper;
import com.mit.campus.rest.modular.poverty.model.PoorConsumpComare;
import com.mit.campus.rest.modular.poverty.model.PoorStuRecords;
import com.mit.campus.rest.modular.poverty.service.IPoorConsumpComareService;

/**
 * 
* 二级页面，消费对比
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class PoorConsumpComareServiceImpl extends ServiceImpl<PoorConsumpComareMapper, PoorConsumpComare> implements IPoorConsumpComareService {
	
	@Autowired
	private PoorConsumpComareMapper poorConsumpComareMapper;
	
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
	@Override
	public Page getPoorComparison(String college, String major, String poorType,String increase,int pageNum, int pageSize) {
		EntityWrapper<PoorConsumpComare> wrapper = new EntityWrapper<>();
		wrapper.orderBy("increase", false);
		//一个studentID一条记录
		List<PoorConsumpComare> list = null;//结果
		Page<PoorConsumpComare> page = new Page();
		boolean export = false;
		int totalNum = 0;
		if(pageSize > 0){
			page.setSize(pageSize);
			page.setCurrent(pageNum);
		}else{
			export = true;//当pageSize 小于等于0 时，导出
		}
		//学院条件
		if(!StringUtils.isBlank(college)){
			wrapper.like("college", college);
		}
		//专业条件
		if(!StringUtils.isBlank(major)){
			wrapper.like("major", major);
		}
		//贫困等级条件
		if(!StringUtils.isBlank(poorType)){
			wrapper.eq("poorRank", poorType);
		}
		//正负增长条件
		if(!StringUtils.isBlank(increase)){
			if(increase.equals("负")){
				wrapper.and("increase like {0}", "-%");
			}
			else{
				wrapper.and("increase not like {0}", "-%");
			}
		}
		if(export){
			//导出数据，查询全部
			wrapper.orderBy("college").orderBy("major", false).orderBy("stuName").orderBy("month", false);
			list = poorConsumpComareMapper.selectList(wrapper);
		}else{
			//贫困生评定年份yyyy
			String assessment_year = getassessmentYear();
			//添加时间条件，排除已毕业的贫困生记录和以往的贫困生记录
			wrapper.eq("month", assessment_year+"-01");
			list = poorConsumpComareMapper.selectPage(page, wrapper);
		}
		page.setRecords(list);
		return page;
	}
	
	/**
	 * 
	* 
	* 根据当前年月，获取贫困生评定时间（当前月不满10个月，就获取去年评定时间，否则，获取今年）
	* @param @return
	* @return String
	* @throws
	* @author shuyy
	 */
	public String getassessmentYear(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//贫困生评定固定月份
		//	String MONTH = "06";
		//当前时间
		Calendar calendar = Calendar.getInstance();
		//当前月份<10,展示去年评定前后的涨幅（为满足前后3个月时间）
		if(calendar.get(Calendar.MONTH)+1 <10){
			calendar.add(Calendar.YEAR,-1);
		}
		//贫困生评定年份yyyy
		String assessment_year = sdf.format(calendar.getTime()).split("-")[0];
		return assessment_year;
	}
	/**
	 * 
	*  获取贫困生消费对比前十名
	* @param @param increase
	* @param @return
	* @return List<PoorConsumpComare>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<PoorConsumpComare> getPoorConsumpTop10(String increase){
		List<PoorConsumpComare> list = null;
		//贫困生评定年份yyyy
		String assessment_year = getassessmentYear();
		EntityWrapper<PoorConsumpComare> wrapper = new EntityWrapper<>();
		wrapper.eq("month", assessment_year+"-01");
		//正增长
		if(increase.equals("正")){
			wrapper.and("increase not like '-%'");
		} else{//负增长
			wrapper.and("increase like '-%'");
			wrapper.orderBy("increase", false);
		}
		Page<PoorConsumpComare> page = new Page<>(1, 10);
		list = poorConsumpComareMapper.selectPage(page, wrapper);
		return list;
	}
	
	/**
	 * 
	* 按学号查找学生在评定时间年份的所有月消费记录
	* @param @param studentID
	* @param @return
	* @return List<PoorStuRecords>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<PoorConsumpComare> findStuMonthConsumpRecord(String studentID){
		List<PoorConsumpComare> list = null;
		//贫困生评定年份yyyy
		String assessment_year = getassessmentYear();
		EntityWrapper<PoorConsumpComare> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("studentID", studentID).and("month like {0}", assessment_year+"%").orderBy("month");
		list = poorConsumpComareMapper.selectList(entityWrapper);
		return list;
	}

}
