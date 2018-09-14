package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowReaderTypeRate;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:40
 */
public interface IShowReaderTypeRateService {
    /**
     * 查找读者分类占比，懒惰读者、活跃读者、一般
     * @param year 年份
     * @return 读者分类占比，懒惰读者、活跃读者、一般
     */
    List<ShowReaderTypeRate> findReaderTypeRate(String year);
}
