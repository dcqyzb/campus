package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
*  宿舍用电节能排行表
* @author shuyy
* @date 2018年9月7日
 */
@TableName("show_electricdormrank")
@Data
public class ShowElectricDormRank extends Model<ShowElectricDormRank> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 宿舍号
	 */
    private String dormitory;
    /**
	 * 用电量
	 */
    private Float electricity;
    /**
	 * 宿舍人数
	 */
    private Integer number;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }


}
