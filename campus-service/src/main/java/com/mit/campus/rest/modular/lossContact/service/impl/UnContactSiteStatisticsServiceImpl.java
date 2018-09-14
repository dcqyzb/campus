package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.UnContactSiteStatisticsMapper;
import com.mit.campus.rest.modular.lossContact.model.UnContactSiteStatistics;
import com.mit.campus.rest.modular.lossContact.service.IUnContactSiteStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: campus-parent
 * @Date: 2018/9/6 8:51
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class UnContactSiteStatisticsServiceImpl
        extends ServiceImpl<UnContactSiteStatisticsMapper, UnContactSiteStatistics>
        implements IUnContactSiteStatisticsService {
    @Autowired
    private UnContactSiteStatisticsMapper unContactSiteStatisticsMapper;

    @Override
    public Page getSiteStatistics(String siteName, String startTime, String endTime, int pageNum, int pageSize) {
        EntityWrapper<UnContactSiteStatistics> wrapper = new EntityWrapper<>();
        List<UnContactSiteStatistics> list = null;
        Page<UnContactSiteStatistics> page = new Page<>();
        boolean export = false;
        if (pageNum > 0 && pageSize > 0) {
            page.setSize(pageSize);
            page.setCurrent(pageNum);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        if (siteName != null && siteName.length() > 0) {
            wrapper.like("siteName", siteName);
        }
        if (startTime == null || startTime.length() == 0) {
            startTime = " ";
        }
        if (endTime == null || endTime.length() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            endTime = format.format(new Date());
        }
        wrapper.between("countYear", startTime, endTime);
        wrapper.orderBy("countYear,amount", false);
        long totalNum = 0;
        if (export) {
            list = unContactSiteStatisticsMapper.selectList(wrapper);
            totalNum = list.size();
            page.setRecords(list);
        } else {
            list = unContactSiteStatisticsMapper.selectPage(page, wrapper);
            page.setRecords(list);
        }
        return page;
    }

    @Override
    public  List<Map<String, Object>> getTopSite() {
        //失联最多的十个地方
        List<Map<String, Object>> list = unContactSiteStatisticsMapper.selectMapsPage(
                new Page<UnContactSiteStatistics>(1, 10),
                new EntityWrapper<UnContactSiteStatistics>().setSqlSelect("SUM(amount) as total,siteName").groupBy("siteName").orderBy("SUM(amount)", false)
        );
        return list;
    }
}
