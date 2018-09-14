package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-06
 */
@TableName("tb_bookaprioriforecast")
@Data
public class BookAprioriForecast extends Model<BookAprioriForecast> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String bookName;
    private String cluster;
    private String damage;
    private String pages;
    private String position;
    private String type;
    private String typeName;
    private String version;
    private String year;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }


}
