package com.mit.campus.rest.modular.enrolldecision.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.enrolldecision.model.*;
import com.mit.campus.rest.modular.enrolldecision.service.*;
import com.mit.campus.rest.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:31
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@RestController
@Slf4j
@RequestMapping(value = "/enrollDes")
public class EnrollDecisionController {

    @Resource
    private IShowEnrollBirthPlaceService showEnrollBirthPlaceService;

    @Resource
    private IShowEnrollWebCityService showEnrollWebCityService;

    @Resource
    private IShowEnrollWorkCityService showEnrollWorkCityService;

    @Resource
    private IShowEnrollDeTreeService showEnrollDeTreeService;

    @Resource
    private IShowEnrollWebTimeService showEnrollWebTimeService;

    @Resource
    private IShowEnrollWebWordsService showEnrollWebWordsService;

    @Resource
    private IEnrollVisitCityService enrollVisitCityService;

    @Resource
    private IEnrollJobService enrollJobService;

    @Resource
    private IErollVisitTimeService erollVisitTimeService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object add(StudentInfo student) {
        return "fail to update";
    }

    /**
     * @param year
     * @param school
     * @return
     */
    @RequestMapping(value = "/birthPlaceDis", method = RequestMethod.POST)
    public Result getbirthPlaceDisInfo(String year, String school) {
            if (year != null && year.length() > 0 && school != null && school.length() > 0) {
                List<ShowEnrollBirthPlace> list = showEnrollBirthPlaceService.getAllBirthPlace(year, school);
                if (list != null && list.size() > 0) {
                    JSONArray jsonArray_Amounts = new JSONArray();
                    for (ShowEnrollBirthPlace s : list) {
                        JSONObject jsonObject_Amounts = new JSONObject();
                        try {
                            //生源地名，即省份
                            jsonObject_Amounts.put("name", s.getBirthPlace());
                            //人数
                            jsonObject_Amounts.put("value", s.getCounts());
                            //是否重点生源地，true 或false
                            jsonObject_Amounts.put("isTop", s.isPrimaryPlace());
                        } catch (JSONException e) {
                            String errorMessage = "json数据转换失败";
                            log.error(errorMessage, e);
                            return Result.error(errorMessage);
                        }
                        jsonArray_Amounts.add(jsonObject_Amounts);
                    }
                    return Result.success(jsonArray_Amounts.toArray(), year + "年" + school + "的数据获取完成");
                }
            }
            return Result.error("查询参数不足");
        }

        /**
         * 访问情况，访问最多的城市分布和来源分布
         *
         * @param currentMonth 当前月份
         * @return
         */
        @RequestMapping(value = "/getWebCitys", method = RequestMethod.POST)
    public Result getWebCitys(String currentMonth) {
                if (currentMonth != null && currentMonth.length() > 0 && currentMonth.indexOf("-") > 0) {
                    String dateValue = checkDateFormat(currentMonth);
                    if (dateValue != null) {
                        List<ShowEnrollWebCity> list = showEnrollWebCityService.getAllWebCity(dateValue);
                        if (list != null && list.size() > 0) {
                            List<Object[]> monthCitys = new ArrayList<Object[]>();
                            List<String> citys = new ArrayList<String>();
                            List<int[]> srcArrays = new ArrayList<int[]>();
                            //7种访问来源顺序
                            for (int k = 0; k < 7; k++) {
                        //第K种访问来源的前12个城市
                        int[] arr_ = new int[12];
                                //每月访问量居高的前12个城市
                                for (int j = 0; j < 12; j++) {
                                    String sources = list.get(j).getNetSource();
                                    String cityName = list.get(j).getCity();
                                    if (sources.length() > 0 && sources.indexOf("@") > 0) {
                                        //顺序为：360搜索//百度//省招生网//省教育网//搜狗//学校官网//其他
                                        String srcArr = sources.split("@")[k];
                                        if (srcArr.length() > 0 && srcArr.indexOf("=") > 0) {
                                            try {
                                                arr_[j] = Integer.parseInt(srcArr.split("=")[1]);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                continue;
                                            }
                                        }
                                    }
                                    citys.add(cityName);
                                }
                                srcArrays.add(arr_);
                            }
                            citys.subList(12, citys.size()).clear();
                            //城市名单，纵轴值
                            monthCitys.add(citys.toArray());
                            //每月的城市访问情况
                            monthCitys.add(srcArrays.toArray());
                            return Result.success(monthCitys.toArray(), currentMonth + "的数据查询完成");
                        }
                        return Result.error("查询结果为空");
                    }
                }
                return Result.error("查询参数不足");
            }

