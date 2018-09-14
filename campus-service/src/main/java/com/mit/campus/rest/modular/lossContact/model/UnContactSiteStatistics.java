package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 二级页面-根据失联地点的统计类
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("tb_ucsitestatistics")
@Data
public class UnContactSiteStatistics extends Model<UnContactSiteStatistics> {

    private String uuid;
    /**
     * 失联地点
     */
    private String siteName;
    /**
     * 统计年份
     */
    private String countYear;
    /**
     * 失联总人数
     */
    private int amount;
    /**
     * 区域负责人
     */
    private String responsible;
    /**
     * 位置信息
     */
    private String siteTip;
    /**
     * 活跃度
     */
    private String actLevel;
    /**
     * 监控个数
     */
    private int monAmount;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
