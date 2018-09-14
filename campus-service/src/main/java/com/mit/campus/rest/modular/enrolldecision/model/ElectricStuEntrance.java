package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 宿舍学生门禁实体类
 * @Author: Mr.Deng
 * @date 2018/09/07
 */
@TableName("tb_electricstuentrance")
@Data
public class ElectricStuEntrance extends Model<ElectricStuEntrance> {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String studentID;
    private String date;
    private String type;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
