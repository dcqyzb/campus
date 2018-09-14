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
@TableName("tb_readerapriorirule")
public class ReaderAprioriRule extends Model<ReaderAprioriRule> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String confidence;
    private String rule1;
    private String rule2;
    private String support;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getRule1() {
        return rule1;
    }

    public void setRule1(String rule1) {
        this.rule1 = rule1;
    }

    public String getRule2() {
        return rule2;
    }

    public void setRule2(String rule2) {
        this.rule2 = rule2;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Readerapriorirule{" +
        "uuid=" + uuid +
        ", confidence=" + confidence +
        ", rule1=" + rule1 +
        ", rule2=" + rule2 +
        ", support=" + support +
        "}";
    }
}
