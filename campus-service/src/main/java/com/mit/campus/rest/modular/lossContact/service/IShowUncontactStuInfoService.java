package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactStuInfo;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:17
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowUncontactStuInfoService extends IService<ShowUncontactStuInfo> {

    /**
     * 获取失联预警学生
     *
     * @return
     */
    List<ShowUncontactStuInfo> findUncontactStus();

    /**
     * 按年月获取失联预警学生
     *
     * @param time 日期，格式为'yyyy-MM'
     * @return
     */
    List<ShowUncontactStuInfo> findUncontactStus(String time);
}
