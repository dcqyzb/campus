package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollBirthPlace;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:53
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowEnrollBirthPlaceService extends IService<ShowEnrollBirthPlace> {

    /**
     * getAllBirthPlace
     *
     * @param year    毕业年份
     * @param college 学院名
     * @Description: 生源地分布图
     * @Return:
     * @Author: Mr.Deng
     */

    List<ShowEnrollBirthPlace> getAllBirthPlace(String year, String college);

    /**
     * 获取某一生源地近3年来的学生人数
     *
     * @param year
     * @param college
     * @param city
     * @return
     */
    ShowEnrollBirthPlace getCityAmounts(String year, String college, String city);
}
