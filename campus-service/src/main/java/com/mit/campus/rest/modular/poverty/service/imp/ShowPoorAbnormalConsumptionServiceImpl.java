package com.mit.campus.rest.modular.poverty.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorAbnormalConsumptionMapper;
import com.mit.campus.rest.modular.poverty.model.AbnormalConsumeModel;
import com.mit.campus.rest.modular.poverty.model.ShowPoorAbnormalConsumption;
import com.mit.campus.rest.modular.poverty.service.IShowPoorAbnormalConsumptionService;

/**
 * 
* 贫困生异常消费
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class ShowPoorAbnormalConsumptionServiceImpl extends ServiceImpl<ShowPoorAbnormalConsumptionMapper, ShowPoorAbnormalConsumption> implements IShowPoorAbnormalConsumptionService {
	
	@Autowired
	private ShowPoorAbnormalConsumptionMapper showPoorAbnormalConsumptionMapper;
	
	
	/* 
	 * 获取异常消费前几名，按年
	 */
	@Override
	public Map<String, Object> findAbnormalConsumption(String year, int num) {
		/*
		 * 返回的最终数据格式为：消费类型,学生姓名+班级,月份,[消费值,年增长率,月增长率] 
		 */
		if(StringUtils.isEmpty(year)){
			return null;
		}
		//查询到该年的异常消费记录前几名，包含所有类型、学生
		List<ShowPoorAbnormalConsumption> list = getAbnormalConsumeStu(year, "", num);		
		if(null != list && list.size()>0){
			return convertAbnormalList(list, true);			
		}
		return null;
	}
	
	/* 
	 *  获取异常消费前几名，按年月
	 */
	@Override
	public Map<String, Object> findAbnormalConsumption(String year, String month, int num) {
		if(StringUtils.isBlank(year)|| StringUtils.isBlank(month)){
			return null;
		}
		List<ShowPoorAbnormalConsumption> list = getAbnormalConsumeStu(year, 
				String.valueOf(Integer.valueOf(month)), num);
		if(null != list && list.size()>0){
			return convertAbnormalList(list, false);
		}
		return null;
	}
	
	/**
	 * 将获取到的异常消费数据转化为map格式
	 * 返回的最终数据格式为：消费类型,学生姓名+班级,月份,[消费值,年增长率,月增长率] 
	 * @param list	已经按年或按月获取到的消费记录数据
	 * @param isYear	是按年还是按月，涉及到消费值是以月还是以天为键
	 * @return
	 */
	private Map<String, Object> convertAbnormalList(List<ShowPoorAbnormalConsumption> list, boolean isYear){
		if(null == list || list.size() == 0){
			return null;
		}
		int size = list.size();
		// 定义返回数据
		Map<String, Object> returnmap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 // 将列表数据转化为map键值对格式,key为消费类型，value为该消费类型下的所有记录列表
		Map<String, List<ShowPoorAbnormalConsumption>> map = new HashMap<String, List<ShowPoorAbnormalConsumption>>();
		for(int i=0; i<size; i++){
			ShowPoorAbnormalConsumption item = list.get(i);
			// 学生姓名
			//String stuName = item.getStuname();
			// 消费类型
			String type = item.getType();	
			// 同消费类型下的列表
			List<ShowPoorAbnormalConsumption> childlist = null;	
			// 包含这个消费类型的key
			if(map.containsKey(type)){
				// 获取已有的列表数据
				childlist = map.get(type);	
			}else{	
				// 不包含该消费类型的key，新生成一个列表
				childlist = new ArrayList<ShowPoorAbnormalConsumption>();
			}
			childlist.add(item);
			map.put(type, childlist);
		}
		// 对每一个消费类型的列表进行处理
		Set<String> set = map.keySet();
		Iterator<String> ite = set.iterator();
		while(ite.hasNext()){
			// 消费类型
			String key = ite.next();	
			// 该消费类型的数据列表
			List<ShowPoorAbnormalConsumption> childlist = map.get(key);
			/* 
			 * 定义以<学生姓名>为key，该学生的消费记录列表为value的数据格式
			 * 即该变量的value表示某一消费类型下某学生的消费记录
			 * */
			Map<String, List<Object>> childmap = new HashMap<String, List<Object>>();
			for(int i=0, len=childlist.size(); i<len; i++){	
				ShowPoorAbnormalConsumption s = childlist.get(i);	
				String name = s.getStuname();
				String college = s.getCollege();
				// key，由姓名和班级组成
				String str = name+","+college;	
				// 定义value变量
				List<Object> childlist2 = null;	
				if(childmap.containsKey(str)){
					childlist2 = childmap.get(str);
				}else{
					childlist2 = new ArrayList<Object>();
				}
				childlist2.add(s);
				childmap.put(str, childlist2);
			}
			Set<String> childset = childmap.keySet();
			Iterator<String> childite = childset.iterator();
			/*
			 * 定义某一消费类型下学生的消费，key为学生姓名+班级，value为Map<月份,[消费值,年增长率,月增长率]>
			 * */
			Map<String, Object> returnmap_child = new HashMap<String, Object>();
			while(childite.hasNext()){
				//学生姓名+班级
				String name = childite.next();	
				List<Object> lv = childmap.get(name);
				//定义value，表示各个月的消费值
				Map<Integer, String[]> value = new HashMap<Integer, String[]>();
				for(int i=0, le=lv.size(); i<le; i++){
					ShowPoorAbnormalConsumption s = (ShowPoorAbnormalConsumption) lv.get(i);
					// 日期转换
					String[] date = sdf.format(s.getYyyymmdd()).split("-");	
					// 月份或者按天
					int time = isYear ? Integer.parseInt(date[1]) : Integer.parseInt(date[2]);	
					String[] strs = null;
					// 包含这个键，则先获取值再相加
					if(value.containsKey(time)){	
						strs = value.get(time);
						strs[0] = String.valueOf(Integer.valueOf(strs[0]) + s.getValue());
						value.put(time, strs);
					}else{	
						strs = new String[3];	
						// 消费值
						strs[0] = String.valueOf(s.getValue());	
						// 年增长率
						strs[1] = (s.getYearRate() == null) ? "" : s.getYearPercent();	
						// 月增长率
						strs[2] = (s.getMonthRate() == null) ? "" : s.getMonthPercent();				
						value.put(time, strs);
					}
				}
				returnmap_child.put(name, value);
			}
			returnmap.put(key, returnmap_child);
		}
		return returnmap;
	}
	
	/**
	 * 获取异常消费的学生
	 * @param year	年份
	 * @param month	月份
	 * @param num	前X名
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ShowPoorAbnormalConsumption> getAbnormalConsumeStu(String year,
			String month, int num){
		// 获取所有学生的消费记录
		List<ShowPoorAbnormalConsumption> allConsumeRecords = showPoorAbnormalConsumptionMapper.selectByMap(null);
		// 通过set集合去重属性找出所有学生
		Set<String> h = new HashSet<String>();
		for (ShowPoorAbnormalConsumption a : allConsumeRecords) {
			h.add(a.getStudentID());
		}
		// 将学生放入数组，方便下面的for循环
		ArrayList stuList = new ArrayList();
		stuList.addAll(h);
		// 月份为空，则只传了年份，按年计算消费同比涨跌指标
		if ((StringUtils.isBlank(month)) && !StringUtils.isBlank(year)) {
			AbnormalConsumeModel ab = null;
			// 该model用于临时存放本期数据、同期数据和同比增长率（百分比）、同比增长数（浮点数）
			List<AbnormalConsumeModel> acm = new ArrayList<AbnormalConsumeModel>();
			for (int m = 0; m < stuList.size(); m++) {
				ab = new AbnormalConsumeModel();
				ab.setStudentID((String) stuList.get(m));
				List<ShowPoorAbnormalConsumption> temp = showPoorAbnormalConsumptionMapper.selectList(
						new EntityWrapper().eq("studentID", (String) stuList.get(m)).ge("yyyymmdd", year + "-01-01").le("yyyymmdd", year + "-12-31")
				);
				if(temp.size() ==0){
					ab.setRate("0");
				}else{
					ab.setRate(temp.get(0).getYearRate());
				}	
				acm.add(ab);
			}
			// 按照涨幅指标进行排序
			Collections.sort(acm, new Comparator() {
				@Override
				public int compare(Object arg0, Object arg1) {
					AbnormalConsumeModel ab0 = (AbnormalConsumeModel) arg0;
					AbnormalConsumeModel ab1 = (AbnormalConsumeModel) arg1;
					if (Float.parseFloat(ab0.getRate()) < Float.parseFloat(ab1.getRate())) {
						return 1;
					} else if (Float.parseFloat(ab0.getRate()) == Float.parseFloat(ab1.getRate())) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			// 排序之后，取前num个作为异常消费学生，并获取这些学生在该时间段的消费记录
			Wrapper where = new EntityWrapper().le("yyyymmdd", year + "-12-31").ge("yyyymmdd", year + "-01-01");
			if(num > 0) {
				String[] sA = new String[num];
				for (int i = 0; i < num; i++) {
					String stuID = acm.get(i).getStudentID();
					sA[i] = stuID;
				}
				where.in("studentID", sA);
			}
			List<ShowPoorAbnormalConsumption> asList = showPoorAbnormalConsumptionMapper.selectList(where);
			return asList;
		} else if (!StringUtils.isEmpty(month) && !StringUtils.isEmpty(year)) {
			// 月份不为空，默认计算月同比涨幅
			AbnormalConsumeModel ab1 = null;
			List<AbnormalConsumeModel> acm1 = new ArrayList<AbnormalConsumeModel>();
			for (int m = 0; m < stuList.size(); m++) {
				ab1 = new AbnormalConsumeModel();
				ab1.setStudentID((String) stuList.get(m));
				String hql1 = "";
				// 如果为一月，则与去年的12月份比
				EntityWrapper entityWrapper = new EntityWrapper();
				entityWrapper.eq("studentID", (String) stuList.get(m));
				entityWrapper.ge("yyyymmdd", year + "-" + month + "-01").le("yyyymmdd", year + "-" + month + "-31");
				List<ShowPoorAbnormalConsumption> temp = showPoorAbnormalConsumptionMapper.selectList(entityWrapper);
				if(temp.size() ==0){
					ab1.setRate("0");
				}else{
					ab1.setRate(temp.get(0).getYearRate());
				}				
				acm1.add(ab1);
			}
			Collections.sort(acm1, new Comparator() {
				@Override
				public int compare(Object arg0, Object arg1) {
					AbnormalConsumeModel ab0 = (AbnormalConsumeModel) arg0;
					AbnormalConsumeModel ab1 = (AbnormalConsumeModel) arg1;
					if (Float.parseFloat(ab0.getRate()) < Float.parseFloat(ab1.getRate())) {
						return 1;
					} else if (Float.parseFloat(ab0.getRate()) == Float.parseFloat(ab1.getRate())) {
						return 0;
					} else {
						return -1;
					}
				}

			});
			// 排序之后，取前num个作为异常消费学生，并获取这些学生在该时间段的消费记录
			Wrapper where = new EntityWrapper().le("yyyymmdd", year + "-" + month + "-31").ge("yyyymmdd", year + "-" + month + "-01");
			if(num > 0) {
				String[] sA = new String[num];
				for (int i = 0; i < num; i++) {
					String stuID = acm1.get(i).getStudentID();
					sA[i] = stuID;
				}
				where.in("studentID", sA);
			}
			List<ShowPoorAbnormalConsumption> asList1 = showPoorAbnormalConsumptionMapper.selectList(where);
			return asList1;
		} else {
			return null;
		}
	}	
}
