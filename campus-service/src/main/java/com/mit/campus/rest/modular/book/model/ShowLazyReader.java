package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  懒惰读者
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("show_lazyreader")
@Data
public class ShowLazyReader extends Model<ShowLazyReader> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String rate;
    private String reason;
    private String year;
    private String college;
    private String major;
    private String readerName;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
