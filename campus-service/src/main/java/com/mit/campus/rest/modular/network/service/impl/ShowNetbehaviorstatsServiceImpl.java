package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorGrade;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorStats;
import com.mit.campus.rest.modular.network.dao.ShowNetbehaviorstatsMapper;
import com.mit.campus.rest.modular.network.service.IShowNetbehaviorstatsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@Service
public class ShowNetbehaviorstatsServiceImpl extends ServiceImpl<ShowNetbehaviorstatsMapper, ShowNetBehaviorStats> implements IShowNetbehaviorstatsService {
    /**
     * getStatsByTerms
     * 获取某学期学生平均上网时长、次数、流量统计数据
     * @author： lw
     * @date：2018-9-7
     * @param school 学院
     * @param schoolYear 学年
     * @param term 学期
     * @return ShowNetBehaviorStats列表
     */
	@Override
	public List<ShowNetBehaviorStats> getStatsByTerms(String school, String schoolYear, String term) {
		if(schoolYear.length() > 4){
			String regEx = "[^0-9]";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(schoolYear.trim());
			schoolYear = matcher.replaceAll("").trim();
			//只取年份的数字
		}
		if(term.length() >1){
			if(term.contains("上")){
				term = "上";
			}else if(term.contains("下")){
				term = "下";
			}
		}
        EntityWrapper<ShowNetBehaviorStats> wrapper=new EntityWrapper<>();
        wrapper.and("school like {0}",school.trim()+"%").and().like("term",schoolYear+"%"+term);
		List<ShowNetBehaviorStats> list = selectList(wrapper);
		if(list != null && list.size() >0){
			return list;
		}
		return null;
	}
}
