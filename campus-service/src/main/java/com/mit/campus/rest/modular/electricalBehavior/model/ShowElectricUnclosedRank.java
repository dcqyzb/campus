package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 未关电器分布表
* @author shuyy
* @date 2018年9月7日
 */
@TableName("show_electricunclosedrank")
@Data
public class ShowElectricUnclosedRank extends Model<ShowElectricUnclosedRank> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 楼栋
	 */
    private String dormitory;
    /**
	 * 未关次数
	 */
    private Integer unclosenumbers;
    /**
	 * 日期
	 * yyyy-mm
	 */
    private String yyyymm;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
