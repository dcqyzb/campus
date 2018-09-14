package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
*  宿舍窃电嫌疑、违章电器嫌疑
* @author shuyy
* @date 2018年9月7日
 */
@TableName("show_electricsteal")
@Data
public class ShowElectricSteal extends Model<ShowElectricSteal> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 日期
	 */
    private String date;
    /**
	 * 宿舍号 2-0302
	 */
    private String dormitoryID;
    /**
	 * 用电量
	 */
    private Float electricity;
    /**
	 * 窃电嫌疑
	 */
    private Float stealElec;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
