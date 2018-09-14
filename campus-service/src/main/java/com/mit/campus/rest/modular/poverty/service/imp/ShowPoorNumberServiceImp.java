package com.mit.campus.rest.modular.poverty.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorNumberMapper;
import com.mit.campus.rest.modular.poverty.model.ShowPoorNumber;
import com.mit.campus.rest.modular.poverty.service.IShowPoorNumberService;
/**
 * 
* 精准资助贫困人数按学院分布统计数据
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class ShowPoorNumberServiceImp extends ServiceImpl<ShowPoorNumberMapper, ShowPoorNumber> implements IShowPoorNumberService{
	
	@Autowired
	private ShowPoorNumberMapper showPoorNumberMapper;

	
	@Override
	public List<ShowPoorNumber> selectByYear(String year) {
		List<ShowPoorNumber> list = showPoorNumberMapper.selectList(
				new EntityWrapper<ShowPoorNumber>().eq("year", year).orderBy("total", false)
		);
		return list;
	}
	
	/**
	 * 
	* 查找各学院的贫困生人数分布
	* @param @param year
	* @param @param top10
	* @param @return
	* @return List<ShowPoorNumber>
	* @throws
	 */
	public List<ShowPoorNumber> findPoorNumber(String year,boolean top10) {
		if(year == null || year.trim().equals("")){
			return null;
		}
		List<ShowPoorNumber> list = null;
		Wrapper<ShowPoorNumber> wrapper = new EntityWrapper<ShowPoorNumber>().eq("year", year).orderBy("total", false);
		//如果是二级页面top10模块的请求，只需要前十条记录
		if(top10){
			Page<ShowPoorNumber> page = new Page<>(1, 10);
			list = showPoorNumberMapper.selectPage(page, wrapper);
		}else{
			list = showPoorNumberMapper.selectList(wrapper);
		}
		return list;
	}

	
}
