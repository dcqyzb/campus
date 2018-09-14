package com.mit.campus.rest.modular.poverty.controller;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.campus.rest.algorithm.AprioriRuleModel;
import com.mit.campus.rest.algorithm.model.AprioriCommon;
import com.mit.campus.rest.modular.poverty.dao.PoorAprioriRuleMapper;
import com.mit.campus.rest.modular.poverty.dao.PoorStuForecastMapper;
import com.mit.campus.rest.modular.poverty.dao.PoorStuInputMapper;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorAbnormalConsumptionMapper;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorForecastMapper;
import com.mit.campus.rest.modular.poverty.model.PoorAprioriRule;
import com.mit.campus.rest.modular.poverty.model.PoorStuForecast;
import com.mit.campus.rest.modular.poverty.model.PoorStuInput;
import com.mit.campus.rest.modular.poverty.model.ShowPoorAbnormalConsumption;
import com.mit.campus.rest.modular.poverty.model.ShowPoorForecast;
import com.mit.campus.rest.util.QueryCondition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* 精准资助算法
* @author shuyy
* @date 2018年9月11日
* @company mitesofor
 */
@RestController
@Slf4j
@RequestMapping("povertyAlgorithm")
@Api(value="精准资助算法",tags={"精准资助算法"})
public class PovertyAlgorithmController {
	
	@Autowired
	private PoorStuInputMapper PoorStuInputMapper;
	
	@Autowired
	private PoorAprioriRuleMapper poorAprioriRuleMapper;
	
	@Autowired
	private PoorStuForecastMapper poorStuForecastMapper;
	
	@Autowired
	private ShowPoorForecastMapper showPoorForecastMapper;
	
	@Autowired
	private ShowPoorAbnormalConsumptionMapper showPoorAbnormalConsumptionMapper;
	
