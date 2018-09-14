package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 二级页面-学院节能宿舍
 * @Author: Mr.Deng
 * @date 2018/09/07
 */
@TableName("tb_electriccollegedormgreen")
@Data
public class ElectricCollegeDormGreen extends Model<ElectricCollegeDormGreen> {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String dormID;
    private String type;
    private String collegeName;
    private String peopleNum;
    private String date;
    private float avgElec;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
