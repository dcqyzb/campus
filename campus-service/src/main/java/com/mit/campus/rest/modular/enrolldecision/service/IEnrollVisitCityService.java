package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.EnrollVisitCity;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 11:41
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IEnrollVisitCityService extends IService<EnrollVisitCity> {

    /**
     * 二级页面-访问来源数据(分页)查询
     *
     * @param province
     * @param city
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page getVisitCityAndSource(String province, String city, String startTime, String endTime, int pageNum, int pageSize);

    /**
     * 二级页面top10， 访问来源和城市排行
     *
     * @return
     */
    List<Map<String, Object>> getTopVisitCity();
}
