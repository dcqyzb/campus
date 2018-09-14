package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 宿舍信息实体类
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_electricdorminfo")
@Data
public class ElectricDormInfo extends Model<ElectricDormInfo> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 宿舍号
	 */
    private String dormID;
    /**
	 * 学生学号拼串，学号，学号，..
	 */
    private String students;
    /**
	 * 类型，男、女
	 */
    private String type;
    /**
	 * 日期  2017-01
	 */
    private String date;
    /**
	 * 宿舍月用电量
	 */
    private Float electricity;
    /**
	 * 历史违章次数
	 */
    private Float highPowerSusp;
    /**
	 * 窃电嫌疑度
	 */
    private Float stealSusp;
    /**
	 * 该月未关电器次数
	 */
    private Integer uncloseNum;
    /**
	 * 学院名
	 */
    private String collegeName;
    /**
	 * 违章嫌疑度
	 */
    private Integer highPowerSum;
    /**
	 * 历史窃电次数
	 */
    private Integer stealSum;
    /**
	 * 宿舍月用电量涨幅
	 */
    private Integer increase;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
