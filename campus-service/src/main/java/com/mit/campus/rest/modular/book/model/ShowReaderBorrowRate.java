package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  读者借阅率
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("show_readerborrowrate")
@Data
public class ShowReaderBorrowRate extends Model<ShowReaderBorrowRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String college;
    private String collegeBorrowRate;
    private String major;
    private String majorBorrowRate;
    private String year;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
