/**
 * EnrollJob.java
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
 * @Description: 招生决策-二级页面-就业情况统计
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("tb_enrolljob")
@Data
public class EnrollJob extends Model<EnrollJob> {

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
    private String college;
    /**
     * 城市名
     */
    private String city;
    /**
     * 总人数
     */
    private String counts;
    /**
     * 男
     */
    private String manCount;
    /**
     * 女
     */
    private String womanCount;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}

