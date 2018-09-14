package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.NetBehaviorAndGrade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface INetbehaviorandgradeService extends IService<NetBehaviorAndGrade> {


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
    Page getBehaviorAndGrade(String stuname, String college, String stuClass, String startDate, String endDate,
                             int pageNum, int pageSize);

    /**
     * 通过UUID获得学生上网行为
     *
     * @param uuid 唯一id
     * @return NetBehaviorAndGrade对象
     */
    NetBehaviorAndGrade findStuBehaviorAndGrade(String uuid);

    /**
     * 查找学生平均上网时长的top10
     *
     * @return NetBehaviorAndGrade列表
     */
    List<NetBehaviorAndGrade> getTopStuNet();
}
