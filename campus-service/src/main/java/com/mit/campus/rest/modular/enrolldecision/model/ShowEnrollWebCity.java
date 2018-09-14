package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 招生决策，访问分布城市，网络来源
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("show_enrollwebcity")
@Data
public class ShowEnrollWebCity extends Model<ShowEnrollWebCity> {

    private String uuid;
    /**
     * 城市名
     */
    private String city;
    /**
     * 月份
     */
    private String month;
    /**
     * 城市访问总数
     */
    private long counts;
    /**
     * 网络来源,拼接字段，包含网络来源及数量，如“百度=1200#360=1000#搜狗=800#省教育网=2300”
     */
    private String netSource;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
