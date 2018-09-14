package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 宿舍月用电量
 * @Author: Mr.Deng
 * @date 2018/09/07
 */
@TableName("tb_electricdormelectric")
@Data
public class ElectricDormElectric extends Model<ElectricDormElectric> {

    private static final long serialVersionUID = 1L;
    private String uuid;
    private String dormID;
    private String date;
    private float electricity;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
