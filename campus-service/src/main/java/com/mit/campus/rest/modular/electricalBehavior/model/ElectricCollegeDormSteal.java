/**
 * ElectricCollegeDormSteal.java
 *
 * @描述：
 * @作者：zhoutingting
 * @日期： 2018年4月9日 上午9:30:29
 */
package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 二级页面-学院窃电宿舍，某月某学院发现窃电的宿舍信息
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_electriccollegedormsteal")
@Data
public class ElectricCollegeDormSteal extends Model<ElectricCollegeDormSteal> {
    private static final long serialVersionUID = 1L;

    private String uuid;

    /**
	 *宿舍号 1-0201（1栋2楼1室）
	 */
    private String dormID;
    /**
	 *男寝or女寝,"男"，"女"
	 */
    private String type;
    /**
	 *学院名称
	 */
    private String collegeName;
    /**
	 *宿舍人数
	 */
    private String peopleNum;
    /**
	 * 年月
	 */
    private String date;
    /**
	 *月用电量
	 */
    private float electricity;
    /**
	 *窃电量
	 */
    private float stealElec;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
