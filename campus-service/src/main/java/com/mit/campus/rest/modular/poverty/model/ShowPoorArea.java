package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 贫困区域
* @author shuyy
* @date 2018年9月6日
 */
@TableName("show_poorarea")
@Data
public class ShowPoorArea extends Model<ShowPoorArea> {

    private static final long serialVersionUID = 1L;
    private String uuid;
    /**
	 *中等贫困人数
	 */
    private Integer medium;
    /**
	 *一般贫困人数
	 */
    private Integer normal;
    /**
	 *特困人数
	 */
    private Integer poorest;
    /**
	 *省
	 */
    private String province;
    /**
	 *省总共贫困人数
	 */
    private Integer total;
    /**
	 *年
	 */
    private String year;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
