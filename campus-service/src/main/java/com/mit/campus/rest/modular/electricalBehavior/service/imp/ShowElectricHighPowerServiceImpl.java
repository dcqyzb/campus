package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ShowElectricHighPowerMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricHighPower;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricHighPowerService;

/**
 * 
* 宿舍窃电嫌疑、违章电器嫌疑
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ShowElectricHighPowerServiceImpl extends ServiceImpl<ShowElectricHighPowerMapper, ShowElectricHighPower> implements IShowElectricHighPowerService {

	@Autowired
	private ShowElectricHighPowerMapper showElectricHighPowerMapper;
	
	/**
	 * 
	* 按年查找所有楼栋违章电器使用嫌疑
	* @param @param date
	* @param @return
	* @return List<ShowElectricHighPower>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<ShowElectricHighPower> findDormHighPowerByYear(String date){
		List<ShowElectricHighPower> list = null;
		EntityWrapper<ShowElectricHighPower> wrapper = new EntityWrapper<>();
		wrapper.and("date like {0}", date+"%").orderBy("dormitoryID");
		//获取该月所有宿舍的嫌疑度
		list = showElectricHighPowerMapper.selectList(wrapper);
		return list;
	}
	
	/**
	 * 
	* 按月查找所有楼栋违章电器使用嫌疑度
	* @param @param date
	* @param @return
	* @return List<ShowElectricHighPower>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<ShowElectricHighPower> findDormHighPower(String date) {
		List<ShowElectricHighPower> list = null;
		EntityWrapper<ShowElectricHighPower> wrapper = new EntityWrapper<>();
		wrapper.eq("date", date).orderBy("dormitoryID");
		//获取该月所有宿舍的嫌疑度
		list = showElectricHighPowerMapper.selectList(wrapper);
		return list;
	}
	
}
