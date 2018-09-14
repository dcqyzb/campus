package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.EnrollJobMapper;
import com.mit.campus.rest.modular.enrolldecision.model.EnrollJob;
import com.mit.campus.rest.modular.enrolldecision.service.IEnrollJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 14:05
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class EnrollJobServiceImpl
        extends ServiceImpl<EnrollJobMapper, EnrollJob>
        implements IEnrollJobService {
    @Autowired
    private EnrollJobMapper enrollJobMapper;

    /**
     * @param college   学院
     * @param city      就业城市
     * @param workArea  就业城市所属地区
     * @param startTime 毕业年份
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page getVisitJob(String college, String city, String workArea, String startTime, String endTime, int pageNum, int pageSize) {
        EntityWrapper<EnrollJob> wrapper = new EntityWrapper<>();
        //查询结果
        List<EnrollJob> list = null;
        //查询页
        Page<EnrollJob> page = new Page<>();
        boolean export = false;
        if (pageNum > 0 && pageSize > 0) {
            page.setSize(pageSize);
            page.setCurrent(pageNum);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        //学院条件
        if (college != null && college.length() > 0) {
            wrapper.like("college", college);
        }
        //地区条件
        if (workArea != null && workArea.length() > 0) {
            wrapper.like("workArea", workArea);
        }
        //城市条件
        if (city != null && city.length() > 0) {
            wrapper.like("city", city);
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
        wrapper.orderBy("graduteYear", false);
        wrapper.orderBy("counts", false);
        //按时间区间查询的条件语句
        wrapper.between("graduteYear", startTime, endTime);
        long totalNum = 0;
        if (export) {
            list = enrollJobMapper.selectList(wrapper);
            totalNum = list.size();
            page.setRecords(list);
        } else {
            list = enrollJobMapper.selectPage(page, wrapper);
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 二级页面top10，毕业生在城市就业人数的排行
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getTopJob() {
        //访问量最多的十个城市（全部访问量，不管时间）
        List<Map<String, Object>> list = enrollJobMapper.selectMapsPage(
                new Page<EnrollJob>(0, 10)
                , new EntityWrapper<EnrollJob>().setSqlSelect("city , sum(counts) as total").groupBy("city").orderBy("sum(counts)", false)
        );
        return list;
    }

}
