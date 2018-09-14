package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@TableName("show_netbehaviorgrade")
@Data
public class ShowNetBehaviorGrade extends Model<ShowNetBehaviorGrade> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String school;
    private String term;
    /**
     * 区间成绩，70-80分
     */
    private String grade;
    /**
     * 平均每天上网时长，小时
     */
    private String hour;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
