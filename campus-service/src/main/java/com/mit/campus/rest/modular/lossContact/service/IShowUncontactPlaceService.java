package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactPlace;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 12:00
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowUncontactPlaceService extends IService<ShowUncontactPlace> {

    /**
     * 获取学生失联区域及活跃数据
     *
     * @return
     */
    List<ShowUncontactPlace> findUncontactPlace(String time);
}
