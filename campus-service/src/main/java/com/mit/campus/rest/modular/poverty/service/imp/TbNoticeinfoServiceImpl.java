package com.mit.campus.rest.modular.poverty.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.TbNoticeInfoMapper;
import com.mit.campus.rest.modular.poverty.model.TbNoticeInfo;
import com.mit.campus.rest.modular.poverty.service.ITbNoticeInfoService;

/**
 * 
* 通知通告
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class TbNoticeinfoServiceImpl extends ServiceImpl<TbNoticeInfoMapper, TbNoticeInfo> implements ITbNoticeInfoService {
	
	@Autowired
	private TbNoticeInfoMapper tbNoticeInfoMapper;
	
	/**
	 * 
	* 最近5条通知通告
	* @param @param belong
	* @param @return
	* @return List<TbNoticeInfo>
	* @throws
	 */
	@Override
	public List<TbNoticeInfo> findTopNotice(String belong){
		Wrapper<TbNoticeInfo> wrapper = new EntityWrapper<TbNoticeInfo>().eq("noticeBelong", belong).orderBy("noticeTime", false);
		Page<TbNoticeInfo> page = new Page<>(1, 5);
		List<TbNoticeInfo> findTop5Notice = tbNoticeInfoMapper.selectPage(page, wrapper);
		return findTop5Notice;
	}

}
