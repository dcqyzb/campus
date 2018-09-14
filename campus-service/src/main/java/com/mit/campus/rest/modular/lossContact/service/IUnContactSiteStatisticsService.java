package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.UnContactSiteStatistics;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/6 8:47
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IUnContactSiteStatisticsService extends IService<UnContactSiteStatistics> {

    /**
     * getSiteStatistics
     *
     * @param siteName  地点名
     * @param startTime 时间区间
     * @param endTime
     * @param pageNum   第几页
     * @param pageSize  每页显示数量
     * @Description: 查询失联地点的统计信息
     * @Return:
     * @Author: Mr.Deng
     */

    Page getSiteStatistics(String siteName, String startTime, String endTime, int pageNum, int pageSize);

    /**
     * getTopSite
     *
     * @param
     * @Description: top10-失联最多的十个地方
     * @Return:
     * @Author: Mr.Deng
     */

    List<Map<String, Object>> getTopSite();

}
