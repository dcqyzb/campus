package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowBookTypeRate;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:36
 */
public interface IShowBookTypeRateService {
    /**
     * 根据年份查找图书热门程度占比，热门、冷门、一般
     * @param year 年份
     * @return 图书热门程度占比列表
     */
    List<ShowBookTypeRate> findBookTypeRate(String year);
}
