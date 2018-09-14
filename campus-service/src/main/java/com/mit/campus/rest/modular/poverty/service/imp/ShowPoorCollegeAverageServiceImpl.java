package com.mit.campus.rest.modular.poverty.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorCollegeAverageMapper;
import com.mit.campus.rest.modular.poverty.model.ShowPoorCollegeAverage;
import com.mit.campus.rest.modular.poverty.service.IShowPoorCollegeAverageService;

/**
 * 
* 学院平均消费
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class ShowPoorCollegeAverageServiceImpl extends ServiceImpl<ShowPoorCollegeAverageMapper, ShowPoorCollegeAverage> implements IShowPoorCollegeAverageService {
	
	@Autowired
	private ShowPoorCollegeAverageMapper showPoorCollegeaverageMapper;

	/**
	* 
	* 各学院指定年份的消费总和统计， 按年统计
	* @param @param year
	* @param @return
	* @return List<Integer>
	* @throws
	 */
	@Override
	public List<Integer> findCollegeAverAge(String year) {
		if(StringUtils.isBlank(year)){
			return null;
		}
		/*获取该年的全部数据*/
		List<ShowPoorCollegeAverage> list = showPoorCollegeaverageMapper.selectList(
				new EntityWrapper<ShowPoorCollegeAverage>().like("yyyymm", year)
		);
		if(!list.isEmpty()){
			int size = list.size();
			// map以数字（学院的顺序）为key，以该学院该年所有消费记录总和为value
			Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
			for(int i=0; i<size; i++){
				// 某月的各学院消费记录
				ShowPoorCollegeAverage monthAve = list.get(i);	
				// 各学院顺序固定，消费以','隔开
				String[] a = monthAve.getConsvalues().split(",");	
				// 对每一个学院的消费
				for(int j=0, len=a.length; j<len; j++){
					// 不包含该学院的消费记录和，直接放入map	
					if(!map.containsKey(j)){	
						map.put(j, Integer.parseInt(a[j]));
					}else{	
						//已经存在该学院的消费和，则将该月的消费记录与之前的和相加
						map.put(j, map.get(j)+Integer.parseInt(a[j]));
					}
				}
			}
			//返回值，固定学院顺序的总消费
			List<Integer> all = new ArrayList<Integer>();
			Set<Integer> set = map.keySet();
			Iterator<Integer> ite = set.iterator();
			while(ite.hasNext()){
				all.add(map.get(ite.next()));
			}
			return all;
		}
		return null;
	}
	
	/**
	 * 
	* 
	* 各学院指定年份的消费总和统计， 按月统计
	* @param @param year
	* @param @param month
	* @param @return
	* @return ShowPoorCollegeAverage
	* @throws
	 */
	@Override
	public ShowPoorCollegeAverage findCollegeAverage(String year, String month) {
		if(StringUtils.isEmpty(year) || StringUtils.isEmpty(month)){
			return null;
		}
		month = (Integer.parseInt(month) > 9) ? month : (month.indexOf("0") != -1) ? month : "0"+month;
		List<ShowPoorCollegeAverage> list = showPoorCollegeaverageMapper.selectList(
				new EntityWrapper<ShowPoorCollegeAverage>().eq("yyyymm", year+"-"+month)
		);
		if(!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	
}
