package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("tb_booksequation")
public class BookSequation extends Model<BookSequation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String newVal;
    private String sequation;
    private String type;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNewVal() {
        return newVal;
    }

    public void setNewVal(String newVal) {
        this.newVal = newVal;
    }

    public String getSequation() {
        return sequation;
    }

    public void setSequation(String sequation) {
        this.sequation = sequation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Booksequation{" +
        "uuid=" + uuid +
        ", newVal=" + newVal +
        ", sequation=" + sequation +
        ", type=" + type +
        "}";
    }
}
