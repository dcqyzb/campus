package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowBookBorrowRelate;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:46
 */
public interface IShowBookBorrowRelateService {


    /**
     * 获取图书借阅关系
     * @return 图书借阅关系
     */
    List<ShowBookBorrowRelate> findBookBorrowRelate();
}
