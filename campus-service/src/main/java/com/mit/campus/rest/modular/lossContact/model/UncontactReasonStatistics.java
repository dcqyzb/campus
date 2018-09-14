
package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 二级页面-失联原因统计实体类
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("tb_ucreasonstatistics")
@Data
public class UncontactReasonStatistics extends Model<UncontactReasonStatistics> {

    private String uuid;
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学号
     */
    private String stuId;
    /**
     * 院系
     */
    private String stuClass;
    /**
     * 生源地
     */
    private String place;
    /**
     * 入学年份
     */
    private String schoolYear;
    /**
     * 失联时间
     */
    private String lossTime;
    /**
     * 找到时间
     */
    private String findTime;
    /**
     * 处理时长
     */
    private String dealDuration;
    /**
     * 发现地点
     */
    private String findSite;
    /**
     * 失联原因
     */
    private String lossReason;
    /**
     * 学生标签
     */
    private String stuTag;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
