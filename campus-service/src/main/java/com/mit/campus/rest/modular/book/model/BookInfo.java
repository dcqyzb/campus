package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *      书籍信息
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-05
 */
@TableName("tb_bookinfo")
@Data
public class BookInfo extends Model<BookInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String bookName;
    private String borrowTimes;
    private String cluster;
    private String year;
    private String author;
    private String damage;
    private String edition;
    private String position;
    private String publicationDate;
    private String publish;
    private String purchaseYear;
    private String type;
    private String typeName;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
