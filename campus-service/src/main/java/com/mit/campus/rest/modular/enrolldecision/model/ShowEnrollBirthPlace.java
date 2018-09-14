package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 招生决策，结果表-生源地分布
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("show_enrollbirthplace")
@Data
public class ShowEnrollBirthPlace extends Model<ShowEnrollBirthPlace> {
    private String uuid;
    /**
     * 生源地
     */
    private String birthPlace;
    /**
     * 毕业年份
     */
    private String graduateYear;
    /**
     * 学院
     */
    private String collegeName;
    /**
     * 该省人数是否排名前列，即重点生源，默认为false
     */
    private boolean isPrimaryPlace = false;
    /**
     * 学生人数
     */
    private int counts;
    /**
     * 毕业去向集合，到省级行政
     */
    private String graduatePlaces;
    /**
     * 毕业去向集合，到省级行政
     */
    private String amountChange;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
