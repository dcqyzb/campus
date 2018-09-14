package com.mit.campus.rest.modular.network.service;

import com.mit.campus.rest.modular.network.model.ShowNetBehaviorStats;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface IShowNetbehaviorstatsService extends IService<ShowNetBehaviorStats> {

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
	List<ShowNetBehaviorStats> getStatsByTerms(String school, String schoolYear, String term);


}
