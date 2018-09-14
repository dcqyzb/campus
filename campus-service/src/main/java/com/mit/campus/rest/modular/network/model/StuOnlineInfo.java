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
 * @since 2018-09-11
 */
@TableName("tb_stuonlineinfo")
@Data
public class StuOnlineInfo extends Model<StuOnlineInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String college;
    private String duration;
    @TableField("durationCluster")
    private String durationCluster;
    @TableField("studentID")
    private String studentID;
    private String term;
    private String times;
    @TableField("timesCluster")
    private String timesCluster;
    private String year;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
