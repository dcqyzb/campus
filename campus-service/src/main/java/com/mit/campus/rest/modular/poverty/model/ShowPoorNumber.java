package com.mit.campus.rest.modular.poverty.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
/**
 * 
 * 精准资助贫困人数按学院分布统计数据
* @author shuyy
* @date 2018年9月6日
 */
@TableName("show_PoorNumber")
@Data
public class ShowPoorNumber extends Model<ShowPoorNumber>{

	
	@TableId(value = "uuid", type = IdType.UUID)
	private String uuid;
	
	/**
	 * 年份
	 */
	private String year;
	
	/**
	 *学院名称
	 */
	private String colleague;
	
	
	/**
	 *学院总共贫困人数
	 */
	private int total;
	
	/**
	 *一般贫困人数
	 */
	private int normal;
	
	/**
	 *中等贫困人数
	 */
	private int medium;
	
	
	/**
	 *特困人数
	 */
	private int poorest;


	@Override
	protected Serializable pkVal() {
		return this.uuid;
	}


}
