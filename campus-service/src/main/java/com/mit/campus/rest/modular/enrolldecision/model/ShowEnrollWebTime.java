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
@TableName("show_enrollwebtime")
@Data
public class ShowEnrollWebTime extends Model<ShowEnrollWebTime> {

    private String uuid;
    /**
     * 统计区间
     */
    private String countTime;
    /**
     * 计数字符串
     */
    private String countsContent;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
