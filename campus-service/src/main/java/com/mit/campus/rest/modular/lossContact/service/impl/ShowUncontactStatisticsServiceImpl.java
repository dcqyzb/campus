package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.ShowUncontactStatisticsMapper;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactStatistics;
import com.mit.campus.rest.modular.lossContact.service.IShowUncontactStatisticsService;
import com.mit.campus.rest.modular.lossContact.ui.model.LossSiteStatisticsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:28
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowUncontactStatisticsServiceImpl
        extends ServiceImpl<ShowUncontactStatisticsMapper, ShowUncontactStatistics>
        implements IShowUncontactStatisticsService {
    @Autowired
    private ShowUncontactStatisticsMapper showUncontactStatisticsMapper;

    @Override
    public List<ShowUncontactStatistics> findUncontactStatistics(String time) {
        List<ShowUncontactStatistics> list = showUncontactStatisticsMapper.selectList(
                new EntityWrapper<ShowUncontactStatistics>().eq("yyyyMM", time)
        );
        if (null != list && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<LossSiteStatisticsModel> getSiteLossIncident(String place, String yyyy) {
        if (place != null && place.length() > 0 && yyyy != null && yyyy.length() > 0) {
            List<LossSiteStatisticsModel> list = new ArrayList<LossSiteStatisticsModel>();
            List<ShowUncontactStatistics> ds = showUncontactStatisticsMapper.selectList(
                    new EntityWrapper<ShowUncontactStatistics>().like("yyyyMM", yyyy)
                            .and().like("place", place).orderBy("college, timeslot")
            );
            //对结果进行归类
            if (ds != null && ds.size() > 0) {
                //按学院归类
                Map<String, List<ShowUncontactStatistics>> map = new HashMap<String, List<ShowUncontactStatistics>>();
                for (ShowUncontactStatistics c : ds) {
                    List<ShowUncontactStatistics> valist = null;
                    String college = c.getCollege();
                    if (map.containsKey(college)) {
                        //如果存在该学院，直接累加
                        valist = map.get(college);
                    } else {
                        //否则，新集合
                        valist = new ArrayList<ShowUncontactStatistics>();
                    }
                    valist.add(c);
                    map.put(college, valist);
                }
                for (Map.Entry<String, List<ShowUncontactStatistics>> entry : map.entrySet()) {
                    LossSiteStatisticsModel o = new LossSiteStatisticsModel();
                    String collegev = entry.getKey();
                    List<ShowUncontactStatistics> suList = entry.getValue();
                    int stuTotal = 0;
                    if (collegev != null && collegev.length() > 0 && suList.size() > 0) {
                        String site = null;
                        for (ShowUncontactStatistics s : suList) {
                            stuTotal += s.getNumber();
                            site = s.getPlace();
                        }
                        o.setCollege(collegev);
                        o.setIncTotal(suList.size());
                        o.setSite(site);
                        o.setYear(yyyy);
                        o.setList(suList);
                        o.setStuTotal(stuTotal);
                        list.add(o);
                    }
                }
                return list;
            }
        }
        return null;
    }


}
