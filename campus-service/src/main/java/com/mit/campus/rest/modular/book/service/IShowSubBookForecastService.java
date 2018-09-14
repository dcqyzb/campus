package com.mit.campus.rest.modular.book.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.book.model.ShowSubBookForecast;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:56
 */
public interface IShowSubBookForecastService {

    /**
     * findBookForecastByCondition
     * 按条件查询图书需求分析信息
     * pageSize<0查询所有
     * @param bookName 书名
     * @param typeName 书类名
     * @param pageNum 当前页
     * @param pageSize 每页显示几个
     * @return 图书需求分析信息的page对象
     */
    Page<ShowSubBookForecast> findBookForecastByCondition(String bookName, String typeName, int pageNum, int pageSize);

}
