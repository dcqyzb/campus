package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.EnrollJob;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 12:05
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IEnrollJobService extends IService<EnrollJob> {
    /**
     * 二级页面-就业城市统计
     *
     * @param college   学院
     * @param city      就业城市
     * @param workArea  就业城市所属地区
     * @param startTime 毕业年份
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page getVisitJob(String college, String city, String workArea, String startTime, String endTime, int pageNum, int pageSize);

    /**
     * 二级页面top10，毕业生在城市就业人数的排行
     *
     * @return
     */
    List<Map<String, Object>> getTopJob();
}
