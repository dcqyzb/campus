package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  二级菜单（显示数据）书籍预测
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("show_subbookforecast")
@Data
public class ShowSubBookForecast extends Model<ShowSubBookForecast> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String bookType;
    private String bookTypeName;
    private String borrowDegree;
    private String duplicateNum;
    private String forecastNum;
    private String requireNum;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
