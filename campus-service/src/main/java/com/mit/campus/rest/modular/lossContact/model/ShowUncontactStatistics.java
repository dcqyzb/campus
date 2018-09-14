package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 失联学生统计
 * @Author: Mr.Deng
 */
@TableName("show_uncontactstatistics")
@Data
public class ShowUncontactStatistics extends Model<ShowUncontactStatistics> {

    private static final long serialVersionUID = -3831683261832618174L;

    private String uuid;
    /**
     * 失联年月
     * 2017-02
     */
    private String yyyyMM;
    /**
     * 日期
     * 2/15-2/22
     */
    private String dd;
    /**
     * 失联时长
     */
    private String timeslot;
    /**
     * 学院
     */
    private String college;
    /**
     * 失联地点
     */
    private String place;
    /**
     * 失联人数
     */
    private int number;
    /**
     * 失联名单
     */
    private String names;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
