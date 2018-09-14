package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWebTime;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 16:40
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowEnrollWebTimeService extends IService<ShowEnrollWebTime> {
    /**
     * 访问时间分布图
     *
     * @param date
     * @return
     */
    String getAllWebVisit(String date);
}
