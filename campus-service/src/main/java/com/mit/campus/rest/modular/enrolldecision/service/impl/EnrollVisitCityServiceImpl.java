package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.EnrollVisitCityMapper;
import com.mit.campus.rest.modular.enrolldecision.model.EnrollVisitCity;
import com.mit.campus.rest.modular.enrolldecision.service.IEnrollVisitCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 11:42
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class EnrollVisitCityServiceImpl
        extends ServiceImpl<EnrollVisitCityMapper, EnrollVisitCity>
        implements IEnrollVisitCityService {
    @Autowired
    private EnrollVisitCityMapper enrollVisitCityMapper;

    /**
     * @param province
     * @param city
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page getVisitCityAndSource(String province, String city, String startTime, String endTime, int pageNum, int pageSize) {
        EntityWrapper<EnrollVisitCity> wrapper = new EntityWrapper<>();
        //查询结果
        List<EnrollVisitCity> list = null;
        //查询页
        Page<EnrollVisitCity> page = new Page<>();
        boolean export = false;
        if (pageNum > 0 && pageSize > 0) {
            page.setSize(pageSize);
            page.setCurrent(pageNum);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        //省份条件
        if (province != null && province.length() > 0) {
            wrapper.like("province", province);
        }
        //城市条件
        if (city != null && city.length() > 0) {
            wrapper.like("city", city);
        }
        //统计时间区间条件，设置默认值
        if (startTime == null || startTime.length() == 0) {
            startTime = " ";//开始时间可为空
        }
        if (endTime == null || endTime.length() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            endTime = format.format(new Date());//结束时间不可为空，所以设置为今天的日期
        }
        //排序规则
        wrapper.orderBy("month,counts", false);
        //按时间区间查询的条件语句
        wrapper.and().between("month", startTime, endTime);
        long totalNum = 0;
        if (export) {
            list = enrollVisitCityMapper.selectList(wrapper);
            totalNum = list.size();
            page.setRecords(list);
        } else {
            list = enrollVisitCityMapper.selectPage(page, wrapper);
            page.setRecords(list);
        }
        return page;
    }

    @Override
    public List<Map<String, Object>> getTopVisitCity() {
        //访问量最多的十个城市（全部访问量，不管时间）
//        String hql = "SELECT e.city, sum(e.counts) FROM EnrollVisitCity e GROUP BY e.city ORDER BY sum(e.counts) DESC";
        List<Map<String, Object>> list = enrollVisitCityMapper.selectMapsPage(new Page<EnrollVisitCity>(0, 10),
                new EntityWrapper<EnrollVisitCity>().setSqlSelect("city, sum(counts) as total").groupBy("city").orderBy("sum(counts)", false)
        );
        return list;
    }
}
