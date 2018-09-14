package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ElectricCollegeElecMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeElec;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeElecService;

/**
 * 
* 二级页面-学院用电分析
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ElectricCollegeElecServiceImpl extends ServiceImpl<ElectricCollegeElecMapper, ElectricCollegeElec> implements IElectricCollegeElecService {
	
	@Autowired
	private ElectricCollegeElecMapper electricCollegeElecMapper;
	
	/**
	 * 
	* 二级页面-分页查询学院月用电量
	* @param @param collegeName
	* @param @param startMonth
	* @param @param endMonth
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	@Override
	public Page getCollegeElecByCondition(String collegeName, String startMonth, String endMonth, 
			int pageNum, int pageSize) {	
			List<ElectricCollegeElec> list = null;//结果
			Page<ElectricCollegeElec> page = new Page<>();
			int totalNum = 0;
			String hql_college = "";
			EntityWrapper<ElectricCollegeElec> wrapper = new EntityWrapper<>();
			wrapper.orderBy("date", false).orderBy("avgElec", false).orderBy("collegeName", false);
			//学院条件
			if(!StringUtils.isBlank(collegeName)){
				wrapper.and("collegeName like {0}", collegeName+"%");
			}
			//评定年份区间条件，设置默认值
			if(StringUtils.isBlank(startMonth)){
				startMonth = " ";//开始时间可为空
			}
			if(StringUtils.isBlank(endMonth)){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
				endMonth = format.format(new Date());
			}
			// 按时间区间查询的条件语句
			wrapper.between("date", startMonth, endMonth);
			if(pageSize > 0){
				//分页查询
				page.setSize(pageSize);
				page.setCurrent(pageNum);
				list = electricCollegeElecMapper.selectPage(page, wrapper);
			}else{
				//当pageSize 小于等于0 时，导出数据，查询全部
				list = electricCollegeElecMapper.selectList(wrapper);
			}
			page.setRecords(list);
			return page;
		}

}
