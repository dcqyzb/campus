package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  读者信息
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("tb_readerinfo")
@Data
public class ReaderInfo extends Model<ReaderInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String borrowTimes;
    private String cluster;
    private String collegeName;
    private String dormitory;
    private String major;
    private String readerID;
    private String readerIdentity;
    private String readerName;
    private String readerSex;
    private String schoolYear;
    private Integer bookSum;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
