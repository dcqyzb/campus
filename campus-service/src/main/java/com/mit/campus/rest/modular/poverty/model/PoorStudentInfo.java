package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 贫困学生信息
* @author shuyy
* @date 2018年9月6日
 */
@TableName("tb_poorstudentinfo")
@Data
public class PoorStudentInfo extends Model<PoorStudentInfo> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**生源地，到市级*/
    private String birthPlace;
    /**学院名*/
    private String college;
    /**专业名*/
    private String major;
    /**贫困等级*/
    private String poorRank;
    /**入学年份*/
    private String schoolYear;
    /**学生姓名*/
    private String stuName;
    /**学生性别*/
    private String stuSex;
    /**学号*/
    private String studentID;
    /**户口*/
    private String familyAcc;
    /**是否为烈士子女*/
    private String childForMartyrs;
    /**每年家庭收入情况*/
    private String familyAnnualIncome;
    /**家庭人口数*/
    private String familyPopNum;
    /**健康状况*/
    private String heathy;
    /**是否孤残*/
    private String isDisabled;
    /** 是否有危重病人*/
    private String isHasPatient;
    /**是否补考*/
    private String isMakeupExam;
    /**是否单亲*/
    private String isSingleParent;
    /**评定年份*/
    private String poorYear;
    /**助学贷款金额*/
    private String studentLoan;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
