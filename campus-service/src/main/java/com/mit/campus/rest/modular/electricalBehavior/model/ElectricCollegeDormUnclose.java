package com.mit.campus.rest.modular.electricalBehavior.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 二级页面-宿舍未关电器详情
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_electriccollegedormunclose")
@Data
public class ElectricCollegeDormUnclose extends Model<ElectricCollegeDormUnclose> {

    private static final long serialVersionUID = 1L;
    private String uuid;
    /**
	 *宿舍号 1-0201（1栋2楼1室）
	 */
    private String dormID;
    /**
	 * 时间段，表示该时间段存在未关电器，2017-12-01 8:00:00~10:00:00
	 */
    private String date;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
