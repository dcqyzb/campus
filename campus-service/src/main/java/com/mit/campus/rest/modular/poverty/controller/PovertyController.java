package com.mit.campus.rest.modular.poverty.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.makeData.ConstansForData;
import com.mit.campus.rest.modular.poverty.model.CollegePay;
import com.mit.campus.rest.modular.poverty.model.PoorConsumpComare;
import com.mit.campus.rest.modular.poverty.model.PoorStuRecords;
import com.mit.campus.rest.modular.poverty.model.PoorStudentInfo;
import com.mit.campus.rest.modular.poverty.model.ShowPoorArea;
import com.mit.campus.rest.modular.poverty.model.ShowPoorCollegeAverage;
import com.mit.campus.rest.modular.poverty.model.ShowPoorComparison;
import com.mit.campus.rest.modular.poverty.model.ShowPoorForecast;
import com.mit.campus.rest.modular.poverty.model.ShowPoorNumber;
import com.mit.campus.rest.modular.poverty.model.TbNoticeInfo;
import com.mit.campus.rest.modular.poverty.service.ICollegepayService;
import com.mit.campus.rest.modular.poverty.service.IPoorAbnormalConsumptionService;
import com.mit.campus.rest.modular.poverty.service.IPoorConsumpComareService;
import com.mit.campus.rest.modular.poverty.service.IPoorStuForecastService;
import com.mit.campus.rest.modular.poverty.service.IPoorStuRecordsService;
import com.mit.campus.rest.modular.poverty.service.IPoorStudentInfoService;
import com.mit.campus.rest.modular.poverty.service.IShowPoorAbnormalConsumptionService;
import com.mit.campus.rest.modular.poverty.service.IShowPoorAreaService;
import com.mit.campus.rest.modular.poverty.service.IShowPoorCollegeAverageService;
import com.mit.campus.rest.modular.poverty.service.IShowPoorComparisonService;
import com.mit.campus.rest.modular.poverty.service.IShowPoorForecastService;
import com.mit.campus.rest.modular.poverty.service.IShowPoorNumberService;
import com.mit.campus.rest.modular.poverty.service.ITbNoticeInfoService;
import com.mit.campus.rest.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* 
* @author shuyy
* @date 2018年9月11日
* @company mitesofor
 */
@RestController
@Slf4j
@RequestMapping("poverty")
@Api(value="精准资助",tags={"精准资助"})
public class PovertyController {

	@Autowired
	private IShowPoorNumberService showPoorNumberService;
	
	@Autowired
	private IShowPoorComparisonService showPoorComparisonService;
	
	@Autowired
	private IShowPoorForecastService showPoorforecastService;
	
	@Autowired
	private IShowPoorCollegeAverageService showPoorCollegeAverAgeService;
	
	@Autowired
	private IShowPoorAbnormalConsumptionService showPoorAbnormalConsumptionService;
	
	@Autowired
	private IShowPoorAreaService showPoorAreaService;
	
	@Autowired
	private ITbNoticeInfoService tbNoticeInfoService;
	
	@Autowired
	private IPoorStudentInfoService poorstudentinfoServiceImpl;
	
	@Autowired
	private ICollegepayService collegepayService;
	
	@Autowired
	private IPoorStuForecastService poorStuForecastService;
	
	@Autowired
	private IPoorAbnormalConsumptionService poorAbnormalConsumptionService;
	
	@Autowired
	private IPoorConsumpComareService poorConsumpComareService;
	
	@Autowired
	private IPoorStuRecordsService PoorsturecordsService;
	
