package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("tb_stunetbehaviorwords")
@Data
public class StuNetBehaviorWords extends Model<StuNetBehaviorWords> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private Float avghour;
    @TableField("collegeName")
    private String collegeName;
    private String date;
    @TableField("keyWords")
    private String keyWords;
    private String major;
    @TableField("studentID")
    private String studentID;
    @TableField("studentName")
    private String studentName;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
