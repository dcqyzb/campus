package com.mit.campus.rest.modular.electricalBehavior.controller;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormHighPower;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormSteal;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormUnclose;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricHighPower;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricSteal;
import com.mit.campus.rest.modular.electricalBehavior.model.ShowElectricUnclosedRank;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeDormHighPowerService;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeDormStealService;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeDormUncloseService;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeElecService;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricDormInfoService;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricDormRankService;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricHighPowerService;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricStealService;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricTrendService;
import com.mit.campus.rest.modular.electricalBehavior.service.IShowElectricUnclosedRankService;
import com.mit.campus.rest.modular.enrolldecision.model.StudentInfo;
import com.mit.campus.rest.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* 用电行为
* @author shuyy
* @date 2018年9月11日
* @company mitesofor
 */
@RestController
@Slf4j
@RequestMapping("electricalBehavior")
@Api(value="用电行为",tags={"用电行为"})
public class ElectricalBehaviorController {

	@Autowired
	private IShowElectricDormRankService showElectricDormRankService;
	
	@Autowired
	private IShowElectricStealService showElectricStealService;
	
	@Autowired
	private IShowElectricTrendService showElectricTrendService;
	
	@Autowired
	private IShowElectricHighPowerService showElectricHighPowerService;
	
	@Autowired
	private IShowElectricUnclosedRankService showElectricUnclosedRankService;
	
	@Autowired
	private IElectricCollegeElecService electricCollegeElecService;
	
	@Autowired
	private IElectricDormInfoService electricDormInfoService;
	
	@Autowired
	private IElectricCollegeDormStealService electricCollegeDormStealService;
	
	@Autowired
	private IElectricCollegeDormHighPowerService electricCollegeDormHighPowerService;
	
	@Autowired
	private IElectricCollegeDormUncloseService ElectricCollegeDormUncloseService;
	
