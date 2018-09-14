package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ElectricCollegeDormStealMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormSteal;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeDormStealService;

/**
 * 
* 二级页面-学院窃电宿舍，某月某学院发现窃电的宿舍信息
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ElectricCollegeDormStealServiceImpl  extends ServiceImpl<ElectricCollegeDormStealMapper, ElectricCollegeDormSteal>
        implements IElectricCollegeDormStealService {
	
    @Autowired
    private ElectricCollegeDormStealMapper electricCollegeDormStealMapper;

    
    /**
     * 
    * 二级页面-按宿舍时间查询该宿舍历史窃电详情
    * @param @param dormId
    * @param @param date
    * @param @return
    * @return List<ElectricCollegeDormSteal>
    * @throws
    * @author shuyy
     */
	@Override
	public List<ElectricCollegeDormSteal> getDormStealHistory(String dormId, String date){
		EntityWrapper<ElectricCollegeDormSteal> wrapper = new EntityWrapper<>();
		wrapper.eq("dormID", dormId).lt("date", date).orderBy("date", false);
		List<ElectricCollegeDormSteal> list = electricCollegeDormStealMapper.selectList(wrapper);
		return list;
	}
}
