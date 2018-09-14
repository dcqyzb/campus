package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 二级页面-学生历史助学金、奖学金详情
* @author shuyy
* @date 2018年9月6日
 */
@TableName("tb_poorsturecords")
@Data
public class PoorStuRecords extends Model<PoorStuRecords> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**评定年份或贷款年份*/
    private String poorYear;
    /**入学年份*/
    private String schoolYear;
    /**学生姓名*/
    private String stuName;
    /**学号*/
    private String studentID;
    /*资助金额，具体金额*/
    private String surAmount;
    /**资助名，比如 “学校奖学金”，“某某校友奖学金”，“国家助学金”，“助学贷款”*/
    private String surName;
    /**评定等级，奖、助学金，一等奖，二等奖*/
    private String surRank;
    /**资助类型，助学金（aid）还是奖学金（scholarship），或贷款（loan）*/
    private String surType;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
