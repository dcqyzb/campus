package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWebCity;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 15:24
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowEnrollWebCityService extends IService<ShowEnrollWebCity> {
    /**
     * 访问情况分析图
     *
     * @param month
     * @return
     */
    List<ShowEnrollWebCity> getAllWebCity(String month);
}
