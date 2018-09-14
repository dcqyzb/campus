package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 二级页面，消费对比源数据表
* @author shuyy
* @date 2018年9月6日
 */
@TableName("tb_poorconsumpcomare")
@Data
public class PoorConsumpComare extends Model<PoorConsumpComare> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**学院名*/
    private String college;
    /**专业名*/
    private String major;
    /**消费金额*/
    private String moneyAmount;
    /**月份，形如2017-01*/
    private String month;
    /**学生姓名*/
    private String stuName;
    /**学生性别*/
    private String stuSex;
    /**学号*/
    private String studentID;
    /**贫困等级*/
    private String poorRank;
    /**评定前后3个月涨幅（可正可负）*/
    private String increase;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
