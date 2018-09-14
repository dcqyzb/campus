package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 二级页面-失联预警事件类
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("tb_ucwarninc")
@Data
public class UncontactWarnInc extends Model<UncontactWarnInc> {
    private String uuid;
    /**
     * 失联号
     */
    private String ucId;
    /**
     * 学生学号
     */
    private String stuId;
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学生联系方式
     */
    private String stuPhone;
    /**
     * 院系
     */
    private String stuClass;
    /**
     * 学院
     */
    private String college;
    /**
     * 辅导员姓名
     */
    private String teaName;
    /**
     * 预警时间
     */
    private String createTime;
    /**
     * 最新处理时间
     */
    private String dealTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 宿舍人员
     */
    private String roomMates;
    /**
     * 亲密好友
     */
    private String friends;
    /**
     * 最新状态
     */
    private String status;
    /**
     * 最后出现地点
     */
    private String lastShowSite;
    /**
     * 最后出现时间
     */
    private String lastShowTime;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
