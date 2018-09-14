package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.UncontactWarnIncMapper;
import com.mit.campus.rest.modular.lossContact.model.UncontactWarnInc;
import com.mit.campus.rest.modular.lossContact.service.IUncontactWarnIncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:39
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class UncontactWarnIncServiceImpl
        extends ServiceImpl<UncontactWarnIncMapper, UncontactWarnInc>
        implements IUncontactWarnIncService {

    @Autowired
    private UncontactWarnIncMapper uncontactWarnIncMapper;

    /**
     * getLossWarnIncidents
     *
     * @param stuClass   院系
     * @param stuName    学生姓名
     * @param dealStatus 状态
     * @param startTime  时间区间
     * @param endTime
     * @param pageNum    第几页
     * @param pageSize   每页条数
     * @return
     * @描述：二级页面-条件查询失联预警事件
     * @作者： edgwong
     * @日期：2018年4月10日
     */
    @Override
    public Page getLossWarnIncidents(String stuClass, String stuName, String dealStatus,
                                     String startTime, String endTime, int pageNum, int pageSize) {
        List<UncontactWarnInc> list = null;
        EntityWrapper<UncontactWarnInc> wrapper = new EntityWrapper<>();
        Page<UncontactWarnInc> page = new Page<>();
        boolean export = false;
        if (pageNum > 0 && pageSize > 0) {
            page.setSize(pageSize);
            page.setCurrent(pageNum);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }

        if (stuClass != null && stuClass.length() > 0) {
            wrapper.like("stuClass", stuClass);
        }
        if (stuName != null && stuName.length() > 0) {
            wrapper.like("stuName", stuName);
        }
        if (dealStatus != null && dealStatus.length() > 0) {
            wrapper.like("status", dealStatus);
        }
        if (startTime == null || startTime.length() == 0) {
            startTime = " ";
        }
        if (endTime == null || endTime.length() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            endTime = format.format(new Date());
        }
        wrapper.between("createTime", startTime, endTime);
        wrapper.orderBy("stuClass,teaName,createTime");
        long totalNum = 0;
        if (export) {
            //导出数据，查询全部
            list = uncontactWarnIncMapper.selectList(wrapper);
            totalNum = list.size();
            page.setRecords(list);
        } else {
            list = uncontactWarnIncMapper.selectPage(page, wrapper);
            page.setRecords(list);
        }
        return page;
    }

    @Override
    public List<Map<String, Object>> getTopCollegeWarn() {
        //预警最多的十个学院
        List<Map<String, Object>> list = uncontactWarnIncMapper.selectMapsPage(
                new Page<UncontactWarnInc>(1, 10),
                new EntityWrapper<UncontactWarnInc>().setSqlSelect("count(*) as total,college").groupBy("college").orderBy("count(*)", false)
        );
        if (!list.isEmpty()) {
            return list;
        }
        return null;
    }
}
