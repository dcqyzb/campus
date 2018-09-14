package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
* 贫困生预测
* @author shuyy
* @date 2018年9月6日
 */
@TableName("tb_poorstuforecast")
@Data
public class PoorStuForecast extends Model<PoorStuForecast> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 是否为烈士子女
	 */
    private String childForMartyrs;
    /**
	 * 学院
	 */
    private String collegeName;
    /**
	 * 每年家庭收入情况
	 */
    private String familyAnnualIncome;
    /**
	 * 家庭人口数
	 */
    private String familyPopNum;
    /**
	 * 健康状况
	 */
    private String heathy;
    /**
	 * 是否孤残
	 */
    private String isDisabled;
    /**
	 * 是否有危重病人
	 */
    private String isHasPatient;
    /**
	 * 是否补考
	 */
    private String isMakeupExam;
    /**
	 * 是否单亲
	 */
    private String isSingleParent;
    /**
	 * 专业
	 */
    private String major;
    /**
	 * 贫困等级
	 */
    private String poorRank;
    /**
	 * 入学前户口
	 */
    private String preSchoolResidence;
    /**
	 * 姓名
	 */
    private String stuName;
    /**
	 * 学号
	 */
    private String studentID;
    /**
	 * 助学贷款金额
	 */
    private String studentLoan;
    private String schoolYear;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
