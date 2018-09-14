package com.mit.campus.rest.modular.lossContact.ui.model;

import com.mit.campus.rest.modular.lossContact.model.ShowUncontactStatistics;
import lombok.Data;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/6 10:20
 * @Author: Mr.Deng
 * @Description: 失联地点一年内失联事件统计结果封装类，具体到学院、学生名，
 * @company mitesofor
 */
@Data
public class LossSiteStatisticsModel {

    /**
     * 学院名
     */
    private String college;
    /**
     * 失联事件总数
     */
    private int incTotal;
    /**
     * 失联总人数
     */
    private int stuTotal;
    /**
     * 统计年份
     */
    private String year;
    /**
     * 失联地
     */
    private String site;
    /**
     * 每起事件具体信息
     */
    private List<ShowUncontactStatistics> list;

}
