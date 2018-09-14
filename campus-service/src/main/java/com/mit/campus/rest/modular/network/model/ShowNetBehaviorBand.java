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
@TableName("show_netbehaviorband")
@Data
public class ShowNetBehaviorBand extends Model<ShowNetBehaviorBand> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    @TableField("countTime")
    private String countTime;
    @TableField("wirelessContent")
    private String wirelessContent;
    @TableField("wiredContent")
    private String wiredContent;
    private String college;



    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
