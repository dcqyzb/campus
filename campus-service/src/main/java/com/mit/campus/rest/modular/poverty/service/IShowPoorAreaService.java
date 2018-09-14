package com.mit.campus.rest.modular.poverty.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.ShowPoorArea;

import java.util.List;

/**
 * 
* 学院平均消费
* @author shuyy
* @date 2018年9月6日
 */
public interface IShowPoorAreaService extends IService<ShowPoorArea> {

	/**
	 * 
	* 获取贫困区域
	* @param @param year 指定年份
	* @param @param top10 是否是前十名
	* @param @return
	* @return List<ShowPoorArea>
	* @throws
	 */
	List<ShowPoorArea> findPoorArea(String year,boolean top10);
}
