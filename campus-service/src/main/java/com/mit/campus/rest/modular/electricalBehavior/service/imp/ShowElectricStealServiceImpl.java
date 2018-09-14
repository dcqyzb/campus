package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ShowElectricStealMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricSteal;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricStealService;

/**
 * 
* 宿舍窃电嫌疑、违章电器嫌疑
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ShowElectricStealServiceImpl extends ServiceImpl<ShowElectricStealMapper, ShowElectricSteal> implements IShowElectricStealService {

	@Autowired
	private ShowElectricStealMapper showElectricStealMapper;
	
	/**
	 * 
	* 获取某栋当前月的上一个月窃电嫌疑度高的前4，近两年用电变化
	* @param @param position
	* @param @return
	* @return List<List<ShowElectricSteal>>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<List<ShowElectricSteal>> findStealElectricTop4(String position) {
		List<ShowElectricSteal> list = null;
		List<ShowElectricSteal> one_room = null;
		List<List<ShowElectricSteal>> total = new ArrayList<List<ShowElectricSteal>>();
		//当前月的上一个月
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM");
		String last_month = sdf.format(c.getTime()); 
		//当前月的上一个月的嫌疑度前4
		EntityWrapper<ShowElectricSteal> wrapper = new EntityWrapper<>();
		wrapper.eq("date", "2018-02").and("dormitoryID like {0}", position+"-"+"%").orderBy("stealElec", false);
		//找到前4的宿舍
		list = showElectricStealMapper.selectList(wrapper);
		//前4的宿舍近两年的数据
		for(int i = 0;i<4;i++){
			one_room = new ArrayList<ShowElectricSteal>();
			EntityWrapper<ShowElectricSteal> wrapper2 = new EntityWrapper<>();
			wrapper2.eq("dormitoryID", list.get(i).getDormitoryID()).le("date", "2018-02").orderBy("date", false);
			one_room = showElectricStealMapper.selectList(wrapper2);
			total.add(one_room);
		}
		return total;
	}
}
