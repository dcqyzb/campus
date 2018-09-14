package com.mit.campus.rest.modular.poverty.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 贫困生预测
* @author shuyy
* @date 2018年9月6日
 */
@TableName("show_poorforecast")
@Data
public class ShowPoorForecast extends Model<ShowPoorForecast> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *学院名称
	 */
    private String colleague;
    /**
	 *专业名称
	 */
    private String major;
    /**
	 *中等贫困人数
	 */
    private Integer medium;
    /**
	 *一般贫困人数
	 */
    private Integer normal;
    /**
	 *特困人数
	 */
    private Integer poorest;
	/**
	 *学院总共贫困人数
	 */
    private Integer total;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
   
}
