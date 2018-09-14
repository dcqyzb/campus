package com.mit.campus.rest.modular.poverty.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 学院平均消费
* @author shuyy
* @date 2018年9月6日
 */
@TableName("show_poorcollegeaverage")
@Data
public class ShowPoorCollegeAverage extends Model<ShowPoorCollegeAverage> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *院人均消费
	 */
    private String consvalues;
    
    /**
	 *年月
	 */
    private String yyyymm;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
   
}
