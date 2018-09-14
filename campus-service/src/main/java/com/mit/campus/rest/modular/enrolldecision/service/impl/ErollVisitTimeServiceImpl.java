package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ErollVisitTimeMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ErollVisitTime;
import com.mit.campus.rest.modular.enrolldecision.service.IErollVisitTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 14:29
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ErollVisitTimeServiceImpl
        extends ServiceImpl<ErollVisitTimeMapper, ErollVisitTime>
        implements IErollVisitTimeService {
    @Autowired
    private ErollVisitTimeMapper erollVisitTimeMapper;

    /**
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page getVisitTime(String startTime, String endTime, int pageNum, int pageSize) {
        EntityWrapper<ErollVisitTime> wrapper = new EntityWrapper<>();
        //查询结果
        List<ErollVisitTime> list = null;
        //查询页
        Page<ErollVisitTime> page = new Page<>();
        boolean export = false;
        if (pageNum > 0 && pageSize > 0) {
            page.setSize(pageSize);
            page.setCurrent(pageNum);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        //统计时间区间条件，设置默认值
        if (startTime == null || startTime.length() == 0) {
            //开始时间可为空
            startTime = " ";
        }
        if (endTime == null || endTime.length() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            //结束时间不可为空，所以设置为今天的日期
            endTime = format.format(new Date());
        }
        //排序规则
        wrapper.orderBy("count", false);
        //按时间区间查询的条件语句
        wrapper.between("year", startTime, endTime);
        long totalNum = 0;
        if (export) {
            list = erollVisitTimeMapper.selectList(wrapper);
            totalNum = list.size();
            page.setRecords(list);
        } else {
            list = erollVisitTimeMapper.selectPage(page, wrapper);
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 二级-访问量最多的前十个月份
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getTopTime() {
        //访问量最多的十个城市（全部访问量，不管时间）
        List<Map<String, Object>> list = erollVisitTimeMapper.selectMapsPage(
                new Page<ErollVisitTime>(0, 10),
                new EntityWrapper<ErollVisitTime>().setSqlSelect("year,count").orderBy("count", false)
        );
        return list;
    }

}
