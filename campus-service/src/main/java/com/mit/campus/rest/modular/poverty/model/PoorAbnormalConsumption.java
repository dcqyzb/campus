package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 异常消费信息表
* @author shuyy
* @date 2018年9月6日
 */
@Data
@TableName("tb_poorabnormalconsumption")
public class PoorAbnormalConsumption extends Model<PoorAbnormalConsumption> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *餐饮消费金额
	 *
	 */
    private Integer catering;
    /**
	 *学院专业班级
	 *经管学院经济管理（1）班
	 */
    private String college;
    /**
	 *娱乐消费金额
	 */
    private Integer entertainment;
    /**
	 *水电消费金额
	 */
    private Integer hydropower;
    private String monthPercent;
    private String monthRate;
    /**
	 *其它消费金额
	 */
    private Integer otherconsumption;
    /**
	 * 学号
	 */
    private String studentID;
    /**
	 *学生姓名
	 */
    private String stuname;
    /**
	 *异常总消费额
	 *value
	 */
    private Integer value;
    private String yearPercent;
    private String yearRate;
    /**
	 *年月日
	 *如 2017-12-12
	 *说明：年月日，以"-"分隔
	 */
    private Date yyyymmdd;
    private String stuclass;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
