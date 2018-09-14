package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.network.dao.StunetbehaviorwordsMapper;
import com.mit.campus.rest.modular.network.model.StuNetBehaviorWords;
import com.mit.campus.rest.modular.network.service.IStuNetbehaviorWordsService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@Service
public class StuNetBehaviorWordsServiceServiceImpl extends ServiceImpl<StunetbehaviorwordsMapper, StuNetBehaviorWords> implements IStuNetbehaviorWordsService {
    @Override
    public Page getStuNetWords(String studentName, String collegeName, String major, String startDate, String endDate,
                               int pageNum, int pageSize) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        EntityWrapper<StuNetBehaviorWords> wrapper = new EntityWrapper<>();
        wrapper.orderBy("date", false);
        List<StuNetBehaviorWords> list = null;
        Page<StuNetBehaviorWords> page = new Page();
        boolean export = false;
        if (pageSize > 0) {
            page.setCurrent(pageNum);
            page.setSize(pageSize);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        //时间查询条件
        if (startDate != null && startDate.length() == 7) {
            //默认从一个月第一天开始
            startDate = startDate + "-01";
        } else {
            startDate = " ";
        }
        if (endDate != null && endDate.length() == 7) {
            //默认从一个月第一天开始
            endDate = endDate + "-01";
        } else {
            //结束时间不可为空，所以设置为今天的日期
            endDate = format.format(new Date());
        }
        //按时间区间查询的条件语句
        wrapper.between("date", startDate, endDate);
        //姓名查询条件
        if (studentName != null && studentName.length() > 0) {
            wrapper.like("studentName", studentName);
        }
        //学院查询条件
        if (collegeName != null && collegeName.length() > 0) {
            wrapper.like("collegeName", collegeName);
        }
        //专业查询条件
        if (major != null && major.length() > 0) {
            wrapper.like("major", major);
        }
        //查询语句
        if (export) {
            //导出数据，查询全部
            //要导出的数据
            list = selectList(wrapper);
        } else {
//            分页查询
            return selectPage(page, wrapper);
        }
        if (list != null && list.size() > 0) {
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 获得排名前面的关键词和次数
     *
     * @return 排名和关键词
     */
    @Override
    public List<Object[]> getKeyWords() {
        EntityWrapper<StuNetBehaviorWords> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("keyWords,COUNT(*) count").groupBy("keyWords")
                .orderBy("COUNT(*)", false);
        //获得排名前面的关键词和次数
        List<Object[]> list=null;
        List<Map<String, Object>> mapList = selectMaps(wrapper);
        if (mapList!=null) {
            list = new ArrayList<>();
            for (Map<String, Object> map : mapList) {
                Set<String> set = map.keySet();
                List<String> ob=new ArrayList<>();
                for (String str:set){
                    ob.add(String.valueOf( map.get(str)));
                }
                list.add(ob.toArray());
            }
        }
        if (list.size() > 10) {
            return list.subList(0, 10);
        } else {
            return list;
        }
    }
}
