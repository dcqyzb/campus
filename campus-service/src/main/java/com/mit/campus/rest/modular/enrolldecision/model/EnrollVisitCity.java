/**
 * EnrollVisitCity.java
 *
 * @描述 TODO
 * @作者 wangjh
 * @日期 2018年4月22日
 */
package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 访问来源统计
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("tb_enrollvisitcity")
@Data
public class EnrollVisitCity extends Model<EnrollVisitCity> {

    private String uuid;
    /**
     * 省名
     */
    private String province;
    /**
     * 城市名
     */
    private String city;
    /**
     * 月份,年月格式 yyyy-MM
     */
    private String month;
    /**
     * 城市访问总数
     */
    private String counts;
    /**
     * 360搜索量-奇虎360
     */
    private String qihuCount;
    /**
     * 百度搜索量
     */
    private String baiduCount;
    /**
     * 搜狗搜索量
     */
    private String sougouCount;
    /**
     * 学校官网搜索量-奇虎360
     */
    private String schoolCount;
    /**
     * 招生网搜索量
     */
    private String enrollCount;
    /**
     * 教育搜索量
     */
    private String eduSiteCount;
    /**
     * 其他
     */
    private String otherCount;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}

