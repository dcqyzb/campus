package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWebWords;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 11:06
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */

public interface IShowEnrollWebWordsService extends IService<ShowEnrollWebWords> {
    /**
     * 招生咨询热词
     *
     * @return
     */
    List<ShowEnrollWebWords> getAllWebWords();
}