	/**
	 * 
	* 绿色宿舍排行榜
	* @param @return
	* @return ResultObject
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getDormitoryRank", method=RequestMethod.GET)
	@ApiOperation(value="绿色宿舍排行榜", notes="绿色宿舍排行榜")
	public Result getDormitoryRank() {
		return Result.success(showElectricDormRankService.findDormitoryRank());
	}
	
	/**
	 * 
	* 
	* 获取某栋当前月的上一个月窃电嫌疑度高的前4，近两年用电变化
	* @param @param position
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getStealElectricTop4", method=RequestMethod.GET)
	@ApiOperation(value="获取某栋当前月的上一个月窃电嫌疑度高的前4，近两年用电变化", notes="获取某栋当前月的上一个月窃电嫌疑度高的前4，近两年用电变化")
	public Result getStealElectricTop4(String position) {
		//排空处理
		if(StringUtils.isBlank(position)){
			return Result.error("参数错误");
		}
		//获取前4个宿舍的记录
		List<List<ShowElectricSteal>> list = showElectricStealService.findStealElectricTop4(position);
		if(list.isEmpty()){
			return Result.error(null);
		}
		ShowElectricSteal record  = null;
		List<ShowElectricSteal> one_room = null;
		//{[日期，用电量，宿舍号]...}
		List<String[]> str_list = null;
		//返回结果  name:宿舍号，
		//		value:{[日期，用电量，宿舍号]..}
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		//遍历4个宿舍记录
		for(int i = 0;i<4;i++){
			str_list = new ArrayList<String[]>();
			one_room = list.get(i);
			//遍历一个宿舍的所有用电记录，[日期，用电量，宿舍号]
			String dorm_id = one_room.get(0).getDormitoryID();
			for(int j = 0;j<24;j++){ 
				record = one_room.get(j);
				String[] str = {record.getDate(),record.getElectricity()+"",dorm_id};
				str_list.add(str);
			}
			jsonObject = new JSONObject();	
			try {
				jsonObject.put("name", dorm_id);
				jsonObject.put("value", str_list);
			} catch (JSONException e) {
				String err = "json数据转换失败";
				log.error(err, e);;
				return Result.error(null);
			}
			jsonArray.add(jsonObject);
		}
		return Result.success(jsonArray);
	}
	
	/**
	 * 
	* 
	* 获取某年月学院用电记录
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAcademyElectric", method = RequestMethod.GET)
	@ApiOperation(value="获取某年月学院用电记录", notes="获取某年月学院用电记录")
	public Result getAcademyElectric(String date) {
		if(StringUtils.isBlank(date)){
			return Result.error("参数为空不合理");
		}
		// 判断请求的日期是否是以年为单位
		boolean isYear = (date.indexOf("-") == -1);	
		Map<String, Object> map = null;
		String time = "";
		if(isYear){
			time = date;
		}else{
			//对月份进行处理，小于10的前面加0
			String[] times = date.split("-");
			String month = times[1];
			month = (Integer.parseInt(month) > 9) ? month : (month.indexOf("0") != -1) ? month : "0"+month;
			time = times[0]+"-"+month;
		}
		map = showElectricTrendService.findAcademyElectricMap(time);
		if(map != null){
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = null;
			Set<String> set = map.keySet();
			Iterator<String> ite = set.iterator();
			while(ite.hasNext()){
				String college = ite.next();	//学院名称
				jsonObject = new JSONObject();
				jsonObject.put("name", college);

				List<Object[]> value = new ArrayList<Object[]>();	//各日期下的用电转换为list
				Map<Integer, Float> map2 = (Map<Integer, Float>) map.get(college);	//该学院下对应日期的用电
				Set<Integer> set2 = map2.keySet();
				Iterator<Integer> ite2 = set2.iterator();
				while(ite2.hasNext()){
					Object[] o = new Object[2];
					int d = ite2.next();
					o[0] = d;
					o[1] = map2.get(d);
					value.add(o);
				}
				jsonObject.put("data", value.toArray());
				jsonArray.add(jsonObject);
			}
			return Result.success(jsonArray);
		}
		return Result.success(null);
	}
	
	/**
	 * 
	* 
	* 按月按年查找所有楼栋违章电器使用嫌疑度
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getDormHighPower", method = RequestMethod.GET)
	@ApiOperation(value="按月按年查找所有楼栋违章电器使用嫌疑度", notes="按月按年查找所有楼栋违章电器使用嫌疑度")
	public Result getDormHighPower(String date) {
		/*请求参数验证*/	
		if(StringUtils.isBlank(date)){
			return Result.error("参数不能为空");
		}
		ShowElectricHighPower record = null; 
		//判断请求的日期是否是以年为单位
		boolean isYear = (date.indexOf("-") == -1);	
		if(isYear){
			List<ShowElectricHighPower> list = showElectricHighPowerService.findDormHighPowerByYear(date);
			if(list.isEmpty()) {
				return Result.success(null);
			}
			//将宿舍号存入HashSet
			HashSet<String> dorms = new HashSet<>();
			for(int k=0, len = list.size(); k<len; k++){
				dorms.add(list.get(k).getDormitoryID());
			}
			List<ShowElectricHighPower> highpowerlist = new ArrayList<ShowElectricHighPower>(); 
			//遍历每个宿舍号，求得年平均违章嫌疑度
			for(String doimnane : dorms) {
				ShowElectricHighPower highpower = new ShowElectricHighPower();
				highpower.setDormitoryID(doimnane);
				highpower.setDate(date);
				float susp = 0;
				//月份数
				float num = 0; 
				for(int m=0, len = list.size(); m<len; m++){
					if(list.get(m).getDormitoryID().equals(doimnane)) {
						susp = susp + list.get(m).getHighPower();
						num++;
					}
				}
				//年平均违章嫌疑度
				highpower.setHighPower(susp/num);
				highpowerlist.add(highpower);
			}
			//返回{[1栋，0.44,1-0101]}
			List<String[]> str_list = new ArrayList<String[]>();
			for(int i = 0, len =highpowerlist.size();i< len;i++){
				//获取每个宿舍记录
				record = highpowerlist.get(i);
				String[] str = {record.getDormitoryID().split("-")[0]+"栋",
								record.getHighPower()*100+"",
								record.getDormitoryID(),
								record.getDate()};
				str_list.add(str);
			}
			return Result.success(str_list);
		}
		else{
			List<ShowElectricHighPower> list = showElectricHighPowerService.findDormHighPower(date);
			if(list.isEmpty()){
				return Result.success(null);
			}
			//返回{[1栋，0.44,1-0101]}
			List<String[]> str_list = new ArrayList<String[]>();
			for(int i = 0, len =list.size();i< len;i++){
				//获取每个宿舍记录
				record = list.get(i);
				String[] str = {record.getDormitoryID().split("-")[0]+"栋",
								record.getHighPower()*100+"",
								record.getDormitoryID(),
								record.getDate()};
				str_list.add(str);
			}
			return Result.success(str_list);
		}
		
	}
	
	/**
	 * 
	* 
	* 按时间获取未关电器占比
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getUnclosedRank", method = RequestMethod.GET)
	@ApiOperation(value="按时间获取未关电器占比", notes="按时间获取未关电器占比")
	public Result getUnclosedRank(String date) {
		/*请求参数验证*/	
		if(StringUtils.isBlank(date)){
			return Result.error("日期不符合格式");
		}
		//判断请求的日期是否是以年为单位
		boolean isYear = (date.indexOf("-") == -1);
		if(isYear){
			List<ShowElectricUnclosedRank> list = showElectricUnclosedRankService.findUnclosedRank(date);
			if(!list.isEmpty()){
				/* 列表数据转换
				 * 转换成json后数据格式：[{"name":<宿舍号>,"value":[次数，日期],[次数，日期]}, ...]
				 * */
				HashSet<String> dorms = new HashSet<>();
				for(int k=0, len = list.size(); k<len; k++){
					dorms.add(list.get(k).getDormitory());
				}
				List<ShowElectricUnclosedRank> showdomlist = new ArrayList<ShowElectricUnclosedRank>(); 
				//遍历每个宿舍号，求得年未关电器数
				for(String doimnane : dorms) {
					ShowElectricUnclosedRank showedom = new ShowElectricUnclosedRank();
					showedom.setDormitory(doimnane);
					showedom.setUnclosenumbers(0);
					showedom.setYyyymm(date);
					int num = 0;
					for(int m=0, len = list.size(); m<len; m++){
						if(list.get(m).getDormitory().equals(doimnane)) {
							num = num + list.get(m).getUnclosenumbers();
						}
					}
				    showedom.setUnclosenumbers(num);
				    showdomlist.add(showedom);
				}
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = null;
				for(int i=0, len = showdomlist.size(); i<len; i++){
					ShowElectricUnclosedRank s = showdomlist.get(i);		
					jsonObject = new JSONObject();				
					String[] value = { s.getUnclosenumbers()+"",s.getYyyymm()};
					try {
						jsonObject.put("name", s.getDormitory());
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
		}else{   //按月显示
			String[] times = date.split("-");			
			List<ShowElectricUnclosedRank> list = showElectricUnclosedRankService.findUnclosedRank(times[0], times[1]);;
			if(null != list && list.size()>0){
				//设置json
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = null;
				for(int i=0, len = list.size(); i<len; i++){
					ShowElectricUnclosedRank s = list.get(i);			
					jsonObject = new JSONObject();	
					//{次数，月份}
					String[] value = {s.getUnclosenumbers()+"",times[1] };
					try {
						jsonObject.put("name", s.getDormitory());
						jsonObject.put("value", value);
					} catch (JSONException e) {
						String err = "json数据转换失败";
						return Result.error(err);
					}
					jsonArray.add(jsonObject);
				}
				return Result.success(jsonArray);
			}			
		}
		return Result.success(null);
	}
	
	/**
	 * 
	* 
	* 二级页面-分页查询学院月用电量
	* @param @param collegeName
	* @param @param startMonth
	* @param @param endMonth
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getCollegeElecByCondition", method = RequestMethod.GET)
	@ApiOperation(value="分页查询学院月用电量", notes="分页查询学院月用电量")
	public Result getCollegeElecByCondition(String collegeName, 
			String startMonth,String endMonth, 
			int pageNum,int pageSize) {
		//先按条件查询
		Page obj = electricCollegeElecService.getCollegeElecByCondition(collegeName, startMonth, endMonth, pageNum, pageSize);
		return Result.success(obj);
	}
	
	/**
	 * 
	* 二级页面-条件查询寝室用电信息
	* @param @param collegeName
	* @param @param dormName
	* @param @param roomName
	* @param @param startMonth
	* @param @param endMonth
	* @param @param belong
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/findElectricByConition", method = RequestMethod.GET)
	@ApiOperation(value="条件查询寝室用电信息", notes="条件查询寝室用电信息")
	public Result findElectricByConition(String collegeName,String dormName,String roomName,
			String startMonth,String endMonth,String belong,
			int pageNum,int pageSize) {
		//先按条件查询
		Page obj = electricDormInfoService.findElectricByConition(collegeName, dormName, roomName, startMonth, endMonth, belong, pageNum, pageSize);
		return Result.success(obj);
	}
	
	/**
	 * 
	* 
	* 获取该宿舍某时间的学生详情
	* @param @param dormID
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getDormStudents", method=RequestMethod.GET)
	@ApiOperation(value="获取该宿舍某时间的学生详情", notes="获取该宿舍某时间的学生详情")
	public Result getDormStudents(String dormID,String date) {
		//先按条件查询
		List<StudentInfo> obj = electricDormInfoService.getDormStudents(dormID, date);
		return Result.success(obj, "查询完成");
	}
	
	/**
	 * 
	* 按宿舍时间查询该宿舍历史窃电详情
	* @param @param dormId
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getDormStealHistory", method = RequestMethod.GET)
	@ApiOperation(value="按宿舍时间查询该宿舍历史窃电详情", notes="按宿舍时间查询该宿舍历史窃电详情")
	public Result getDormStealHistory(String dormId,String date) {
		List<ElectricCollegeDormSteal> list = electricCollegeDormStealService.getDormStealHistory(dormId, date);
		return Result.success(list);
	}
	
	
	/**
	 * 
	* 
	* 按宿舍时间查询该宿舍历史违章详情
	* @param @param dormId
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getDormHighPowerHistory", method = RequestMethod.GET)
	@ApiOperation(value="按宿舍时间查询该宿舍历史违章详情", notes="按宿舍时间查询该宿舍历史违章详情")
	public Result getDormHighPowerHistory(String dormId,String date) {
		//先按条件查询
		List<ElectricCollegeDormHighPower> list = electricCollegeDormHighPowerService.getDormHighPowerHistory(dormId, date);
		return Result.success(list);
	}
	
	/**
	 * 
	* 
	* 获取宿舍该月未关电器记录
	* @param @param dormID
	* @param @param date
	* @param @return
	* @return Result
	* @throws
	* @author shuyy
	 */
	@RequestMapping(value = "/getUncloseDetail", method = RequestMethod.GET)
	@ApiOperation(value="获取宿舍该月未关电器记录", notes="获取宿舍该月未关电器记录")
	public Result getUncloseDetail(String dormID,String date) {
		//先按条件查询
		List<List<ElectricCollegeDormUnclose>> obj = ElectricCollegeDormUncloseService.getUncloseDetail(dormID, date);
		if(obj.isEmpty()){
			return Result.error("查询为空");
		}
		//为了获取不重复的时间的数组
		List<ElectricCollegeDormUnclose> date_list = obj.get(0);
		//所有未关电器记录
		List<ElectricCollegeDormUnclose> list = obj.get(1);
		//删除date_list中相同天的元素
		for(int i = 0 ; i < date_list.size() - 1 ; i++ )  {  
			String day_i = date_list.get(i).getDate().split(" ")[0];
			for(int  j  =  date_list.size() - 1 ; j > i; j-- )  {     
				String day_j = date_list.get(j).getDate().split(" ")[0];
				if(day_i.equals(day_j)){       
					date_list.remove(j);       
				}        
			}        
		}  
		/*下面将相同日期的不同时间段找出
		      返回结果  name:2017-12-01，
				value:8:00:00~12:00:00,....} */
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		//将相同日期的记录放在一起
		for(ElectricCollegeDormUnclose item : date_list) {
			String day = item.getDate().split(" ")[0];
			String time_str = "";
			for(int m=0, len = list.size(); m<len; m++){
				ElectricCollegeDormUnclose unclose = list.get(m);
				if(unclose.getDate().indexOf(day) != -1) {
					time_str +=unclose.getDate().split(" ")[1]+",";
				}
			}
			jsonObject = new JSONObject();	
			try {
				jsonObject.put("name", day);
				jsonObject.put("value", time_str);
			} catch (JSONException e) {
				String err = "json数据转换失败";
				log.error(err, e);
				return Result.error(err);
			}
			jsonArray.add(jsonObject);
		
		}
		return Result.success(jsonArray);
	}
}
