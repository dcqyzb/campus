package com.mit.campus.rest.modular.book.model;

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
 * @since 2018-09-06
 */
@TableName("tb_bookapriorirule")
@Data
public class BookAprioriRule extends Model<BookAprioriRule> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String confidence;
    private String rule1;
    private String rule2;
    private String support;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
