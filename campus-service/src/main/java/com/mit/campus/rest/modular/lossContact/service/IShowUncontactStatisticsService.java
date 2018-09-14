package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactStatistics;
import com.mit.campus.rest.modular.lossContact.ui.model.LossSiteStatisticsModel;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 12:01
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowUncontactStatisticsService extends IService<ShowUncontactStatistics> {

    /**
     * 获取失联学生统计信息
     *
     * @param time 要统计的日期，格式为'yyyy-MM'
     * @return
     */
    List<ShowUncontactStatistics> findUncontactStatistics(String time);

    /**
     * @param place 失联地点
     * @param yyyy  年份
     * @Description: 查询某一地点在某年内的所有失联人数分布
     * @Return:
     * @Author: Mr.Deng
     */
    List<LossSiteStatisticsModel> getSiteLossIncident(String place, String yyyy);
}
