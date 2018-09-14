package com.mit.campus.rest.modular.poverty.service.imp;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorAreaMapper;
import com.mit.campus.rest.modular.poverty.model.ShowPoorArea;
import com.mit.campus.rest.modular.poverty.service.IShowPoorAreaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
* 贫困区域
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class ShowPoorAreaServiceImpl extends ServiceImpl<ShowPoorAreaMapper, ShowPoorArea> implements IShowPoorAreaService {
	
	@Autowired
	private ShowPoorAreaMapper showPoorAreaMapper;
	
	/**
	 * 
	* 获取贫困区域
	* @param @param year 指定年份
	* @param @param top10 是否是前十名
	* @param @return
	* @return List<ShowPoorArea>
	* @throws
	 */
	public List<ShowPoorArea> findPoorArea(String year, boolean top10) {
		if(StringUtils.isBlank(year)){
			return null;
		}
		List<ShowPoorArea> list = showPoorAreaMapper.selectPage(
				new Page<ShowPoorArea>(1, 10),
				new EntityWrapper<ShowPoorArea>().eq("year", year).orderBy("total", false)
		);
		try {
			Wrapper<ShowPoorArea> wrapper = new EntityWrapper<ShowPoorArea>().eq("year", year).orderBy("total", false);
			if(top10){
				list = showPoorAreaMapper.selectPage(
						new Page<ShowPoorArea>(1, 10),
						wrapper
				);
			}else{
				list = showPoorAreaMapper.selectList(wrapper);
			}
			if(!list.isEmpty()){
				return list;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
}
