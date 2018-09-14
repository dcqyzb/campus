package com.mit.campus.rest.modular.poverty.service.imp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.CollegePayMapper;
import com.mit.campus.rest.modular.poverty.model.CollegePay;
import com.mit.campus.rest.modular.poverty.model.ShowPoorCollegeAverage;
import com.mit.campus.rest.modular.poverty.service.ICollegepayService;
import com.mit.campus.rest.util.PageObject;

@Service
public class CollegePayServiceImpl extends ServiceImpl<CollegePayMapper, CollegePay> implements ICollegepayService {

	@Autowired
	private CollegePayMapper collegePayMapper;
	
	/**
	 * 
	* 获取学院平均消费的数据模式
	* @param @param collegeName
	* @param @param startYear
	* @param @param endYear
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	@Override
	public Page getCollegeAverage(String collegeName, String startYear,String endYear, int pageNum, int pageSize) {
		List<Map<String, Object>> result;
		PageObject<CollegePay> page = new PageObject<>();
		EntityWrapper<CollegePay> wrapper = new EntityWrapper<>();
		wrapper.groupBy("year").groupBy("collegeName").orderBy("year", false).orderBy("SUM(pay_value)", false);
		int totalNum = 0;
		//学院条件
		if(!StringUtils.isBlank(collegeName)){
			wrapper.and("collegeName like {0}", collegeName + "%");
		}
		//评定年份区间条件，设置默认值
		if(StringUtils.isBlank(startYear)){
			startYear = " ";
		}
		if(StringUtils.isBlank(endYear)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			endYear = format.format(new Date());
		}
		wrapper.between("year", startYear, endYear);
		if(pageSize > 0){
			page.setSize(pageSize);
			page.setCurrent(pageNum);
			wrapper.setSqlSelect("collegeName, year, collegePopu, SUM(hydro_value) hydroValue,"
					+ " SUM(repast_value) repastValue, SUM(ente_value) enteValue, "
					+ "SUM(other_value) otherValue,SUM(pay_value) payValue,updown");
			result = collegePayMapper.selectMapsPage(page, wrapper);
		}else{
			result = collegePayMapper.selectMaps(wrapper);
		}
		page.setObject(result);
		return page;
	}
	
	/**
	 * 
	* 查询某学院某一年十二个月的各项消费金额
	* @param @param collegeName
	* @param @param Year
	* @param @return
	* @return List<CollegePay>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<CollegePay> getByCollegeAndYear(String collegeName, String Year) {
		EntityWrapper<CollegePay> wrapper = new EntityWrapper<>();
		wrapper.orderBy("payValue", false);
		List<CollegePay> list = null;
		//学院条件
		if(!StringUtils.isBlank(collegeName)){
			wrapper.eq("collegeName", collegeName);
		}
		//年份条件
		if(StringUtils.isBlank(Year)){
			wrapper.eq("year", Year);
		}
		list = collegePayMapper.selectList(wrapper);
		return list;
	}
}
