package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ShowElectricUnclosedRankMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricUnclosedRank;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricUnclosedRankService;

/**
 * 
* 未关电器分布表
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ShowElectricUnclosedRankServiceImpl extends ServiceImpl<ShowElectricUnclosedRankMapper, ShowElectricUnclosedRank> implements IShowElectricUnclosedRankService {

	@Autowired
	private ShowElectricUnclosedRankMapper showElectricUnclosedRankMapper;
	
	/**
	 * 
	* 按时间获取未关电器占比
	* @param @param year
	* @param @return
	* @return List<ShowElectricUnclosedRank>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<ShowElectricUnclosedRank> findUnclosedRank(String year) {
		if(StringUtils.isBlank(year)){
			return null;
		}
		EntityWrapper<ShowElectricUnclosedRank> wrapper = new EntityWrapper<>();
		wrapper.like("yyyymm", year);
		List<ShowElectricUnclosedRank> list = null;
		return list = showElectricUnclosedRankMapper.selectList(wrapper);
	}
	
	/**
	 * 
	* 按时间获取未关电器占比
	* @param @param year
	* @param @param month
	* @param @return
	* @return List<ShowElectricUnclosedRank>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<ShowElectricUnclosedRank> findUnclosedRank(String year, String month) {
		if(StringUtils.isBlank(year)|| StringUtils.isBlank(month)){
			return null;
		}
		month = (Integer.parseInt(month) > 9) ? month : (month.indexOf("0") != -1) ? month : "0"+month;
		List<ShowElectricUnclosedRank> list = null;
		EntityWrapper<ShowElectricUnclosedRank> wrapper = new EntityWrapper<>();
		wrapper.like("yyyymm", year+"-"+month);
		list = showElectricUnclosedRankMapper.selectList(wrapper);
		return list;
	}
}
