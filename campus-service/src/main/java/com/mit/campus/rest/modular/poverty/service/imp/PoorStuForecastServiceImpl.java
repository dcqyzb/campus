package com.mit.campus.rest.modular.poverty.service.imp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.PoorStuForecastMapper;
import com.mit.campus.rest.modular.poverty.model.PoorStuForecast;
import com.mit.campus.rest.modular.poverty.service.IPoorStuForecastService;

/**
 * 
* 贫困生预测
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class PoorStuForecastServiceImpl extends ServiceImpl<PoorStuForecastMapper, PoorStuForecast> implements IPoorStuForecastService {
	
	@Autowired
	private PoorStuForecastMapper poorStuForecastMapper;
	
	/**
	 * 
	* 二级页面-分页查询贫困生预测名单
	* @param @param collegeName
	* @param @param major
	* @param @param poorRank
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Page
	* @throws
	* @author shuyy
	 */
	@Override
	public Page getPoorForecast(String collegeName, String major, String poorRank, int pageNum,int pageSize) {
		EntityWrapper<PoorStuForecast> wrapper = new EntityWrapper<>();
		wrapper.orderBy("collegeName", true).orderBy("major").orderBy("poorRank", false);		
		List<PoorStuForecast> list = null;
		Page<PoorStuForecast> page = new Page<>();
		boolean export = false;
		int totalNum = 0;
		if(pageSize > 0){
			page.setCurrent(pageNum);
			page.setSize(pageSize);
		}else{
			export = true;
		}
		//学院条件
		if(!StringUtils.isBlank(collegeName)){
			wrapper.like("collegeName", collegeName);
		}
		//专业条件
		if(!StringUtils.isBlank(major)){
			wrapper.like("major", major);
		}
		//贫困等级条件
		if(!StringUtils.isBlank(poorRank)){
			wrapper.eq("poorRank", poorRank);
		}
		if(export){
			//导出数据，查询全部
			list = poorStuForecastMapper.selectList(wrapper);
		}else{
			list = poorStuForecastMapper.selectPage(page, wrapper);
		}
		page.setRecords(list);
		return page;
	}	

}
