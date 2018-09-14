package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactKeyWords;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 11:58
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowUncontactKeyWordsService extends IService<ShowUncontactKeyWords> {

    /**
     * 获取失联关键词
     *
     * @return
     */
    List<ShowUncontactKeyWords> findUncontactKeywords();
}
