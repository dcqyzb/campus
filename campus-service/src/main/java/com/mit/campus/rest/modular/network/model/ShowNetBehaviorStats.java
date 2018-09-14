package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.annotations.TableField;
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
@Data
@TableName("show_netbehaviorstats")
public class ShowNetBehaviorStats extends Model<ShowNetBehaviorStats> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String download;
    @TableField("netHour1")
    private String netHour1;
    @TableField("netHour2")
    private String netHour2;
    @TableField("netHour3")
    private String netHour3;
    @TableField("netHour4")
    private String netHour4;
    @TableField("netTime1")
    private String netTime1;
    @TableField("netTime2")
    private String netTime2;
    @TableField("netTime3")
    private String netTime3;
    @TableField("netTime4")
    private String netTime4;
    private String school;
    private String term;
    private String upload;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
