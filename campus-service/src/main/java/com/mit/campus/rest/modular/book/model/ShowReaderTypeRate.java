package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  读者类型率
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("show_readertyperate")
@Data
public class ShowReaderTypeRate extends Model<ShowReaderTypeRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String readerType;
    private String readerTypeRate;
    private String year;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
