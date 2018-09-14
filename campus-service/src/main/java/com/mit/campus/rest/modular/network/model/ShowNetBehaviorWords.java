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
@TableName("show_netbehaviorwords")
public class ShowNetBehaviorWords extends Model<ShowNetBehaviorWords> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private Integer counts;
    @TableField("keyWords")
    private String keyWords;
    private String pencentage;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
