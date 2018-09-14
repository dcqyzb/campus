package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 
* 精准资助贫困生输入表
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_poorStuInput")
@Data
public class PoorStuInput extends Model<PoorStuForecast>{

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.UUID)
	private String uuid;
	
	private String poorRank;
	
	private String preSchoolResidence;
	
	private String isDisabled;
	
	private String isSingleParent;
	
	private String childForMartyrs;
	
	private String heathy;
	
	private String familyPopNum;
	
	private String familyAnnualIncome;
	
	private String isHasPatient;
	
	private String studentLoan;
	
	private String isMakeupExam;

	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}
	
	
	
	
}
