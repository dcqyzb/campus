/**
 * ErollVisitTime.java
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
 * @Description: 招生决策-二级页面 访问时间统计
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("tb_enrollvisitime")
@Data
public class ErollVisitTime extends Model<ErollVisitTime> {
    private String uuid;

    /**
     * 统计年份
     */
    private String year;
    /**
     * 总访问量
     */
    private String count;
    /**
     * 一月访问量
     */
    private String jan;
    /**
     * 一月访问量
     */
    private String feb;
    /**
     * 一月访问量
     */
    private String mar;
    /**
     * 一月访问量
     */
    private String apr;
    /**
     * 一月访问量
     */
    private String may;
    /**
     * 一月访问量
     */
    private String june;
    /**
     * 一月访问量
     */
    private String july;
    /**
     * 一月访问量
     */
    private String aug;
    /**
     * 一月访问量
     */
    private String sep;
    /**
     * 一月访问量
     */
    private String oct;
    /**
     * 一月访问量
     */
    private String nov;
    /**
     * 一月访问量
     */
    private String dece;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}

