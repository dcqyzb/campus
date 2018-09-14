package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.StuNetBehaviorWords;

import java.util.List;

/**
 * <p>
 *  网络行为词汇服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface IStuNetbehaviorWordsService extends IService<StuNetBehaviorWords> {

    /**
     * 返回关键词
     * @return 获取排名前面的排名和关键词最多10条
     */
    List<Object[]> getKeyWords();

    /**
     * getStuNetWords
     * 二级页面-学生沉迷网络原因查询
     *
     * @param studentName 学生姓名
     * @param collegeName 学院
     * @param major       专业
     * @param startDate   监测时间-开始时间
     * @param endDate     监测时间-结束时间
     * @param pageNum     当前页
     * @param pageSize    每页记录数，<= 0时导出
     * @return StuNetBehaviorWords的page对象
     * @author lw
     * @date：2018-9-7
     */
    Page getStuNetWords(String studentName, String collegeName, String major, String startDate, String endDate,
                        int pageNum, int pageSize);
}
