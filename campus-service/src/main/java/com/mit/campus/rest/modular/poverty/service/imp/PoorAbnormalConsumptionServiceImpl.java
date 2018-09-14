package com.mit.campus.rest.modular.poverty.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.PoorAbnormalConsumptionMapper;
import com.mit.campus.rest.modular.poverty.model.PoorAbnormalConsumption;
import com.mit.campus.rest.modular.poverty.service.IPoorAbnormalConsumptionService;

/**
 * 
*  异常消费信息表
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class PoorAbnormalConsumptionServiceImpl extends ServiceImpl<PoorAbnormalConsumptionMapper, PoorAbnormalConsumption> implements IPoorAbnormalConsumptionService {
	
	@Autowired
	private PoorAbnormalConsumptionMapper poorAbnormalConsumptionMapper;

	
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
	@Override
	public Page getAbnormalConsumption(String college, String startYear,String endYear, String type, String stuname,int pageNum,
			int pageSize) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		EntityWrapper<PoorAbnormalConsumption> wrapper = new EntityWrapper<>();
		wrapper.orderBy("college").orderBy("yyyymmdd", false);
		Page<PoorAbnormalConsumption> page = new Page();
		boolean export = false;
		int totalNum = 0;
		List<PoorAbnormalConsumption> list;
		if(pageSize > 0){
			page.setCurrent(pageNum);
			page.setSize(pageSize);
		}else{
			export = true;
		}
		//时间查询条件
		if(!StringUtils.isBlank(startYear)){
			//默认从一个月第一天开始
			startYear = startYear + "-01";
		}else{
			startYear = " ";
		}
		if(!StringUtils.isBlank(endYear)){
			//默认从一个月第一天开始
			endYear = endYear + "-01";
		}else{
			endYear = format.format(new Date());
		}
		wrapper.between("yyyymmdd", startYear, endYear);
		//学院条件
		if(!StringUtils.isBlank(college)){
			wrapper.like("college", college);
		}
		//消费类型条件
		if(!StringUtils.isBlank(type)){
			wrapper.eq("type", type);
		}
		//按学生姓名查询
		if(!StringUtils.isBlank(stuname)){
			wrapper.like("stuname", stuname);
		}
		if(export){
			//导出数据，查询全部
			list = poorAbnormalConsumptionMapper.selectList(wrapper);
		}else{
			//分页查询
			list = poorAbnormalConsumptionMapper.selectPage(page, wrapper);
		}
		page.setRecords(list);
		return page;
	}
	
	
	/**
	 * getTopAbnCon
	 * @描述： 异常消费人数最多的十个学院
	 * @作者： edgwong
	 * @日期：2018年3月30日
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getTopAbnCon(String year) {
		//按学院统计人数最多的前十名
		EntityWrapper<PoorAbnormalConsumption> wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect("college,count(*) num");
		wrapper.like("yyyymmdd", year);
		wrapper.groupBy("college");
		wrapper.orderBy("count(*)", false);
		Page<PoorAbnormalConsumption> page = new Page<>();
		List<Map<String, Object>> result = poorAbnormalConsumptionMapper.selectMapsPage(page, wrapper);
		return result;
	}
}
