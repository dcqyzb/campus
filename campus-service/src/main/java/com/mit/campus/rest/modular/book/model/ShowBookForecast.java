package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  图书预测
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("show_bookforecast")
@Data
public class ShowBookForecast extends Model<ShowBookForecast> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String duplicateNum;
    private String forecastNum;
    private String type;
    private String typeName;
    private String adviseBuyNum;
    private String beyondForecastNum;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
