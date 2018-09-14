package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ShowElectricDormRankMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricDormRank;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricDormRankService;

/**
 * 
* 宿舍用电节能排行表
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ShowElectricDormRankServiceImpl extends ServiceImpl<ShowElectricDormRankMapper, ShowElectricDormRank> implements IShowElectricDormRankService {

	@Autowired
	private ShowElectricDormRankMapper showElectricDormRankMapper;
	
	/**
	 * 
	* 绿色宿舍排行榜
	* @param @return
	* @return List<ShowElectricDormRank>
	* @throws
	* @author shuyy
	 */
	@Override
	public List<ShowElectricDormRank> findDormitoryRank() {
		List<ShowElectricDormRank> list = null;
		EntityWrapper<ShowElectricDormRank> wrapper = new EntityWrapper<>();
		wrapper.orderBy("electricity");
		list = showElectricDormRankMapper.selectList(wrapper);
		return list;
	}
}
