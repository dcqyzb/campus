package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

@Data
@TableName("tb_collegepay")
public class CollegePay extends Model<CollegePay> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 *学院名称
	 */
    private String collegeName;
    /**
	 *学院总人数
	 */
    private Integer collegePopu;
    /**
	 *娱乐人均消费
	 */
    @TableField("ente_value")
    private Integer enteValue;
    /**
	 *水电人均消费
	 */
    @TableField("hydro_value")
    private Integer hydroValue;
    /**
	 *其它人均消费
	 */
    @TableField("other_value")
    private Integer otherValue;
    /**
	 *餐饮人均消费
	 */
    @TableField("repast_value")
    private Integer repastValue;
    
    /**
	 *院人均消费
	 */
    @TableField("pay_value")
    private Integer payValue;
    /**
	 *年份
	 */
    private String year;
    /**
	 *月份
	 */
    private String month;
    /**
	 * 较上一年的人均消费增长或下降,0为增长
	 */
    private Integer updown;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
