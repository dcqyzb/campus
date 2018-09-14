package com.mit.campus.rest.modular.poverty.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
* 学生消费对比
* @author shuyy
* @date 2018年9月6日
 */
@TableName("show_poorcomparison")
@Data
public class ShowPoorComparison extends Model<ShowPoorComparison> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *学院专业班级
	 *经管学院经济管理（1）班
	 */
    private String college;
    /**
	 *学生姓名
	 */
    private String stuname;
    /**
	 *1月到12月每月消费
	 *如："1000, 900, 860, 1600, 2100, 920, 1520, 1532, 2500, 1530, 1495, 2340"
	 *说明：按学院号顺序排序列举每月院人均消费数据
	 */
    private String consvalues;
    /**
	 *年月
	 */
    private String yyyy;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