	/**
	 *
	* 贫困生模型规则生成。
	*  通过apriori算法，传入最小置信度，最小支持度， 要计算的贫困等级， 算出这个指定的贫困等级和那些因素有关，把这个关联关系保存进一个表。
	* @param @param minSupport 最小支持度
	* @param @param minConfidence 最小置信度
	* @param @param poorRank 贫困等级
	* @param @throws IOException
	* @return void
	* @throws
	* @author shuyy
	 */
	@ApiOperation(value="贫困生模型规则生成。", notes="通过apriori算法，传入最小置信度，最小支持度，"
			+ " 要计算的贫困等级， 算出这个指定的贫困等级和那些因素有关，把这个关联关系保存进一个表。")
	@RequestMapping(value = "/poorStuProRule", method = RequestMethod.POST)
	public void poorStuProRule(double minSupport, double minConfidence, String poorRank)
			throws IOException {
		// 设置查询条件
		QueryCondition condition = new QueryCondition("poorRank", QueryCondition.EQ, poorRank);
		// 所有贫困生的数据，用这个数据进行分析贫困关联关系
		// 查询对应贫困等级的贫困生数据
		List<PoorStuInput> psi = PoorStuInputMapper.selectList(null);
		// 存放离散化后的<<所有贫困生>>数据
		List<List<String>> record = new ArrayList<List<String>>();
		// 遍历所有贫困生信息数据，进行数据转换。
		if (psi != null && psi.size() > 0) {
			for (PoorStuInput p : psi) {
				// 存放离散化后的<<单个贫困生>>数据
				List<String> l = new ArrayList<String>();
				// 是否是烈士子女
				if (p.getChildForMartyrs().equals("是")) {
					l.add("A1");
				} else {
					l.add("A2");
				}
				// 家庭年收入
				if (Integer.parseInt(p.getFamilyAnnualIncome()) < 20000) {
					l.add("B1");
				} else if (Integer.parseInt(p.getFamilyAnnualIncome()) < 80000
						&& Integer.parseInt(p.getFamilyAnnualIncome()) >= 20000) {
					l.add("B2");
				} else if (Integer.parseInt(p.getFamilyAnnualIncome()) >= 80000
						&& Integer.parseInt(p.getFamilyAnnualIncome()) <= 180000) {
					l.add("B3");
				}
				// 家庭人口数
				if (Integer.parseInt(p.getFamilyPopNum()) <= 3 && Integer.parseInt(p.getFamilyPopNum()) >= 1) {
					l.add("C1");
				} else if (Integer.parseInt(p.getFamilyPopNum()) <= 6 && Integer.parseInt(p.getFamilyPopNum()) >= 4) {
					l.add("C2");
				} else {
					l.add("C3");
				}
				// 健康状况
				if (p.getHeathy().equals("良好")) {
					l.add("D1");
				} else if (p.getHeathy().equals("一般")) {
					l.add("D2");
				} else {
					l.add("D3");
				}
				// 是否孤残
				if (p.getIsDisabled().equals("是")) {
					l.add("E1");
				} else {
					l.add("E2");
				}
				// 是否有危重病人
				if (p.getIsHasPatient().equals("是")) {
					l.add("F1");
				} else {
					l.add("F2");
				}
				// 是否单亲
				if (p.getIsSingleParent().equals("是")) {
					l.add("G1");
				} else {
					l.add("G2");
				}
				// 贫困等级
				if (p.getPoorRank().equals("特困")) {
					l.add("H1");
				} else if (p.getPoorRank().equals("困难")) {
					l.add("H2");
				} else {
					l.add("H3");
				}
				// 户口性质
				if (p.getPreSchoolResidence().equals("农村")) {
					l.add("I1");
				} else {
					l.add("I2");
				}
				// 助学贷款
				if (Double.parseDouble(p.getStudentLoan()) == 0) {
					l.add("J1");
				} else if (Double.parseDouble(p.getStudentLoan()) <= 3000
						&& Double.parseDouble(p.getStudentLoan()) > 0) {
					l.add("J2");
				} else if (Double.parseDouble(p.getStudentLoan()) > 3000
						&& Double.parseDouble(p.getStudentLoan()) <= 5000) {
					l.add("J3");
				} else if (Double.parseDouble(p.getStudentLoan()) > 5000
						&& Double.parseDouble(p.getStudentLoan()) <= 10000) {
					l.add("J4");
				}
				// 是否补考
				if (p.getIsMakeupExam().equals("是")) {
					l.add("K1");
				} else {
					l.add("K2");
				}
				record.add(l);
			}
		}

		// 转换贫困等级代码
		String poorB = "";
		if (poorRank.equals("特困")) {
			poorB = "[H1]";
		} else if (poorRank.equals("困难")) {
			poorB = "[H2]";
		} else {
			poorB = "[H3]";
		}
		/**
		 * @描述：通过关联分析算法找出关联规则
		 * @param minSupport：最小支持度
		 * @param minConfidence：最小置信度
		 * @param record：训练数据离散化后的数据
		 * @param poorB：贫困等级
		 */
		List<AprioriRuleModel> rs = AprioriCommon.getRules(minSupport, minConfidence, record, poorB);
		/*
		 * 用关联算法跑出规则
		 */
		if (rs.size() == 0) {
			System.out.println("没有找到规则");
			return;
		}
		for (AprioriRuleModel rule : rs) {
			System.out.println(rule.getRule1() + "==>" + rule.getRule2() + "--- " + "【支持度】：" + rule.getSupport()
					+ "  【置信度】：" + rule.getConfidence());
		}

		// 选择最优规则
		// 存放贫困生关联规则
		PoorAprioriRule api = null;
		// 用于存放单个的规则
		List<List<String>> bossList = new ArrayList<List<String>>();
		List<String> letter = null;
		// 从强关联规则中寻找规则个数最多的关联规则
		for (int i = rs.size() - 1; i >= 0; i--) {
			api = new PoorAprioriRule();
			AprioriRuleModel rule = rs.get(i);
			if (i == rs.size() - 1) {
				api.setSupport(rule.getSupport());
				api.setConfidence(rule.getConfidence());
				api.setRule1(rule.getRule1());
				String ruleStr = rule.getRule1().replace(" ", "");
				String[] ruleArr = ruleStr.substring(1, ruleStr.length() - 1).split(",");
				bossList.add(Arrays.asList(ruleArr));
				api.setRule2(rule.getRule2());
				poorAprioriRuleMapper.insert(api);
			} else {
				if (rule.getRule1().length() >= rs.get(rs.size() - 1).getRule1().length()) {
					String ruleStr = rule.getRule1().replace(" ", "");
					String[] ruleArr = ruleStr.substring(1, ruleStr.length() - 1).split(",");
					letter = Arrays.asList(ruleArr);
					boolean f = false;
					for (List<String> boss : bossList) {
						// 防止获取重复的关联规则
						if (boss.containsAll(letter)) {
							f = true;
						}
					}
					if (!f) {
						api.setSupport(rule.getSupport());
						api.setConfidence(rule.getConfidence());
						// 左边规则
						api.setRule1(rule.getRule1());
						// 右边规则
						api.setRule2(rule.getRule2());
						poorAprioriRuleMapper.insert(api);
						bossList.add(letter);
					}
				} else {
					break;
				}
			}
		}
	}

