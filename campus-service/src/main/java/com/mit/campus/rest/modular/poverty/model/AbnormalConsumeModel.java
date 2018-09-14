package com.mit.campus.rest.modular.poverty.model;

import lombok.Data;

/**
 * 
 * 学生异常消费封装类
* @author shuyy
* @date 2018年9月6日
 */
@Data
public class AbnormalConsumeModel {
	/**
	 * 学号
	 */
	private String studentID;
	
	/**
	 * 用于排序
	 */
	private String rate;
}
