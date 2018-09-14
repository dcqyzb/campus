package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 学院用电量
* @author shuyy
* @date 2018年9月7日
 */
@TableName("show_electrictrend")
@Data
public class ShowElectricTrend extends Model<ShowElectricTrend> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 学院
	 */
    private String academy;
    /**
	 * 日期
	 */
    private String date;
    /**
	 * 用电量
	 */
    private Float electricity;
  
    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