	/**
	 * 
	* 
	* 预测下一届贫困生,通过上面获取的强关联规则来预测下一届学生的贫困生信息
	* @param 
	* @return void
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/forecastPoorStu", method = RequestMethod.POST)
	@ApiOperation(value="预测下一届贫困生,通过上面获取的强关联规则来预测下一届学生的贫困生信息。", notes="预测下一届贫困生,"
			+ "通过获取的强关联规则来预测下一届学生的贫困生信息")
	public @ResponseBody void forecastPoorStu() {
		// 获取所有要预测的贫困生信息
		List<PoorStuForecast> psf = poorStuForecastMapper.selectList(null);
		List<String> poorStuList = null;
		if (null != psf && psf.size() > 0) {
			for (PoorStuForecast p : psf) {
				poorStuList = new ArrayList<String>();
				// 是否是烈士子女
				if (p.getChildForMartyrs().equals("是")) {
					poorStuList.add("A1");
				} else {
					poorStuList.add("A2");
				}
				// 家庭年收入
				if (Integer.parseInt(p.getFamilyAnnualIncome()) < 20000) {
					poorStuList.add("B1");
				} else if (Integer.parseInt(p.getFamilyAnnualIncome()) < 80000
						&& Integer.parseInt(p.getFamilyAnnualIncome()) >= 20000) {
					poorStuList.add("B2");
				} else if (Integer.parseInt(p.getFamilyAnnualIncome()) >= 80000
						&& Integer.parseInt(p.getFamilyAnnualIncome()) <= 180000) {
					poorStuList.add("B3");
				}
				if (Integer.parseInt(p.getFamilyPopNum()) <= 3 && Integer.parseInt(p.getFamilyPopNum()) >= 1) {// 家庭人口数
					poorStuList.add("C1");
				} else if (Integer.parseInt(p.getFamilyPopNum()) <= 6 && Integer.parseInt(p.getFamilyPopNum()) >= 4) {
					poorStuList.add("C2");
				} else {
					poorStuList.add("C3");
				}
				// 健康状况
				if (p.getHeathy().equals("良好")) {
					poorStuList.add("D1");
				} else if (p.getHeathy().equals("一般")) {
					poorStuList.add("D2");
				} else {
					poorStuList.add("D3");
				}
				// 是否孤残
				if (p.getIsDisabled().equals("是")) {
					poorStuList.add("E1");
				} else {
					poorStuList.add("E2");
				}
				// 是否有危重病人
				if (p.getIsHasPatient().equals("有")) {
					poorStuList.add("F1");
				} else {
					poorStuList.add("F2");
				}
				// 是否单亲
				if (p.getIsSingleParent().equals("是")) {
					poorStuList.add("G1");
				} else {
					poorStuList.add("G2");
				}
				// 户口性质
				if (p.getPreSchoolResidence().equals("农村")) {
					poorStuList.add("I1");
				} else {
					poorStuList.add("I2");
				}
				// 助学贷款
				if (Double.parseDouble(p.getStudentLoan()) == 0) {
					poorStuList.add("J1");
				} else if (Double.parseDouble(p.getStudentLoan()) <= 3000
						&& Double.parseDouble(p.getStudentLoan()) > 0) {
					poorStuList.add("J2");
				} else if (Double.parseDouble(p.getStudentLoan()) > 3000
						&& Double.parseDouble(p.getStudentLoan()) <= 5000) {
					poorStuList.add("J3");
				} else if (Double.parseDouble(p.getStudentLoan()) > 5000
						&& Double.parseDouble(p.getStudentLoan()) <= 10000) {
					poorStuList.add("J4");
				}
				// 是否补考
				if (p.getIsMakeupExam() == "是") {
					poorStuList.add("K1");
				} else {
					poorStuList.add("K2");
				}
				// 获取每类贫困等级的关联规则
				// 特困
				EntityWrapper<PoorAprioriRule> wrapper = new EntityWrapper<>();
				wrapper.eq("rule2", "[H1]");
				List<PoorAprioriRule> r1 = poorAprioriRuleMapper.selectList(wrapper);
				List<PoorAprioriRule> r2 = poorAprioriRuleMapper.selectList(
						new EntityWrapper<PoorAprioriRule>().eq("rule2", "[H2]")
				);;
				List<PoorAprioriRule> r3 = poorAprioriRuleMapper.selectList(
						new EntityWrapper<PoorAprioriRule>().eq("rule2", "[H3]")
				);
				if ((null != r1 && r1.size() > 0) && (null != r2 && r2.size() > 0) && (null != r3 && r3.size() > 0)) {
					boolean falg = false;
					// 找出符合特定贫困等级的学生，并设定该学生的贫困等级，若flag为true，则跳到上一层循环，否则继续执行下面的if语句
					for (PoorAprioriRule ar : r1) {
						String rank = ar.getRule1().replace(" ", "");
						String[] rankArr = rank.substring(1, rank.length() - 1).split(",");
						if (poorStuList.containsAll(Arrays.asList(rankArr))) {// Arrays.asList将数组改为列表
							p.setPoorRank("特困");
							poorStuForecastMapper.insert(p);
							System.out.println("特困生：" + p.getStuName() + ", 所属学院：" + p.getCollegeName());
							falg = true;
							break;
						}
					}
					if (falg) {
						continue;
					}
					for (PoorAprioriRule ar : r2) {
						String rank = ar.getRule1().replace(" ", "");
						String[] rankArr = rank.substring(1, rank.length() - 1).split(",");
						if (poorStuList.containsAll(Arrays.asList(rankArr))) {
							p.setPoorRank("困难");
							poorStuForecastMapper.updateById(p);
							System.out.println("困难生：" + p.getStuName() + ", 所属学院：" + p.getCollegeName());
							falg = true;
							break;
						}
					}
					if (falg) {
						continue;
					}

					for (PoorAprioriRule ar : r3) {
						String rank = ar.getRule1().replace(" ", "");
						String[] rankArr = rank.substring(1, rank.length() - 1).split(",");
						if (poorStuList.containsAll(Arrays.asList(rankArr))) {
							p.setPoorRank("一般");
							poorStuForecastMapper.updateById(p);
							System.out.println("一般：" + p.getStuName() + ", 所属学院：" + p.getCollegeName());
							falg = true;
							break;
						}
					}

					if (falg) {
						continue;
					}
					System.out.println("不是贫困生：" + p.getStuName() + ", 所属学院：" + p.getCollegeName());
				} else {
					System.out.println("关联规则不全！");
				}
			}
		} else {
			System.out.println("没有预测对象");
		}

		// 将数据进行统计存入数据库
		String[] collegeName = { "经管学院", "体教学院", "管理学院", "理学院", "化学学院", "生科学院", "食品学院", "材料学院", "环化学院", "机电学院", "建筑学院",
				"信息学院" };
		String[] zhuanye = { "经济学,国际经济与贸易,旅游管理", "教育学,体育教育,应用心理学", "管理科学,信息管理与信息系统,电子商务", "数学与应用数学,信息与计算科学,光电信息科学与工程",
				"化学,应用化学", "生物科学,生物技术,水产养殖学", "食品科学与工程,食品质量与安全,生物工程", "材料科学与工程,高分子材料与工程,新能源材料与器件",
				"过程装备与控制工程,化学工程与工艺,安全工程", "机械设计制造及其自动化,材料成型及控制工程,能源与动力工程", "土木工程,建筑学,工程管理",
				"测控技术与仪器,计算机科学与技术,物联网工程,数字媒体技术" };
		for (int i = 0; i < collegeName.length; i++) {
			int count1 = 0, count2 = 0, count3 = 0, count4 = 0;
			String[] major = zhuanye[i].split(",");
			for (int j = 0; j < major.length; j++) {
				ShowPoorForecast pf = new ShowPoorForecast();
				count1 = getRankCount(collegeName[i], major[j], "特困");
				count2 = getRankCount(collegeName[i], major[j], "困难");
				count3 = getRankCount(collegeName[i], major[j], "一般");
				count4 = count1 + count2 + count3;
				pf.setMajor(major[j]);// 专业
				pf.setColleague(collegeName[i]);// 学院
				pf.setTotal(count4);// 贫困生总人数
				pf.setNormal(count3);// 一般贫困生人数
				pf.setMedium(count2);// 困难贫困生人数
				pf.setPoorest(count1);// 特困贫困生人数
				showPoorForecastMapper.insert(pf);
			}
		}
	}

	/**
	 * 
	* 
	*  获取XX学院XX专业XX贫困等级的贫困生数量
	* @param @param c 学院
	* @param @param m 专业
	* @param @param poorRank  贫困等级
	* @param @return
	* @return int 数量
	* @throws
	* @author shuyy
	 */
	public int getRankCount(String c, String m, String poorRank) {
		
		EntityWrapper<PoorStuForecast> wrapper = new EntityWrapper<>();
		if (!StringUtils.isBlank(c)) {
			wrapper.eq("collegeName", c);
		}
		if (!StringUtils.isBlank(m)) {
			wrapper.eq("major", m);
		}
		if (!StringUtils.isBlank(poorRank)) {
			wrapper.eq("poorRank", poorRank);
		}
		try {
			
			int count = poorStuForecastMapper.selectCount(wrapper);
			return count;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * 
	* 计算所有学生消费的同比涨幅，按月和按年
	* @param @throws ParseException
	* @return void
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/findStuConsume", method = RequestMethod.POST)
	@ApiOperation(value="计算所有学生消费的同比涨幅，按月和按年", notes="计算所有学生消费的同比涨幅，按月和按年")
	public @ResponseBody void findStuConsume() throws ParseException {
		// 获取所有学生的消费记录
		List<ShowPoorAbnormalConsumption> allConsumeRecords = showPoorAbnormalConsumptionMapper.selectList(null);
		for (ShowPoorAbnormalConsumption a : allConsumeRecords) {
			int year = Integer.parseInt(a.getYyyymmdd().toString().split("-")[0]);
			int month = Integer.parseInt(a.getYyyymmdd().toString().split("-")[1]);
			List<ShowPoorAbnormalConsumption> yearNowrecords = null;
			List<ShowPoorAbnormalConsumption> yearLastrecords = null;
			EntityWrapper<ShowPoorAbnormalConsumption> wrapper  = new EntityWrapper<>();
			wrapper.eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , year + "%");
			// 查找本期数据
			yearNowrecords = showPoorAbnormalConsumptionMapper.selectList(wrapper);
			EntityWrapper<ShowPoorAbnormalConsumption> wrapperLastYear = new EntityWrapper<>();
			wrapperLastYear.eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , (year - 1) + "%");
			// 查找同期数据
			yearLastrecords = showPoorAbnormalConsumptionMapper.selectList(wrapperLastYear); 
			// 存放今年总消费金额
			float yearNowMoney = 0;
			for (ShowPoorAbnormalConsumption p : yearNowrecords) {
				yearNowMoney += p.getValue();
			}
			// 存放去年总消费金额
			float yearLastMoney = 0;
			for (ShowPoorAbnormalConsumption p : yearLastrecords) {
				yearLastMoney += p.getValue();
			}
			float yearRate = 0;			
			// 本期数据和同期数据有一个为0，则该条数据视为无效
			if (yearNowMoney == 0.0 || yearLastMoney == 0.0) {
				a.setYearRate("0");
				a.setYearPercent("0%");
			} else {
				// 本期大于同期则为上涨，反之为下降
				if (yearNowMoney > yearLastMoney) {
					// 计算同比涨幅，浮点型，方便排序
					yearRate = (yearNowMoney - yearLastMoney) / yearLastMoney;
					a.setYearRate(Float.toString(yearRate));
					DecimalFormat df = new DecimalFormat("#.00");
					df.setRoundingMode(RoundingMode.HALF_UP);
					// 计算同比涨幅，百分比，用于前台界面显示
					a.setYearPercent("+" + df.format(yearRate * 100) + "%");
				} else if (yearNowMoney < yearLastMoney) {
					yearRate = (yearLastMoney - yearNowMoney) / yearLastMoney;
					a.setYearRate(Float.toString(yearRate));
					DecimalFormat df = new DecimalFormat("#.00");
					df.setRoundingMode(RoundingMode.HALF_UP);
					a.setYearPercent("-" + df.format(yearRate * 100) + "%");
				} else {// 无涨幅
					a.setYearRate("0");
					a.setYearPercent("0%");
				}
			}
			List<ShowPoorAbnormalConsumption> monthNowrecords = null;
			List<ShowPoorAbnormalConsumption> monthLastrecords = null;
			if (month == 1) {// 如果为一月，则与去年的12月份比
				monthNowrecords = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper<ShowPoorAbnormalConsumption>().eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , year + 01 + "%")
				);
				monthLastrecords = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper<ShowPoorAbnormalConsumption>().eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , (year - 1) + 12 + "%")
				);
			} else {
				// 与上一个月相比
				monthLastrecords = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper<ShowPoorAbnormalConsumption>().eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , (year - 1) + 12 + "%")
				);
				String m = "";
				if(month < 10) {
					m = "0" + month;
				}
				monthNowrecords = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper<ShowPoorAbnormalConsumption>().eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , year + month + "%")
				);
				monthLastrecords = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper<ShowPoorAbnormalConsumption>().eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , year + month + "%")
						);
				if(month - 1 < 10) {
					m = "0" + (month - 1);
				}
				monthLastrecords = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper<ShowPoorAbnormalConsumption>().eq("studentID", a.getStudentID()).and("yyyymmdd like {0}" , year + m + "%")
						);
			}

			float monthNowMoney = 0;
			for (ShowPoorAbnormalConsumption p : monthNowrecords) {
				monthNowMoney += p.getValue();
			}
			float monthLastMoney = 0;
			for (ShowPoorAbnormalConsumption p : monthLastrecords) {
				monthLastMoney += p.getValue();
			}
			float monthRate = 0;			
			if (monthNowMoney == 0.0 || monthLastMoney == 0.0) {
				a.setMonthRate("0");
				a.setMonthPercent("0%");
			} else {
				if (monthNowMoney > monthLastMoney) {
					monthRate = (monthNowMoney - monthLastMoney) / monthLastMoney;
					a.setMonthRate(Float.toString(monthRate));
					DecimalFormat df1 = new DecimalFormat("#.00");
					df1.setRoundingMode(RoundingMode.HALF_UP);
					a.setMonthPercent("+" + df1.format(monthRate * 100) + "%");
				} else if (monthNowMoney < monthLastMoney) {
					monthRate = (monthLastMoney - monthNowMoney) / monthLastMoney;
					a.setMonthRate(Float.toString(monthRate));
					DecimalFormat df1 = new DecimalFormat("#.00");
					df1.setRoundingMode(RoundingMode.HALF_UP);
					a.setMonthPercent("-" + df1.format(monthRate * 100) + "%");
				} else {
					a.setMonthRate("0");
					a.setMonthPercent("0%");
				}
			}
			showPoorAbnormalConsumptionMapper.updateById(a);
		}
	}
}
