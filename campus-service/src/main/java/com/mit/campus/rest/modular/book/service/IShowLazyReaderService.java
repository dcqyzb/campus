package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.ShowLazyReader;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:45
 */
public interface IShowLazyReaderService {
    /**
     * 获取懒惰读者的原因分析
     * @return 懒惰读者的原因列表
     */
    List<ShowLazyReader> findLazyReader(String year);
}
