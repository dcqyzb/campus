package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
/**
 * 
* 贫困生关联规则实体表
* @author shuyy
* @date 2018年9月7日
 */
@TableName("tb_poorAprioriRule")
@Data
public class PoorAprioriRule extends Model<PoorStuForecast> {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.UUID)
	private String uuid;
	
	/**
	 * 左边的关联规则
	 */
	private String rule1;
	
	/**
	 * 右边的关联规则
	 */
	private String rule2;
	
	/**
	 * 支持度
	 */
	private String support;
	
	/**
	 * 置信度
	 */
	private String confidence;

	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}

	
	
}
