package com.mit.campus.rest.modular.electricalBehavior.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
* 二级页面-学院违章宿舍，某月某学院发现违章的宿舍信息
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_electriccollegedormhighpower")
@Data
public class ElectricCollegeDormHighPower extends Model<ElectricCollegeDormHighPower> {
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
	 * 年月日，被发现违章的具体日期2017-12-03
	 */
    private String date;
    /**
	 *月用电量
	 */
    private float electricity;
    /**
	 *发现的违章电器
	 */
    private String highPower;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
