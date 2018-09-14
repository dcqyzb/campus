package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 宿舍学生上网记录实体类
 * @Author: Mr.Deng
 * @date 2018/09/07
 */
@TableName("tb_electricstuonline")
@Data
public class ElectricStuOnline extends Model<ElectricStuOnline> {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String studentID;
    private String loginTime;
    private String logoutTime;
    private String position;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
