package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 就业情况表，毕业生去最多的几个城市，包括学院、专业、男女的统计
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("show_enrollworkcity")
@Data
public class ShowEnrollWorkCity extends Model<ShowEnrollWorkCity> {

    private String uuid;
    /**
     * 就业地区
     */
    private String workArea;
    /**
     * 毕业年份
     */
    private String graduteYear;
    /**
     * 学院
     */
    private String collegeName;
    /**
     * 城市名
     */
    private String cityName;
    /**
     * 城市人数
     */
    private int cityCounts;
    /**
     * 学院男女比例
     */
    private String citySexContent;
    /**
     * 城市男女比例
     */
    private String collegeContent;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