    /**
     * 查询某生源地的毕业生毕业去向和三年人数变化
     *
     * @param year
     * @param school
     * @param city
     * @return
     */
    @RequestMapping(value = "/getCityinfo", method = RequestMethod.POST)
    public Result getCityinfo(String year, String school, String city) {
        if (year != null && year.length() > 0 && school != null && school.length() > 0 && city != null && city.length() > 0) {
            ShowEnrollBirthPlace sb = showEnrollBirthPlaceService.getCityAmounts(year, school, city);
            if (sb != null) {
                //毕业去向
                String workPlaces = sb.getGraduatePlaces();
                List<Object[]> resArray = new ArrayList<>();
                List<Object[]> ahead_value = new ArrayList<Object[]>();

                if (workPlaces.length() > 0 && workPlaces.indexOf("!@#") > 0) {
                    for (String wp : workPlaces.split("!@#")) {
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("name", city);
                        JSONObject jsonObject2 = new JSONObject();
                        if (wp != null && wp.length() > 0 && wp.indexOf("=") > 0) {
                            String cityName = wp.split("=")[0].trim();
                            String cityCount = wp.split("=")[1].trim();
                            if (cityName != null && cityName.length() > 0 && cityCount != null && cityCount.length() > 0) {
                                try {
                                    jsonObject2.put("name", cityName);
                                    jsonObject2.put("value", Integer.parseInt(cityCount));
                                } catch (NumberFormatException e) {
                                    String errorMessage = "int数据转换失败";
                                    log.error(errorMessage);
                                    return Result.error(errorMessage);
                                } catch (JSONException e) {
                                    String errorMessage = "json数据转换失败";
                                    log.error(errorMessage);
                                    return Result.error(errorMessage);
                                }
                            }
                            //如    [{name:'北京'}, {name:'上海',value:65}]
                            jsonArray.add(jsonObject1);
                            jsonArray.add(jsonObject2);
                            //如    [[{name:'北京'}, {name:'上海',value:65}],[{name:'北京'}, {name:'广东',value:40}]]
                            ahead_value.add(jsonArray.toArray());
                        }
                    }
                    //毕业去向的数组对象加进去
                    resArray.add(ahead_value.toArray());
                }
                //近三年人数变化
                String amountChange = sb.getAmountChange();
                //坐标
                List<String[]> amount_value = new ArrayList<String[]>();

                if (amountChange.length() > 0 && amountChange.indexOf("@") > 0) {
                    String[] ac = amountChange.split("@");
                    if (ac.length >= 3) {
                        for (int i = 0; i < 3; i++) {
                            String[] v = new String[2];
                            String ac_ = ac[i];
                            if (ac_.length() > 0 && ac_.indexOf("=") > 0) {
                                try {
                                    v[0] = (ac_.split("=")[0]);
                                    v[1] = (ac_.split("=")[1]);
                                } catch (NumberFormatException e) {
                                    String errorMessage = "int数据转换失败";
                                    log.error(errorMessage);
                                    return Result.error(errorMessage);
                                }
                                amount_value.add(v);
                            } else {
                                return Result.error("查询参数不足");
                            }
                        }
                        //三年人数变化的数组对象加进去，如[[1,1],[2,1],[3,1]]
                        resArray.add(amount_value.toArray());
                        return Result.success(resArray.toArray(), "的数据获取完成");
                    } else {
                        return Result.error("查询结果为空");
                    }
                }
            }
        }
        return Result.error("查询参数不足");
    }

