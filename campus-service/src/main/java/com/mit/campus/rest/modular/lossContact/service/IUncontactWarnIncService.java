package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.UncontactWarnInc;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:20
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IUncontactWarnIncService extends IService<UncontactWarnInc> {

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
     * @Description: 二级页面-条件查询失联预警事件
     * @Return:
     * @Author: Mr.Deng
     */
    Page getLossWarnIncidents(String stuClass, String stuName, String dealStatus,
                              String startTime, String endTime, int pageNum, int pageSize);

    /**
     * getTopCollegeWarn
     *
     * @Description: top10-预警最多的十个学院
     * @Return:
     * @Author: Mr.Deng
     */
    List<Map<String, Object>> getTopCollegeWarn();
}
