package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.UncontactReasonStatisticsMapper;
import com.mit.campus.rest.modular.lossContact.model.UncontactReasonStatistics;
import com.mit.campus.rest.modular.lossContact.model.UncontactWarnInc;
import com.mit.campus.rest.modular.lossContact.service.IUncontactReasonStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:34
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class UncontactReasonStatisticsServiceimpl
        extends ServiceImpl<UncontactReasonStatisticsMapper, UncontactReasonStatistics>
        implements IUncontactReasonStatisticsService {
    @Autowired
    private UncontactReasonStatisticsMapper uncontactReasonStatisticsMapper;

    @Override
    public Page getReasonStatistics(
            String stuName, String stuClass, String startTime, String endTime,
            String lossReason, int pageNum, int pageSize) {
        EntityWrapper<UncontactReasonStatistics> wapper = new EntityWrapper<>();
        List<UncontactReasonStatistics> list = null;
        Page<UncontactReasonStatistics> page = new Page<>();
        boolean export = false;
        if (pageNum > 0 && pageSize > 0) {
            page.setSize(pageSize);
            page.setCurrent(pageNum);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        if (stuName != null && stuName.length() > 0) {
            wapper.like("stuName", stuName);
        }
        if (stuClass != null && stuClass.length() > 0) {
            wapper.like("stuClass", stuClass);
        }
        if (startTime == null || startTime.length() == 0) {
            startTime = " ";
        }
        if (endTime == null || endTime.length() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            endTime = format.format(new Date());
        }
        wapper.and().between("schoolYear", startTime, endTime);
        wapper.orderBy("stuClass");
        wapper.orderBy("schoolYear", false);
        wapper.orderBy("lossReason");
        if (lossReason != null && lossReason.length() > 0) {
            wapper.like("lossReason", lossReason);
        }
        long totalNum = 0;
        if (export) {
            list = uncontactReasonStatisticsMapper.selectList(wapper);
            totalNum = list.size();
            page.setRecords(list);
        } else {
            list = uncontactReasonStatisticsMapper.selectPage(page, wapper);
            page.setRecords(list);
        }
        return page;
    }

    @Override
    public UncontactReasonStatistics getById(String uuid) {
        if (uuid != null && uuid.length() > 0) {
            List<UncontactReasonStatistics> list = uncontactReasonStatisticsMapper.selectList(
                    new EntityWrapper<UncontactReasonStatistics>().eq("uuid", uuid));
            if (null != list && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTopReason() {
        //失联最多的十个地方
        List<Map<String, Object>> list = uncontactReasonStatisticsMapper.selectMapsPage(
                new Page<UncontactReasonStatistics>(1, 10),
                new EntityWrapper<UncontactReasonStatistics>().setSqlSelect("COUNT(*) as total,lossReason").groupBy("lossReason").orderBy("COUNT(*)", false)
        );
        return list;
    }

}
