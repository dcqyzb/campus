package com.mit.campus.rest.modular.poverty.service.imp;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.PoorStudentInfoMapper;
import com.mit.campus.rest.modular.poverty.model.PoorStudentInfo;
import com.mit.campus.rest.modular.poverty.service.IPoorStudentInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 
* 贫困学生信息
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class PoorStudentInfoServiceImpl extends ServiceImpl<PoorStudentInfoMapper, PoorStudentInfo> implements IPoorStudentInfoService {
	
	@Autowired
	private PoorStudentInfoMapper poorStudentInfoMapper;
	
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
	@Override
	public Page getPoorNum(String collegeName, String major, String poorRank, String startYear,String endYear, int pageNum,
			int pageSize) {
		EntityWrapper<PoorStudentInfo> wrapper = new EntityWrapper<>();
		wrapper.orderBy("college", true).orderBy("major", true).orderBy("poorRank", false).orderBy("poorYear", false);
		Page<PoorStudentInfo> page = new Page<>();
		boolean export = false;
		if(pageSize > 0 && pageNum > 0){
			page.setSize(pageSize);
			page.setCurrent(pageNum);
		}else{
			export = true;//当pageSize 小于等于0 时，导出
		}
		//评定年份区间条件，设置默认值
		if(StringUtils.isEmpty(startYear)){
			// 开始时间可为空
			startYear = " ";
		}
		if(StringUtils.isEmpty(endYear)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			endYear = format.format(new Date());//结束时间不可为空，所以设置为今天的日期
		}
		wrapper.between("poorYear", startYear, endYear);
		//学院条件
		if(!StringUtils.isBlank(collegeName)){
			wrapper.like("college", collegeName);
		}
		//专业条件
		if(!StringUtils.isBlank(major)){
			wrapper.like("major", major);
		}
		//贫困等级条件
		if(!StringUtils.isBlank(poorRank)){
			wrapper.like("poorRank", poorRank);
		}
		long totalNum = 0;
		List<PoorStudentInfo> list;
		if(export){
			//导出数据，查询全部
			list = poorStudentInfoMapper.selectList(wrapper);
			totalNum = list.size();
			page.setRecords(list);
		}else{
			//分页查询
			list = poorStudentInfoMapper.selectPage(page, wrapper);
			page.setRecords(list);
/*			//计数
			totalNum = page.getTotal();
//			totalNum  = (int) dao.count("select count(*) "+hql2);
			//总页数
			long pages = page.getPages();
//			int totalPage = (int) (totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1);
			p.setTotalPage(totalPage);*/
		}
		return page;
	}
	
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
	@Override
	public Page getPoorArea(String birthPlace, String college, String startYear,String endYear, String poorRank, int pageNum,
			int pageSize) {
		EntityWrapper<PoorStudentInfo> wrapper = new EntityWrapper<>();
		wrapper.orderBy("college", true).orderBy("birthPlace", true).orderBy("poorRank", false).orderBy("poorYear", false);
		List<PoorStudentInfo> list = null;//结果
		Page<PoorStudentInfo> page = new Page<>();
		boolean export = false;
		if(pageSize > 0){
			page.setSize(pageSize);
			page.setCurrent(pageNum);
		}else{
			export = true;
		}
		//生源地条件
		if(!StringUtils.isBlank(birthPlace)){
			wrapper.like("birthPlace", birthPlace);
		}
		//学院条件
		if(!StringUtils.isBlank(college)){
			wrapper.like("college", college);
		}
		//评定年份区间条件，设置默认值
		if(StringUtils.isBlank(startYear)){
			startYear = " ";//开始时间可为空
		}
		if(StringUtils.isBlank(endYear)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			endYear = format.format(new Date());//结束时间不可为空，所以设置为今天的日期
		}
		wrapper.between("poorYear", startYear, endYear);
		//贫困等级条件
		if(!StringUtils.isBlank(poorRank)){
			wrapper.eq("poorRank", poorRank);
		}
		if(export){
			//导出数据，查询全部
			list = poorStudentInfoMapper.selectList(wrapper);
			page.setRecords(list);
		}else{
			//分页查询
			list = poorStudentInfoMapper.selectPage(page, wrapper);
			page.setRecords(list);
		}
		return page;
	}

}
