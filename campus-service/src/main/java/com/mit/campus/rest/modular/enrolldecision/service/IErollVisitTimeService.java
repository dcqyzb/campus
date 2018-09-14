package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ErollVisitTime;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 14:28
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IErollVisitTimeService extends IService<ErollVisitTime> {
    /**
     * 二级页面  访问时间统计
     *
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page getVisitTime(String startTime, String endTime, int pageNum, int pageSize);

    /**
     * 二级-访问量最多的前十个月份
     *
     * @return
     */
    List<Map<String, Object>> getTopTime();
}
