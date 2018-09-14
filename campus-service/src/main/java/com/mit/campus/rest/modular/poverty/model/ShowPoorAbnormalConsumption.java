package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
 * 贫困生异常消费
* @author shuyy
* @date 2018年9月6日
 */
@TableName("show_poorabnormalconsumption")
@Data
public class ShowPoorAbnormalConsumption extends Model<ShowPoorAbnormalConsumption> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *学院专业班级
	 *经管学院经济管理（1）班
	 */
    private String college;
    /**
	 * 月同比涨幅
	 */
    private String monthRate;
    /**
     * 学号
     */
    private String studentID;
    /**
	 *学生姓名
	 */
    private String stuname;
    /**
	 *异常消费类型
	 *如：水电、餐饮
	 */
    private String type;

	/**
	 *异常消费额
	 */
    private Integer value;

	
	/**
	 * 年同比涨幅
	 */
    private String yearRate;
    /**
	 *年月日
	 *如 2017-12-12
	 *说明：年月日，以"-"分隔
	 */
    private Date yyyymmdd;
    /**
	 * 月同比涨幅,百分比
	 */
    private String monthPercent;
    /**
	 * 年同比涨幅,百分比
	 */
    private String yearPercent;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
