package com.mit.campus.rest.modular.poverty.service;


import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.poverty.model.TbNoticeInfo;

import java.util.List;

/**
 * 
* 通知通告
* @author shuyy
* @date 2018年9月6日
 */
public interface ITbNoticeInfoService extends IService<TbNoticeInfo> {

	/**
	 * 
	* 最近5条通知通告
	* @param @param belong
	* @param @return
	* @return List<TbNoticeInfo>
	* @throws
	 */
	List<TbNoticeInfo> findTopNotice(String belong);
}
