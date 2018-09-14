package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorGrade;
import com.mit.campus.rest.modular.network.dao.ShowNetbehaviorgradeMapper;
import com.mit.campus.rest.modular.network.service.IShowNetbehaviorgradeService;
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
public class ShowNetbehaviorgradeServiceImpl extends ServiceImpl<ShowNetbehaviorgradeMapper, ShowNetBehaviorGrade> implements IShowNetbehaviorgradeService {
    /**
     * getGradeByTerms
     * 获取某学期学生平均成绩与平均上网时长的关系
     *
     * @param school     学院
     * @param schoolYear 学年
     * @param term       学期
     * @return ShowNetBehaviorGrade列表
     * @author lw
     * @date：2018-9-7
     */
	@Override
	public List<ShowNetBehaviorGrade> getGradeByTerms(String school, String schoolYear, String term) {
		//防止前台传参不规范的情况，只取代表年的数字部分
		if(schoolYear.length() > 4){
			String regEx = "[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(schoolYear.trim());
            //只取年份的数字
			schoolYear = m.replaceAll("").trim();
		}
		//学期只取“上”或“下”
		if(term.length() >1){
			if(term.contains("上")){
				term = "上";
			}else if(term.contains("下")){
				term = "下";
			}
		}
        EntityWrapper<ShowNetBehaviorGrade> wrapper=new EntityWrapper<>();
        wrapper.where("school like {0}",school.trim()+"%").and().like("term",schoolYear+"%"+term);
		List<ShowNetBehaviorGrade> list = selectList(wrapper);
		if(list != null && list.size() >0){
			return list;
		}
		return null;

	}

}
