package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 学生失联地点
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("show_uncontactplace")
@Data
public class ShowUncontactPlace extends Model<ShowUncontactPlace> {

    private static final long serialVersionUID = -2125754215154198591L;
    private String uuid;
    /**
     * 地点名称
     */
    private String placename;
    /**
     * 地点坐标
     */
    private String coordinate;
    /**
     * 年月
     */
    private String yyMM;
    /**
     * 次数/时长等
     */
    private int number;
    /**
     * 类型
     */
    private String placetype;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
