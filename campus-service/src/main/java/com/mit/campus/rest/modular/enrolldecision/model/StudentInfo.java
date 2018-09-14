package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 学生实体表
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("tb_studentInfo")
@Data
public class StudentInfo extends Model<StudentInfo> {
    private static final long serialVersionUID = 1L;
    private String uuid;
    /**
     * 学号
     */
    private String studentID;
    /**
     * 姓名
     */
    private String studentName;
    /**
     * 性别
     */
    private String studentSex;
    /**
     * 民族
     */
    private String nation;
    /**
     * 学制
     */
    private String schoolLength;
    /**
     * 学院名称
     */
    private String collegeName;
    /**
     * 班级
     */
    private String stuClass;
    /**
     * 专业
     */
    private String major;
    /**
     * 专业所属分类
     */
    private String majorType;
    /**
     * 入取成绩
     */
    private String enrollScore;
    /**
     * 户口性质
     */
    private String householdType;
    /**
     * 宿舍号
     */
    private String dormitoryID;
    /**
     * 出生年月
     */
    private String birthady;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 身份证号
     */
    private String IDNumber;
    /**
     * 出生地，生源地
     */
    private String birthPlace;
    /**
     * 出生地区，生源地属地区
     */
    private String birthArea;
    /**
     * 毕业去向（江西南昌）
     */
    private String graduationWhere;
    /**
     * 毕业去向（华东）
     */
    private String graduationWhereRegion;
    /**
     * 入学年份
     */
    private String entranceYear;
    /**
     * 辅导员
     */
    private String instructor;
    /**
     * 辅导员联系方式
     */
    private String instructorLink;
    /**
     * 监护人
     */
    private String guardian;
    /**
     * 监护人联系方式
     */
    private String guardianLink;
    /**
     * 亲密朋友
     */
    private String friends;
    /**
     * 沉迷网络
     */
    private String webAddiction;
    /**
     * 社团活动
     */
    private String communityActivity;
    /**
     * 是否开网店
     */
    private String isOnlineStore;
    /**
     * 是否做微商
     */
    private String isWechatBusiness;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