    /**
     * 就业情况分析表，就业城市排名数据
     *
     * @param year    毕业年
     * @param college 学院
     * @param area    地区
     * @return
     */
    @RequestMapping(value = "/getWorkCitys", method = RequestMethod.POST)
    public Result getWorkCitys(String year, String college, String area) {
        List<Object[]> arrayList = new ArrayList<Object[]>();
        if (year != null && college != null && area != null && college.length() > 0 && area.length() > 0 && year.length() > 0) {
            List<ShowEnrollWorkCity> list = showEnrollWorkCityService.getAllWorkInfo(year, college, area);
            if (list != null && list.size() > 0) {
                //城市名数组
                List<String> citys = new ArrayList<String>();

                if (list.size() > 7) {
                    //只要前7名
                    list.subList(7, list.size()).clear();
                }
                List<Object[]> objs_count = new ArrayList<Object[]>();
                for (ShowEnrollWorkCity e : list) {
                    String cityName = e.getCityName();
                    //城市名数组
                    citys.add(cityName);
                    //学院或专业组成
                    String elements = e.getCollegeContent();
                    //男女比例，总人数
                    String sexContent = e.getCitySexContent();
                    if (elements != null && elements.length() > 0 && elements.indexOf("@") > 0 &&
                            sexContent != null && sexContent.length() > 0 && sexContent.indexOf("@") > 0) {
                        String[] elNames = elements.split("@");
                        String[] elNums = sexContent.split("@");
                        for (int i = 0; i < elNames.length; i++) {
                            List<Object> obj_city = new ArrayList<Object>();
                            String elNumChar = elNums[i];
                            String elName = elNames[i];

                            if (elNumChar != null && elNumChar.length() > 0 && elNumChar.indexOf(",") > 0 &&
                                    elName != null && elName.length() > 0) {
                                String[] eles = elNumChar.split(",");
                                try {
                                    obj_city.add(cityName);
                                    obj_city.add(Integer.parseInt(eles[0]));
                                    obj_city.add(elName);
                                    obj_city.add(Integer.parseInt(eles[1]));
                                    obj_city.add(Integer.parseInt(eles[2]));

                                    objs_count.add(obj_city.toArray());
                                } catch (NumberFormatException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }
                    }
                }
                arrayList.add(citys.toArray());
                arrayList.add(objs_count.toArray());

                String successMessage = year + "年" + college + "在" + area + "工作的毕业生数据查询完成";
                return Result.success(arrayList.toArray(), successMessage);
            }
        }
        return Result.error("查询参数不足");
    }

    /**
     * 专业倾向分析，决策树结构
     *
     * @param college 学院
     * @return
     */
    @RequestMapping(value = "/getDeTree", method = RequestMethod.POST)
    public Result getDeTree(String college) {
        if (college != null && college.length() > 0) {
            List<ShowEnrollDeTree> list = showEnrollDeTreeService.getAllTreeInfo(college);
            if (list != null && list.size() > 0) {
                //最后结果，树结构
                JSONObject fatherObject_root = new JSONObject();
                //树结构的根节点名
                fatherObject_root.put("name", college);

                JSONArray jsonArray_root = new JSONArray();
                for (ShowEnrollDeTree e : list) {
                    String content = e.getPropertyReg();
                    JSONObject fatherObject = new JSONObject();
                    fatherObject.put("name", e.getMajorName());
                    JSONArray jsonArray = new JSONArray();
                    if (content.length() > 0 && content.length() > 0 && content.indexOf("@") > 0) {
                        for (String reg : content.split("@")) {
                            //叶子节点
                            JSONObject jsonObject = new JSONObject();
                            if (reg != null && reg.length() > 0) {
                                jsonObject.put("name", reg);
                            }
                            jsonArray.add(jsonObject);
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", content);
                        jsonArray.add(jsonObject);
                    }
                    fatherObject.put("children", jsonArray.toArray());
                    jsonArray_root.add(fatherObject);
                }
                fatherObject_root.put("children", jsonArray_root.toArray());
                //最后结果是一个树结构的json对象
                String successMessage = college + "的数据查询完成";
                return Result.success(fatherObject_root, successMessage);
            }
        }
        return Result.error("查询参数不足");
    }

    /**
     * 访问时间分布-旧-每次点击事件接口
     *
     * @param date
     * @return
     */
    @RequestMapping(value = "/getWebVisitTime", method = RequestMethod.POST)
    public Result getWebVisitTime(String date) {
        String dateValue = checkDateFormat(date);
        if (dateValue != null) {
            //坐标数组
            List<Object[]> arrayDots = new ArrayList<Object[]>();
            int length = dateValue.length();
            //获取源数据,格式如 12@12@13@...@12
            String content = showEnrollWebTimeService.getAllWebVisit(dateValue);
            if (content != null && content.length() > 0 && content.indexOf("@") > 0) {
                String[] numbers = content.split("@");
                //横轴坐标
                List<Integer> xaxis = new ArrayList<Integer>();
                //纵轴坐标
                List<Integer> yaxis = new ArrayList<Integer>();
                if (length == 4 && numbers.length == 12) {
                    //按年
                    for (int i = 1; i <= 12; i++) {
                        int number_ = 0;
                        String number = numbers[i - 1];
                        if (number != null && number.length() > 0) {
                            try {
                                number_ = Integer.parseInt(number);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        xaxis.add(i);
                        yaxis.add(number_);
                    }
                } else if (length == 7 && numbers.length >= 28) {
                    //按月
                    int maxDay = 0;
                    int monthValue = Integer.parseInt(dateValue.split("-")[1]);
                    if (monthValue == 2) {
                        maxDay = 28;
                    } else if (monthValue == 4 || monthValue == 6 || monthValue == 9 || monthValue == 11) {
                        maxDay = 30;
                    } else {
                        maxDay = 31;
                    }
                    for (int i = 1; i <= maxDay; i++) {
                        int number_ = 0;
                        String number = numbers[i - 1];
                        if (number != null && number.length() > 0) {
                            try {
                                number_ = Integer.parseInt(number);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        xaxis.add(i);
                        yaxis.add(number_);
                    }
                } else if (length == 10 && numbers.length == 24) {
                    //按日
                    for (int i = 0; i <= 23; i++) {
                        int number_ = 0;
                        String number = numbers[i];
                        if (number != null && number.length() > 0) {
                            try {
                                number_ = Integer.parseInt(number);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        xaxis.add(i);
                        yaxis.add(number_);
                    }
                }
                arrayDots.add(xaxis.toArray());
                arrayDots.add(yaxis.toArray());
                return Result.success(arrayDots.toArray(), date + "的数据查询完成");
            }
        }
        return Result.error("查询参数不足");

    }

    /**
     * 招生咨询热词
     *
     * @return
     */
    @RequestMapping(value = "/getWebWords", method = RequestMethod.POST)
    public Result getWebWords() {
        List<ShowEnrollWebWords> mapList = showEnrollWebWordsService.getAllWebWords();
        if (mapList != null && mapList.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (ShowEnrollWebWords m : mapList) {
                JSONObject jsonObject = new JSONObject();
                String key = m.getKeyWords().trim();
                if (key != null && key.length() > 0) {
                    try {
                        jsonObject.put("name", key);
                        jsonObject.put("value", m.getCounts());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return Result.error("json数据转换失败");
                    }
                    jsonArray.add(jsonObject);
                }
            }
            if (jsonArray != null && jsonArray.size() > 0) {
                return Result.success(jsonArray.toArray());
            }
        }
        return Result.success(mapList);
    }

    /**
     * 二级页面-访问来源数据(分页)查询
     *
     * @param province
     * @param city
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getVisitCitySource", method = RequestMethod.POST)
    public Result getVisitCityAndSource(String province, String city, String startTime,
                                        String endTime, int pageNum, int pageSize) {
        //按条件查询
        Page p = enrollVisitCityService.getVisitCityAndSource(province, city, startTime, endTime, pageNum, pageSize);
        if (p != null) {
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * @param college   学院
     * @param city      就业城市
     * @param workArea  就业城市所属地区
     * @param startTime 毕业年份
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getVisitJob", method = RequestMethod.POST)
    public Result getVisitJob(String college, String city, String workArea, String startTime, String endTime, int pageNum, int pageSize) {
        //按条件查询
        Page p = enrollJobService.getVisitJob(college, city, workArea, startTime, endTime, pageNum, pageSize);
        if (p != null) {
            //查询结果
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * 二级页面  访问时间统计
     *
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getVisitTime", method = RequestMethod.POST)
    public Result getVisitTime(String startTime, String endTime, int pageNum, int pageSize) {
        //按条件查询
        Page p = erollVisitTimeService.getVisitTime(startTime, endTime, pageNum, pageSize);
        if (p != null) {
            //查询结果
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * 二级页面top10， 访问来源和城市排行
     *
     * @return
     */
    @RequestMapping(value = "/getTopVisitCity", method = RequestMethod.POST)
    public Result getTopVisitCity() {
        List<Map<String, Object>> list = enrollVisitCityService.getTopVisitCity();
        if (null != list && list.size() > 0) {
            return Result.success(list, "查询成功");
        }
        return Result.error("查询失败");
    }

    /**
     * 二级页面top10，毕业生在城市就业人数的排行
     *
     * @return
     */
    @RequestMapping(value = "/getTopJob", method = RequestMethod.POST)
    public Result getTopJob() {
        List<Map<String, Object>> list = enrollJobService.getTopJob();
        if (null != list && list.size() > 0) {
            return Result.success(list, "查询成功");
        }
        return Result.error("查询失败");
    }

    /**
     * 二级-访问量最多的前十个月份
     *
     * @return
     */
    @RequestMapping(value = "/getTopTime", method = RequestMethod.POST)
    public Result getTopTime() {
        List<Map<String, Object>> list = erollVisitTimeService.getTopTime();
        if (null != list && list.size() > 0) {
            return Result.success(list, "查询成功");
        }
        return Result.error("查询失败");
    }

            @SuppressWarnings("unused")
            public static String checkDateFormat (String date){
                if (date != null && date.length() > 0) {
                    SimpleDateFormat yearFom = new SimpleDateFormat("yyyy");
                    yearFom.setLenient(false);
                    SimpleDateFormat monthFom = new SimpleDateFormat("yyyy-MM");
                    monthFom.setLenient(false);
                    SimpleDateFormat dateFom = new SimpleDateFormat("yyyy-MM-dd");
                    dateFom.setLenient(false);
                    if (date.length() < 5) {
                        try {
                            Date dYear = yearFom.parse(date);
                            return date;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (date.length() < 8 && date.indexOf("-") > 0) {
                        try {
                            Date dMonth = monthFom.parse(date);
                            String[] arr_ym = date.split("-");
                            int monthNum = Integer.parseInt(arr_ym[1]);
                            String monthChar = (monthNum < 10 ? ("0" + monthNum) : (String.valueOf(monthNum)));
                            return arr_ym[0] + "-" + monthChar;
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return null;
                        }
                    } else if (date.length() < 11 && date.indexOf("-") > 0) {
                        try {
                            Date dDate = dateFom.parse(date);
                            String[] arr_ym = date.split("-");
                            int monthNum = Integer.parseInt(arr_ym[1]);
                            String monthChar = (String) (monthNum < 10 ? ("0" + monthNum) : (String.valueOf(monthNum)));
                            int dateNum = Integer.parseInt(arr_ym[2]);
                            String dateChar = (String) (dateNum < 10 ? ("0" + dateNum) : (String.valueOf(dateNum)));
                            return arr_ym[0] + "-" + monthChar + "-" + dateChar;
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                }
                return null;
            }
        }
