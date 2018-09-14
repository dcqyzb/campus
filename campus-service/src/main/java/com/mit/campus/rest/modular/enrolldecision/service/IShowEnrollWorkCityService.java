package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWorkCity;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 15:20
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */

public interface IShowEnrollWorkCityService extends IService<ShowEnrollWorkCity> {
    /**
     * 就业情况分析图
     *
     * @param year
     * @param college
     * @param area
     * @return
     */
    List<ShowEnrollWorkCity> getAllWorkInfo(String year, String college, String area);
}
