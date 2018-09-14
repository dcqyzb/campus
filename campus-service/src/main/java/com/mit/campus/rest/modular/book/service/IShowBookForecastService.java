package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowBookForecast;


import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:41
 */
public interface IShowBookForecastService {
    /**
     * 获取图书需求变化、预测
     * @return 图书需求变化、预测
     */
    List<ShowBookForecast> findBookForecast();
}
