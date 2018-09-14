package com.mit.campus.rest.modular.poverty.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorForecastMapper;
import com.mit.campus.rest.modular.poverty.model.ShowPoorForecast;
import com.mit.campus.rest.modular.poverty.service.IShowPoorForecastService;

/**
 * 
* 贫困生预测
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class ShowPoorForecastServiceImpl extends ServiceImpl<ShowPoorForecastMapper, ShowPoorForecast> implements IShowPoorForecastService {

	@Autowired
	private ShowPoorForecastMapper showPoorforecastMapper;
	/**
	* 查询所有贫困生预测
	* @param @return
	* @return List<ShowPoorForecast>
	* @throws
	 */
	@Override
	public List<ShowPoorForecast> findPoorForecast() {
		List<ShowPoorForecast> list = showPoorforecastMapper.selectByMap(null);
		return list;
	}
	
	/**
	 * 
	* 获取下阶段贫困生预测最多的前十个学院
	* @param @return
	* @return List<Map<String,Object>>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<Map<String, Object>> getTopForeast() {
		//按学院统计人数最多的前十名
//		String hql = "SELECT e.colleague,SUM(e.total) FROM ShowPoorForecast e GROUP BY e.colleague ORDER BY e.total DESC";
		EntityWrapper<ShowPoorForecast> wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect("colleague,SUM(total) total ");
		wrapper.groupBy("colleague");
		wrapper.orderBy("total", false);
		Page<ShowPoorForecast> page = new Page<>(1, 10);
		List<Map<String, Object>> result = showPoorforecastMapper.selectMapsPage(page, wrapper);
		return result;
	}
}
