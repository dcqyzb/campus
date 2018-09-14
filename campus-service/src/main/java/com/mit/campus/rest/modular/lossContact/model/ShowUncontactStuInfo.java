package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 失联学生信息表
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("show_uncontactstuinfo")
@Data
public class ShowUncontactStuInfo extends Model<ShowUncontactStuInfo> {

    private static final long serialVersionUID = 3373504491668742731L;

    private String uuid;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 学生学院班级
     */
    private String stuclass;
    /**
     * 预警时间
     * 2018-02-03 14:34:45
     */
    private String warntime;
    /**
     * 处理时间
     * 2018-02-06 11:33:25
     */
    private String processtime;
    /**
     * 处理状态
     */
    private String status;
    /**
     * 学生宿舍
     */
    private String dormitory;
    /**
     * 室友
     */
    private String roommate;
    /**
     * 亲密朋友
     */
    private String friend;
    /**
     * 辅导员
     */
    private String instructor;
    /**
     * 轨迹
     */
    private String trajectory;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
