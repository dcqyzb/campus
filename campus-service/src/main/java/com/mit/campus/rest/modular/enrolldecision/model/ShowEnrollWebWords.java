package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("show_enrollwebwords")
@Data
public class ShowEnrollWebWords extends Model<ShowEnrollWebWords> {

    private String uuid;
    /**
     * 起始统计月份，如2017-08至今
     */
    private String startMonth;
    /**
     * 关键词
     */
    private String keyWords;
    /**
     * 计数
     */
    private int counts;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
