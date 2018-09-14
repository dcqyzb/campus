package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowUpsetBook;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:48
 */
public interface IShowUpsetBookService{

    /**
     * 获取冷门图书原因分析
     * @return 原因列表
     */
    List<ShowUpsetBook> findUpsetBook(String year);
}
