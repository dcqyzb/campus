package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ElectricCollegeDormHighPowerMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormHighPower;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeDormHighPowerService;

/**
 * 
* 二级页面-学院违章宿舍，某月某学院发现违章的宿舍信息
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ElectricCollegeDormHighPowerServiceImpl
        extends ServiceImpl<ElectricCollegeDormHighPowerMapper, ElectricCollegeDormHighPower>
        implements IElectricCollegeDormHighPowerService {
    @Autowired
    private ElectricCollegeDormHighPowerMapper electricCollegeDormHighPowerMapper;
    
    /**
     * 
    * 二级页面-按宿舍时间查询该宿舍历史违章详情
    * @param @param dormId
    * @param @param date
    * @param @return
    * @return List<ElectricCollegeDormHighPower>
    * @throws
    * @author shuyy
     */
	@Override
	public List<ElectricCollegeDormHighPower> getDormHighPowerHistory(String dormId, String date){
		EntityWrapper<ElectricCollegeDormHighPower> wrapper = new EntityWrapper<>();
		wrapper.eq("dormID", dormId).lt("date", date).orderBy("date", false);
		try{
			List<ElectricCollegeDormHighPower> list = electricCollegeDormHighPowerMapper.selectList(wrapper);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


}
