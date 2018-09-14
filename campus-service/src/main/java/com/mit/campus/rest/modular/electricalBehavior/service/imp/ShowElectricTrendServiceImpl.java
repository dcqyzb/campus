package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ShowElectricTrendMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricTrend;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricTrendService;

/**
 * 
* 学院用电量
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ShowElectricTrendServiceImpl extends ServiceImpl<ShowElectricTrendMapper, ShowElectricTrend> implements IShowElectricTrendService {
	
	@Autowired
	private ShowElectricTrendMapper showElectricTrendMapper;
	
	/**
	 * 
	* 学院用电
	* @param @param date
	* @param @return
	* @return Map<String,Object>
	* @throws
	* @author shuyy
	 */
	@Override
	public Map<String, Object> findAcademyElectricMap(String date){
		/*
		 * 返回的最终数据格式为：<学院,<月或日,用电量>>
		 */
		if(StringUtils.isBlank(date)){
			return null;
		}
		List<ShowElectricTrend> list = null;
		EntityWrapper<ShowElectricTrend> wrapper = new EntityWrapper<>();
		wrapper.and("date like {0}", date+"%").and("date like '%-%-%'");
		try {
			list = showElectricTrendMapper.selectList(wrapper);
			if(!list.isEmpty()){
				return convertElectricTrendList(list, (date.indexOf("-") == -1));
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 将获取到的用电数据转化为map格式
	 * 返回的最终数据格式为：<学院,<月或日,用电量>>
	 * @param list	已经按年或按月获取到的用电记录数据
	 * @param isYear	是按年还是按月，涉及到是以月还是以天为键的粒度
	 * @return
	 */
	private Map<String, Object> convertElectricTrendList(List<ShowElectricTrend> list, 
			boolean isYear){
		if(list.isEmpty()){
			return null;
		}
		//定义返回数据
		Map<String, Object> returnmap = new HashMap<String, Object>();
		/*
		 * 将列表数据转化为map键值对格式,key为学院名称，value为该学院下的所有用电记录列表
		 */
		Map<String, List<ShowElectricTrend>> map = new HashMap<String, List<ShowElectricTrend>>();
		for(int i=0, len=list.size(); i<len; i++){
			ShowElectricTrend s = list.get(i);
			String college = s.getAcademy();	
			List<ShowElectricTrend> childList = null;
			if(map.containsKey(college)){	
				childList = map.get(college);
			}else{
				childList = new ArrayList<ShowElectricTrend>();
			}
			childList.add(s);
			map.put(college, childList);
		}
		
		/*对每一个消费类型的列表进行处理*/
		Set<String> set = map.keySet();
		Iterator<String> ite = set.iterator();
		while(ite.hasNext()){
			String key = ite.next();
			List<ShowElectricTrend> childList = map.get(key);	
			/*
			 * 定义某学院下，key为日或天，value为用电量
			 * */
			Map<Integer, Float> value = new HashMap<Integer, Float>();
			for(int i=0, le=childList.size(); i<le; i++){
				ShowElectricTrend s = childList.get(i);
				String[] date = s.getDate().split("-");
				//月份或者按天
				int time = isYear ? Integer.parseInt(date[1]) : Integer.parseInt(date[2]);	
				if(value.containsKey(time)){	
					value.put(time, value.get(time)+s.getElectricity());
				}else{
					value.put(time, (float) s.getElectricity());
				}
			}
			returnmap.put(key, value);
		}
		return returnmap;
	}

}
