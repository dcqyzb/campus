package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.UncontactReasonStatistics;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:18
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IUncontactReasonStatisticsService extends IService<UncontactReasonStatistics> {

    /**
     * @param stuName    学生姓名
     * @param stuClass   学院
     * @param startTime  入学年份(区间)
     * @param endTime
     * @param lossReason 失联原因
     * @param pageNum    第几页
     * @param pageSize   每页条数
     * @Description: 查询二级页面，失联原因分析的数据
     * @Return:
     * @Author: Mr.Deng
     */
    Page getReasonStatistics(String stuName, String stuClass, String startTime, String endTime, String lossReason, int pageNum, int pageSize);

    /**
     * @param uuid
     * @Description: 二级页面查询学生标签
     * @Return:
     * @Author: Mr.Deng
     */
    UncontactReasonStatistics getById(String uuid);

    /**
     * @param
     * @Description: top10-导致失联最多的十个原因
     * @Return:
     * @Author: Mr.Deng
     * @Date:
     */
    List<Map<String, Object>> getTopReason();

}
