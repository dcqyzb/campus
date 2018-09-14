package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorGrade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface IShowNetbehaviorgradeService extends IService<ShowNetBehaviorGrade> {


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
    List<ShowNetBehaviorGrade> getGradeByTerms(String school, String schoolYear, String term);

}
