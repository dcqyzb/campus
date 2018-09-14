package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
*  二级页面-学院用电分析
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_electriccollegeelec")
@Data
public class ElectricCollegeElec extends Model<ElectricCollegeElec> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *学院人均用电量
	 */
    private Float avgElec;
    /**
	 *学院名称
	 */
    private String collegeName;
    /**
	 *年月2017-12
	 */
    private String date;
    /**
	 *月用电量
	 */
    private String electricity;
    /**
	 *学院该月节能宿舍个数
	 */
    private Integer greenDormNum;
	/**
	 *学院该月违章宿舍个数
	 */
    private Integer highPowerDormNum;
    /**
	 *学院男生人数
	 */
    private Integer manNum;
    /**
	 *学院该月窃电宿舍个数
	 */
    private Integer stealDormNum;	
    /**
	 *学院女生人数
	 */
    private Integer womanNum;
    /**
	 *同比上个月是涨幅
	 */
    private Float increase;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
