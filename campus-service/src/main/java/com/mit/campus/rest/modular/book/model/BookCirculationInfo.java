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
@TableName("tb_bookcirculationinfo")
@Data
public class BookCirculationInfo extends Model<BookCirculationInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String bookType;
    private String bookUseRate;
    private String borrowTimes;
    private String duplicateNum;
    private String newBookTotalNum;
    private String newBookTotalType;
    private String totalNum;
    private String year;
    private String sequation;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
