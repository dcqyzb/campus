package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricDormRank;

/**
 * 宿舍用电节能排行表
* @author shuyy
* @date 2018年9月7日
 */
public interface IShowElectricDormRankService extends IService<ShowElectricDormRank> {
	/**
	 * 
	* 绿色宿舍排行榜
	* @param @return
	* @return List<ShowElectricDormRank>
	* @throws
	* @author shuyy
	 */
	List<ShowElectricDormRank> findDormitoryRank();

}
