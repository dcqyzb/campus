package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowReaderBorrowRate;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:39
 */
public interface IShowReaderBorrowRateService {

    /**
     * 查找各个学院的读者借阅率
     * @param year 年份
     * @return 读者借阅率列表
     */
    List<ShowReaderBorrowRate> findReaderBorrowRate(String year);

}
