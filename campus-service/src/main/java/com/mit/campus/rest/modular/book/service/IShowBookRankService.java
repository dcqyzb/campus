package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowBookRank;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:48
 */
public interface IShowBookRankService {
    /**
     * 获取图书排行榜列表
     * @return 图书排行榜列表
     */
    List<ShowBookRank> findBookRank();
}
