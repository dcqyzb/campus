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
@TableName("tb_subbooksequation")
public class SubBookSequation extends Model<SubBookSequation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String name;
    private String newVal;
    private String sequation;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Subbooksequation{" +
        "uuid=" + uuid +
        ", name=" + name +
        ", newVal=" + newVal +
        ", sequation=" + sequation +
        "}";
    }
}
