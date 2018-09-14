package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowBookBorrowRate;


import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:33
 */

public interface IShowBookBorrowRateService {
    /**
     * findBookBorrowRate
     * 根据年份查找图书借阅占比，以图书分类为维度
     * @param year 年份
     * @return 图书借阅占比列表
     */
    List<ShowBookBorrowRate> findBookBorrowRate(String year);
}
