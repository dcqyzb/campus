package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  学校工作人员信息实体类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@TableName("tb_employee")
@Data
public class Employee extends Model<Employee> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    /**
     * 所属部门
     */
    private String department;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 员工姓名
     */
    private String employeename;
    /**
     * 联系方式
     */
    private String phonenumber;
    /**
     * 职务名称
     */
    private String position;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