	/**
	* 获取各学院贫困生人数分布
	* @param @param year
	* @param @return
	* @return Result
	* @throws
	 */
	@RequestMapping(value = "/getPoorNumber", method=RequestMethod.GET)
	@ApiOperation(value="获取各学院贫困生人数分布", notes="获取各学院贫困生人数分布")
	public Result getPoorNumber(String year){
		if(StringUtils.isBlank(year)) {
			return Result.error("参数不能为空");
		}
		List<ShowPoorNumber> list = showPoorNumberService.selectByYear(year);
		if(!list.isEmpty()) {
			/* 列表数据转换
			 * 转换成json后数据格式：[{"name":学院名称>"value":[总人数, 特困,  贫困, 一般]}, ...]
			 * */
			JSONArray jsonArray = new JSONArray();
			for(int i=0, len = list.size(); i<len; i++){
				JSONObject jsonObject = new JSONObject();	
				ShowPoorNumber s = list.get(i);	
				int[] value = {s.getTotal(), s.getPoorest(), s.getMedium(), s.getNormal()};
				try {
					jsonObject.put("name", s.getColleague());
					jsonObject.put("value", value);
				} catch (JSONException e) {
					String errorMessage = "json数据转换失败";
					log.error(errorMessage, e);
					return Result.error(errorMessage);
				}
				jsonArray.add(jsonObject);
			}
			return Result.success(jsonArray);
		}else {
			return Result.success(null);
		}
	}
	/**
	 * 
	* 获取贫困生消费对比
	* @param @return
	* @return Result
	* @throws
	 */
	@RequestMapping(value = "/getPoorComparison", method=RequestMethod.GET)
	@ApiOperation(value="获取贫困生消费对比", notes="获取贫困生消费对比")
	public Result getPoorComparison() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = String.valueOf(Integer.parseInt(sdf.format(date)) - 1);
		List<ShowPoorComparison> list = showPoorComparisonService.findPoorComparisonByYear(year);
		if(!list.isEmpty()) {
			/* 列表数据转换
			 * 转换成json后数据格式：[{"name":<姓名,学院名称>,"value":[...]}, ...],value为1-12月的消费
			 * */
			JSONArray jsonArray = new JSONArray();
			for(int i=0, len = list.size(); i<len; i++){
				ShowPoorComparison s = list.get(i);	
				JSONObject jsonObject = new JSONObject();
				/*将字符串数组转化为整型数组*/
				String[] values = s.getConsvalues().split(",");
				List<Integer> v = new ArrayList<Integer>();
				for(String str : values){
					v.add(Integer.parseInt(str));
				}
				try {
					jsonObject.put("name", s.getStuname()+","+s.getCollege());
					jsonObject.put("value", v.toArray());
				} catch (JSONException e) {
					String errorMessage = "json数据转换失败";
					log.error(errorMessage, e);
					return Result.error(errorMessage);
				}
				jsonArray.add(jsonObject);
			}
			return Result.success(jsonArray.toArray());
		}else {
			return Result.success(null);
		}
	}
	
	/**
	 * 获取下阶段贫困生预测情况
	* @param @return
	* @return Result
	* @throws
	 */
	@ApiOperation(value="获取下阶段贫困生预测情况", notes="获取下阶段贫困生预测情况")
	@RequestMapping(value = "/getPoorForecast", method=RequestMethod.GET)
	public Result getPoorForecast(){
		List<ShowPoorForecast> list = showPoorforecastService.findPoorForecast();
		return Result.success(list);
	}
	
	@RequestMapping(value = "/getCollegeAverage")
	public Result getCollegeAverage(String time){
		if(!isDateType(time)){
			return Result.error("日期不符合格式");
		}
		//判断是年还是月
		boolean isYear = (time.indexOf("-") == -1);	
		if(isYear){
			List<Integer> v = showPoorCollegeAverAgeService.findCollegeAverAge(time);
			if(v != null){
				return Result.success(v);
			}
		}else{
			String[] times = time.split("-");
			ShowPoorCollegeAverage ca = showPoorCollegeAverAgeService.findCollegeAverage(times[0], times[1]);
			if(ca != null){
				/*将字符串数组转化为整型数组*/
				String[] values = ca.getConsvalues().split(",");
				List<Integer> v = new ArrayList<Integer>();
				for(String s : values){
					v.add(Integer.parseInt(s));
				}
				return Result.success(v);
			}
		}
		return Result.success(null);
	}
	
	/**
	* 获取异常消费统计
	* @param @param time
	* @param @param num 前几位
	* @param @return
	* @return Result
	* @throws
	 */
	@RequestMapping(value = "/getAbnormalConsumption", method=RequestMethod.GET)
	@ApiOperation(value="获取异常消费统计", notes="获取异常消费统计")
	public Result getAbnormalConsumption(String time, int num){
		if(!isDateType(time) || num < 0 ){
			return Result.error("参数不符合格式");
		}
		// 判断请求的日期是否是以年为单位
		boolean isYear = (time.indexOf("-") == -1);
		//获取数据，格式为<消费类型,<学生姓名+班级,<月份,[消费值,年增长率,月增长率]>>> 
		Map<String, Object> map = null;
		if(isYear){
			map = showPoorAbnormalConsumptionService.findAbnormalConsumption(time, num);
		}else{
			String[] times = time.split("-");
			map = showPoorAbnormalConsumptionService.findAbnormalConsumption(times[0], times[1], num);
		}
		// 转换成json格式
		if(map != null){
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = null;
			Set<String> set = map.keySet();
			Iterator<String> ite = set.iterator();
			// 对每一种消费类型
			while(ite.hasNext()){	
				String type = ite.next();
				jsonObject = new JSONObject();
				// 消费类型
				jsonObject.put("type", type);	

				JSONArray jsonArray2 = new JSONArray();
				JSONObject jsonObject2 = null;
				Map<String, Object> map2 = (Map<String, Object>) map.get(type);
				Set<String> set2 = map2.keySet();
				Iterator<String> ite2 = set2.iterator();
				while(ite2.hasNext()){	
					// 学生姓名+班级
					String stuName = ite2.next();	
					jsonObject2 = new JSONObject();
					jsonObject2.put("name", stuName);

					List<Object[]> value = new ArrayList<Object[]>();
					Map<Integer, String[]> map3 = (Map<Integer, String[]>) map2.get(stuName);
					Set<Integer> set3 = map3.keySet();
					Iterator<Integer> ite3 = set3.iterator();
					while(ite3.hasNext()){	
						int month = ite3.next();	
						// [消费值，年增长率，月增长率]
						String[] strs = map3.get(month);	
						Object[] v = {month, Integer.parseInt(strs[0]), strs[1], strs[2]};
						value.add(v);
					}
					jsonObject2.put("value", value.toArray());
					jsonArray2.add(jsonObject2);
				}
				jsonObject.put("data", jsonArray2);
				jsonArray.add(jsonObject);
			}
			return Result.success(jsonArray);
		}
		return Result.success(null);
	}
	
	/**
	 * 
	* 
	* 获取各地区贫困生人数分布，默认获取今年
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getPoorArea", method=RequestMethod.GET)
	@ApiOperation(value="获取各地区贫困生人数分布，默认获取今年", notes="获取各地区贫困生人数分布，默认获取今年")
	public Result getPoorArea(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(date);
		List<ShowPoorArea> list = showPoorAreaService.findPoorArea(year,false);
		if(null != list && !list.isEmpty()){
			/* 列表数据转换
			 * 转换成json后数据格式：[{"name":地区名称,"value":[总人数,特困,贫困,一般]}, ...]
			 * */
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = null;
			for(int i=0, len = list.size(); i<len; i++){
				// 该年某学院贫困生人数情况	
				ShowPoorArea s = list.get(i);			
				jsonObject = new JSONObject();				
				int[] value = {s.getTotal(), s.getPoorest(), s.getMedium(), s.getNormal()};
				try {
					jsonObject.put("name", s.getProvince());
					jsonObject.put("value", value);
				} catch (JSONException e) {
					String err = "json数据转换失败";
					log.error(err, e);
					return Result.error(err);
				}
				jsonArray.add(jsonObject);
			}
			return Result.success(jsonArray);
		}
		return Result.success(null);
	}
	
	/**
	 * 判断字符串是否是yyyy或yyyy-MM格式
	 */
	private boolean isDateType(String time){
		if(null == time || "".equals(time.trim())){
			return false;
		}
		boolean isYear = (time.indexOf("-") == -1);
		/*验证日期的格式有效*/
		SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf_month = new SimpleDateFormat("yyyy-MM");
		try {
			Date d = isYear ? sdf_year.parse(time) : sdf_month.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	* 获取学院专业 
	* @param @return
	* @return Result
	* @throws
	 */
	@RequestMapping(value = "/getCollegeMajor", method=RequestMethod.GET)
	@ApiOperation(value="获取学院专业", notes="获取学院专业")
	public Result getCollegeMajor(){
		// 学院
		String[] colleges = ConstansForData.xueyuan.split(",");	
		// 专业
		String[] majors = ConstansForData.zhuanye;	
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		// 学院对应的各个专业
		List<String> majorlist = null;	
		for(int i=0, len=colleges.length; i<len; i++) {
			// 学院名称
			String collegeName = colleges[i];	
			if(map.containsKey(collegeName)){
				majorlist = map.get(collegeName);
			}else{
				majorlist = new ArrayList<String>();
			}
			// 某学院的各个专业
			String[] subzhuanye = majors[i].split(",");	
			for(int j=0, len2=subzhuanye.length; j<len2; j++) {
				String[] xyxx = subzhuanye[j].split("\\|");
				majorlist.add(xyxx[0]);
			}
			map.put(collegeName, majorlist);
		}
		return Result.success(map);
	}	
	/**
	 * 
	* top5-最新的5条通知通告
	* @param @param belong
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/findTopNotice", method=RequestMethod.GET)
	@ApiOperation(value="最新的5条通知通告", notes="最新的5条通知通告")
	public Result findTopNotice(String belong){
		List<TbNoticeInfo> list = tbNoticeInfoService.findTopNotice(belong);
		if(!list.isEmpty()){
			return Result.success(list);
		}
		return Result.error("无数据");
	}
	
	/**
	 * 
	* 二级页面-贫困生人数分布查询
	* @param @param collegeName
	* @param @param major
	* @param @param poorRank
	* @param @param startYear
	* @param @param endYear
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Result
	* @throws
	 */
	@RequestMapping(value = "/poorStudentNum", method=RequestMethod.GET)
	@ApiOperation(value="贫困生人数分布查询", notes="贫困生人数分布查询")
	public Result getPoorNumberData(String collegeName, String major, 
			String poorRank,String startYear,String endYear, int pageNum,int pageSize){
		//先按条件查询
		Page obj = poorstudentinfoServiceImpl.getPoorNum(collegeName, major, poorRank, startYear, endYear, pageNum,pageSize);
		//查询结果
		return Result.success(obj, "查询完成");
	}
	
	/**]
	 * 
	* 二级页面-贫困生人数分布top10
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getTopNum", method = RequestMethod.GET)
	@ApiOperation(value="贫困生人数分布top10", notes="贫困生人数分布top10")
	public Result getTopNum(){
		//过去一年  
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Calendar c = Calendar.getInstance();  
		c.setTime(new Date());  
		c.add(Calendar.YEAR, -1);  
		Date y = c.getTime();  
		 //上一年
		String year = format.format(y); 
		/*请求参数验证*/
		if(!StringUtils.isBlank( year)){
			//获取某年贫困生人数分布情况top10
			List<ShowPoorNumber> list = showPoorNumberService.findPoorNumber(year,true);
			return Result.success(list);
		}else {
			String err = "参数不能为空";
			log.error(err);
			return Result.error(err);
		}
		
	}
	
	
	/**
	 * 
	* 二级页面-分页查询贫困生生源地分布
	* @param @param birthPlace
	* @param @param college
	* @param @param startYear
	* @param @param endYear
	* @param @param poorRank
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@ApiOperation(value="分页查询贫困生生源地分布", notes="分页查询贫困生生源地分布")
	@RequestMapping(value = "/poorAreaPage", method = RequestMethod.GET)
	public Result getPoorAreaData(String birthPlace, String college, String startYear,String endYear, String poorRank,
			int pageNum,int pageSize){
		//先按条件查询
		Page obj = poorstudentinfoServiceImpl.getPoorArea(birthPlace, college, startYear,endYear, poorRank, pageNum, pageSize);
		//查询结果
		return Result.success(obj);
	}
	
	/**
	 * 
	* 二级页面-贫困生地区分布top10
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getTopArea", method=RequestMethod.GET)
	@ApiOperation(value="贫困生地区分布top10", notes="贫困生地区分布top10")
	public Result getTopArea(){
		//过去一年  
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());  
		c.add(Calendar.YEAR, -1);  
		Date y = c.getTime();  
		String year = format.format(y);  //上一年
		//查询贫困生人数最多的十个地区
		List<ShowPoorArea> list = showPoorAreaService.findPoorArea(year,true);
		if(null != list && !list.isEmpty()){
			return Result.success(list);
		}
		return Result.error("查询失败");
	}
	
	/**
	 * 
	* 二级页面-分页查询学院平均消费
	* @param @param collegeName
	* @param @param startYear
	* @param @param endYear
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return ResultObject
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/collegeAveragePage", method=RequestMethod.GET)
	@ApiOperation(value="分页查询学院平均消费", notes="分页查询学院平均消费")
	public Result getCollegeAverageData(String collegeName, String startYear,String endYear, int pageNum,int pageSize){
		//先按条件查询
		Page obj = collegepayService.getCollegeAverage(collegeName, startYear, endYear, pageNum, pageSize);
		return Result.success(obj);
	}
	
	/**
	 * 
	* 
	* 二级页面-分页查询贫困生预测名单
	* @param @param collegeName
	* @param @param major
	* @param @param poorRank
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/poorForecastPage", method=RequestMethod.GET)
	@ApiOperation(value="分页查询贫困生预测名单", notes="分页查询贫困生预测名单")
	public Result getPoorForecastData(String collegeName, String major,
			String poorRank, int pageNum,int pageSize){
		//先按条件查询
		Page obj = poorStuForecastService.getPoorForecast(collegeName, major, poorRank, pageNum,pageSize);
		return Result.success(obj);
	}
	
	/**、
	 * 
	* top10-获取下阶段贫困生预测最多的前十个学院
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getTopForeast", method=RequestMethod.GET)
	@ApiOperation(value="top10-获取下阶段贫困生预测最多的前十个学院", notes="top10-获取下阶段贫困生预测最多的前十个学院")
	public Result getTopForeast(){
		//查询贫困生预测人数最多的十个学院
		List<Map<String, Object>> result = showPoorforecastService.getTopForeast();
		return Result.success(result);
	}
	
	
	/**
	 * 
	* 二级页面-分页查询贫困生异常消费情况
	* @param @param college
	* @param @param startYear
	* @param @param endYear
	* @param @param type
	* @param @param stuname
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return ResultObject
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/abnormalConsumptionPage", method=RequestMethod.GET)
	@ApiOperation(value="分页查询贫困生异常消费情况", notes="分页查询贫困生异常消费情况")
	public Result getAbnormalConsumptionData(String college, String startYear,String endYear, String type, String stuname,
			int pageNum, int pageSize){
		//先按条件查询
		Page obj = poorAbnormalConsumptionService.getAbnormalConsumption(college, startYear, endYear, type,stuname, pageNum, pageSize);
		return Result.success(obj);
	}
	
	/**
	 * 
	* top10-查询贫困生异常消费人数最多的十个学院
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getTopAbdCon", method=RequestMethod.GET)
	@ApiOperation(value="top10-查询贫困生异常消费人数最多的十个学院", notes="top10-查询贫困生异常消费人数最多的十个学院")
	public Result getTopAbdConsumption(){
		//过去一年  
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());  
		c.add(Calendar.YEAR, -1);  
		Date y = c.getTime();  
		String year = format.format(y);  //上一年
		//查询贫困生异常消费人数最多的十个学院
		List<Map<String, Object>> list = poorAbnormalConsumptionService.getTopAbnCon(year);
		return Result.success(list);
	}
	
	/**
	 * 
	* 
	* 二级页面-分页查询贫困生消费对比
	* @param @param college
	* @param @param major
	* @param @param poorType
	* @param @param increase
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/poorComparisonPage", method=RequestMethod.GET)
	@ApiOperation(value="分页查询贫困生消费对比", notes="分页查询贫困生消费对比")
	public Result getPoorComparisonData(String college, String major, String poorType,String increase, 
			int pageNum, int pageSize){
		//先按条件查询
		Page obj = poorConsumpComareService.getPoorComparison(college, major, poorType,increase,pageNum, pageSize);
		return Result.success(obj);
	}
	
	/**
	 * 
	* 贫困生消费对比涨幅大的前10名
	* @param @param increase
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value="/findPoorConsumpTop10", method=RequestMethod.GET)
	@ApiOperation(value="贫困生消费对比涨幅大的前10名", notes="贫困生消费对比涨幅大的前10名")
	public Result findPoorConsumpTop10(String increase){
		/*请求参数验证，默认正增长*/
		if(StringUtils.isBlank(increase)){
			increase = "正";
		}
		List<PoorConsumpComare> list= poorConsumpComareService.getPoorConsumpTop10(increase);
		return Result.success(list);
	}
	
	/**
	 * 
	* 
	* 根据主键查找贫困生
	* @param @param uuid
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getPoorById", method = RequestMethod.GET)
	@ApiOperation(value="根据主键查找贫困生", notes="根据主键查找贫困生")
	public Result getPoorStudentById(String uuid){
		//查找
		EntityWrapper<PoorStudentInfo> wrapper = new EntityWrapper<>();
		wrapper.eq("uuid", uuid);
		PoorStudentInfo e = poorstudentinfoServiceImpl.selectOne(wrapper);
		if(e != null){
			return Result.success(e, "查找成功");
		}
		return Result.error("查找失败");
	}
	
	/**
	 * 
	* 
	* 查询某学院某一年十二个月的各项消费金额
	* @param @param collegeName
	* @param @param year
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getCollegeMonths", method=RequestMethod.GET)
	@ApiOperation(value="查询某学院某一年十二个月的各项消费金额", notes="查询某学院某一年十二个月的各项消费金额")
	public Result getCollegeConsumpOfMonths(String collegeName, String year){
		List<CollegePay> list = collegepayService.getByCollegeAndYear(collegeName, year);
		return Result.success(list);
	}
	
	/**
	 * 
	* 查询某学生的资助记录
	* @param @param stuName
	* @param @param stuId
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getFundingRecords", method=RequestMethod.GET)
	@ApiOperation(value="查询某学生的资助记录", notes="查询某学生的资助记录")
	public Result getByStuNameAndStuId(String stuName, String stuId){
		List<PoorStuRecords> list = PoorsturecordsService.getByStuNameAndStuId(stuName, stuId);
		return Result.success(list);
	}
	
	/**
	 * 
	* 按学号查找学生在评定时间年份的所有月消费记录
	* @param @param studentID
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value="/findStuMonthConsumpRecord", method=RequestMethod.GET)
	@ApiOperation(value="按学号查找学生在评定时间年份的所有月消费记录", notes="按学号查找学生在评定时间年份的所有月消费记录")
	public Result findStuMonthConsumpRecord(String studentID){
		/*请求参数验证*/
		if(null == studentID || "".equals(studentID.trim())){
			return Result.error(null);
		}
		List<PoorConsumpComare> list= poorConsumpComareService.findStuMonthConsumpRecord(studentID);
		if(!list.isEmpty()){
			/* 列表数据转换
			 * 转换成json后数据格式：[{"name":<姓名,学院名称>,"value":[...]}, ...],value为1-12月的消费
			 * */
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = null;
			PoorConsumpComare s = new PoorConsumpComare();
			List<Integer> v = new ArrayList<Integer>();	
			for(int i=0, len = list.size(); i<len; i++){
				//该年某学生消费		
				s = list.get(i);	
				jsonObject = new JSONObject();
				//获取月消费，添加到数组中			
				v.add(Integer.parseInt(s.getMoneyAmount()));
			}
			try {
				jsonObject.put("name", s.getStuName()+","+s.getCollege());
				jsonObject.put("value", v.toArray());
			} catch (JSONException e) {
				String err = "json数据转换失败";
				log.error(err, e);
				return Result.error(err);
			}
			jsonArray.add(jsonObject);
			return Result.success(jsonArray);
		}
		return Result.error("参数错误");
	}
}