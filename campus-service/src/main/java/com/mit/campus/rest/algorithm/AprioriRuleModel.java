package com.mit.campus.rest.algorithm;

import lombok.Data;

/**
 * 
* 关联规则封装类
* @author shuyy
* @date 2018年9月7日
 */
@Data
public class AprioriRuleModel {

	private String rule1;
	
	private String rule2;
	
	private String support;
	
	private String confidence;

	
}
