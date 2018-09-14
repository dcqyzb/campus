package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  网络行为与成绩的实体
 *      MITESOFOR
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@TableName("tb_netbehaviorandgrade")
@Data
public class NetBehaviorAndGrade extends Model<NetBehaviorAndGrade> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    /**
     * 学院
     */
    private String college;
    /**
     * 统计时间
     */
    private String date;
    /**
     * 每天下载流量总量单位M
     */
    private String download;
    /**
     * 上次考试平均成绩
     */
    private String grade;
    /**
     *  平均每人每天的上网时长，小数（/小时）
     */
    private String hour;
    /**
     *专业班级
     */
    private String stuclass;
    /**
     * 学号
     */
    @TableField("studentID")
    private String studentID;
    /**
     *学生姓名
     */
    private String stuname;
    /**
     * 每天上网次数
     */
    private String times;
    /**
     * 每天上传流量总量单位M
     */
    private String upload;
    /**
     *上网看小说时间，单位小时
     */
    private Float bookhour;
    /**
     *上网游戏时间，单位小时
     */
    private Float gamehour;
    /**
     *上网听音乐时间，单位小时
     */
    private Float musichour;
    /**
     *其它上网时间，单位小时
     */
    private Float otherhour;
    /**
     *上网购物时间，单位小时
     */
    private Float shoppinghour;
    /**
     *上网学习时间，单位小时
     */
    private Float studyhour;
    /**
     *上网看视频时间，单位小时
     */
    private Float videohour;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
