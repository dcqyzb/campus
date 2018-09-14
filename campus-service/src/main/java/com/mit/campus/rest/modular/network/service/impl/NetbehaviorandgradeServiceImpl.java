package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.network.dao.NetbehaviorandgradeMapper;
import com.mit.campus.rest.modular.network.model.NetBehaviorAndGrade;
import com.mit.campus.rest.modular.network.service.INetbehaviorandgradeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class NetbehaviorandgradeServiceImpl extends ServiceImpl<NetbehaviorandgradeMapper, NetBehaviorAndGrade> implements INetbehaviorandgradeService {
    /**
     * getBehaviorAndGrade
     * 二级页面-学生网络行为和成绩查询
     *
     * @param stuname   学生姓名
     * @param college   学院
     * @param stuClass  专业
     * @param startDate 统计时间-开始时间
     * @param endDate   统计时间-结束时间
     * @param pageNum   当前页
     * @param pageSize  每页记录数，<= 0时导出
     * @return 分页对象
     * @author lw
     */
    @Override
    public Page getBehaviorAndGrade(String stuname, String college, String stuClass, String startDate, String endDate,
                                    int pageNum, int pageSize) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<NetBehaviorAndGrade> wrapper = new EntityWrapper<>();
        wrapper.orderBy("date", false);
        List<NetBehaviorAndGrade> list = null;
        Page<NetBehaviorAndGrade> page = new Page();
        boolean export = false;
        if (pageSize > 0) {
            page.setCurrent(pageNum);
            page.setSize(pageSize);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        //时间查询条件
        if (startDate != null && startDate.length() == 10) {
            //默认从一个月第一天开始
            startDate = startDate + "-01";
        } else {
            startDate = " ";
        }
        if (endDate != null && endDate.length() == 10) {
            //默认从一个月第一天开始
            endDate = endDate + "-01";
        } else {
            //结束时间不可为空，所以设置为今天的日期
            endDate = format.format(new Date());
        }
        //按时间区间查询的条件语句
        wrapper.and().between("date", startDate, endDate);
        //姓名查询条件
        if (!StringUtils.isBlank(stuname)) {
            wrapper.like("stuname", stuname);
        }
        //学院查询条件
        if (!StringUtils.isBlank(college)) {
            wrapper.like("college", college);
        }
        //专业查询条件
        if (!StringUtils.isBlank(stuClass)) {
            wrapper.like("stuClass", stuClass);
        }
        //查询语句
        if (export) {
            //导出数据，查询全部
            //要导出的数据
            list = selectList(wrapper);
        } else {
            //分页查询
            return  selectPage(page,wrapper);
        }
        if (list != null && list.size() > 0) {
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 通过UUID获得学生上网行为
     *
     * @param uuid 唯一id
     * @return NetBehaviorAndGrade对象
     */
    @Override
    public NetBehaviorAndGrade findStuBehaviorAndGrade(String uuid) {
        return  selectById(uuid);
    }

    /**
     * 查找学生平均上网时长的top10
     *
     * @return NetBehaviorAndGrade列表
     */
    @Override
    public List<NetBehaviorAndGrade> getTopStuNet() {
        EntityWrapper<NetBehaviorAndGrade> wrapper=new EntityWrapper<>();
        wrapper.groupBy("studentID").orderBy("hour-'h'",false).last("LIMIT 10");
        return selectList(wrapper);
    }
}
