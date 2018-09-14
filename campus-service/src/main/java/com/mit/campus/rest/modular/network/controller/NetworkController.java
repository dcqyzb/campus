package com.mit.campus.rest.modular.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.network.model.*;
import com.mit.campus.rest.modular.network.service.*;
import com.mit.campus.rest.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *     网络行为控制类
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-07 16:30
 */
@RestController
@Slf4j
@RequestMapping(value = "/netWork")
@Api(value = "网络行为接口", tags = {"网络行为"})
public class NetworkController {

    @Autowired
    IShowNetbehaviordeviceService showNetbehaviordeviceService;
    @Autowired
    IShowNetbehaviorbandService showNetbehaviorbandService;
    @Autowired
    IShowNetbehaviorgradeService showNetbehaviorgradeService;
    @Autowired
    IShowNetbehaviorstatsService showNetbehaviorstatsService;
    @Autowired
    IShowNetbehaviorwordsService showNetbehaviorwordsService;
    @Autowired
    IEmployeeService employeeService;
    @Autowired
    INetbehaviordeviceService netbehaviordeviceService;
    @Autowired
    IStuNetbehaviorWordsService stuNetBehaviorWordsService;
    @Autowired
    INetbehaviorandgradeService netbehaviorandgradeService;

    /**
     * getFlowDivice
     * 获取无线AP设备信息和流量统计数据
     *
     * @param month 统计月份，yyyy-MM
     * @return 设备信息和流量统计数据
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "/getFlowDivice", method = RequestMethod.POST)
    @ApiOperation(value = "获取无线AP设备信息和流量统计数据",notes = "获取无线AP设备信息和流量统计数据")
    public Result getFlowDivice(String month) {
        if (month != null && month.length() > 0) {
            String dateValue = checkDateFormat(month);
            if (dateValue != null) {
                //查询该月的所有无线AP设备的上网数据
                List<ShowNetBehaviorDevice> list = showNetbehaviordeviceService.getNetDeviceInfo(dateValue);
                if (list != null && list.size() > 0) {
                    //放25个无线AP设备数据的数组集合
                    List<Object[]> array = new ArrayList<Object[]>();
                    for (ShowNetBehaviorDevice e : list) {
                        //放每一个AP的数据，包括设备编号，连接数，宽带信息，流量统计等
                        List<Object> obj = new ArrayList<Object>();
                        try {
                            //基站在图上的横坐标
                            double xaxis = Double.parseDouble(e.getXaxis());
                            //纵坐标
                            double yaxis = Double.parseDouble(e.getYaxis());

                            obj.add(xaxis);
                            obj.add(yaxis);
                            //设备编号
                            obj.add(e.getDeviceNumber());
                            //终端连接数
                            obj.add(e.getClientCount());
                            //安装时间
                            obj.add(e.getInstallDate());
                            //年限
                            obj.add(e.getLimitedTime());
                            //当前宽带
                            obj.add(e.getBroadband());
                            //宽带使用率
                            obj.add(e.getNetUsage());
                            //宽带消耗
                            obj.add(e.getFlowConsumped());

                            //转化为数组的形式存放
                            array.add(obj.toArray());
                        } catch (NumberFormatException e1) {
                            e1.printStackTrace();
                            return Result.error("数值转换失败，数据存在问题");
                        }
                    }
                    return Result.success(array.toArray(), month + "月的无线AP数据获取完成");
                }

            }
            return Result.error("查找结果为空");
        }
        return Result.error("查询参数不足");
    }

    /**
     * getBandWidthTendency
     * 查询带宽趋势
     *
     * @param time    时间
     * @param college 学院
     * @return 带宽趋势
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "/getBandWidthTency", method = RequestMethod.POST)
    @ApiOperation(value = "查询带宽趋势",notes = "查询带宽趋势")
    public Result getBandWidthTendency(String time, String college) {
        if (time != null && time.length() > 0 && college != null && college.length() > 0) {
            String dateValue = checkDateFormat(time);
            if (dateValue != null) {
                //查询总带宽
                List<ShowNetBehaviorBand> list = showNetbehaviorbandService.getNetworkBand(college, dateValue);
                if (list != null && list.size() > 0) {
                    //放 有线带宽的点数组和无线带宽的点数组
                    List<Object[]> array = new ArrayList<Object[]>();
                    //有线网络的带宽
                    String wiredContent = list.get(0).getWiredContent().trim();
                    if (wiredContent != null && wiredContent.length() > 0) {
                        //放有线带宽的点
                        List<Object[]> array_wired = new ArrayList<Object[]>();
                        if (wiredContent.indexOf("@") > 0) {
                            int count1 = 1;
                            //放有线带宽点坐标
                            List<Object> xaxis = new ArrayList<Object>();
                            for (String a : wiredContent.split("@")) {
                                //横坐标，月或日
                                xaxis.add(count1);
                                if (a.length() > 0) {
                                    try {
                                        count1++;
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        return Result.error("数值转换失败，数据存在问题");
                                    }
                                }
                            }
                            array_wired.add(xaxis.toArray());
                            //放有线带宽点坐标
                            List<Object> yaxis = new ArrayList<Object>();
                            for (String a : wiredContent.split("@")) {
                                if (a.length() > 0) {
                                    try {
                                        yaxis.add(Integer.parseInt(a.trim()));//纵坐标，带宽
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        return Result.error("数值转换失败，数据存在问题");
                                    }
                                }
                            }
                            array_wired.add(yaxis.toArray());
                        }
                        array.add(array_wired.toArray());
                    }

                    //无线网络的带宽
                    String wirelessContent = list.get(0).getWirelessContent().trim();
                    if (wiredContent != null && wiredContent.length() > 0) {
                        //放无线带宽的点
                        List<Object[]> array_wireless = new ArrayList<Object[]>();
                        if (wirelessContent.indexOf("@") > 0) {
                            int count2 = 1;
                            //放无线带宽点坐标
                            List<Object> xaxis = new ArrayList<Object>();
                            for (String b : wirelessContent.split("@")) {
                                //横坐标，月或日
                                xaxis.add(count2);
                                if (b.length() > 0) {
                                    try {
                                        count2++;
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        return Result.error("数值转换失败，数据存在问题");
                                    }
                                }
                            }
                            array_wireless.add(xaxis.toArray());
                            //放有线带宽点坐标
                            List<Object> yaxis = new ArrayList<Object>();
                            for (String b : wirelessContent.split("@")) {
                                if (b.length() > 0) {
                                    try {
                                        //纵坐标，带宽
                                        yaxis.add(Integer.parseInt(b.trim()));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        return Result.error("数值转换失败，数据存在问题");
                                    }
                                }
                            }
                            array_wireless.add(yaxis.toArray());
                        }
                        array.add(array_wireless.toArray());
                    }
                    //10个学期的数据
                    return Result.success(array.toArray(), college + "在" + time + "（年/月）的总带宽数据获取完成");
                }
                return Result.error("查找结果为空");
            }
        }
        return Result.error("查询参数不足");
    }


    /**
     * checkDateFormat
     * 工具方法日期格式转换
     *
     * @param date 时间
     * @return yyyy-MM-dd或yyyy-MM或yyyy
     * @author lw
     * @date 2018-9-7
     */
    private String checkDateFormat(String date) {
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


    /**
     * 获取上网状态信息
     *
     * @param school     学院
     * @param schoolYear 学年
     * @param term       学期
     * @return 信息
     */
    @RequestMapping(value = "/getStatsByTerms", method = RequestMethod.POST)
    @ApiOperation(value = "获取上网状态信息",notes = "获取上网状态信息")
    public Result getStatsByTerms(String school, String schoolYear, String term) {
        if (school != null && school.length() > 0 && schoolYear != null && schoolYear.length() > 0
                && term != null && term.length() > 0) {
            List<Object[]> array = new ArrayList<Object[]>();
            //查询
            List<ShowNetBehaviorStats> list = showNetbehaviorstatsService.getStatsByTerms(school, schoolYear, term);
            if (list != null) {
                //满足条件的只有一条
                ShowNetBehaviorStats s = list.get(0);

                //上网时长
                //上网时长区间1
                String hour_1 = s.getNetHour1().trim();
                String hour_2 = s.getNetHour2().trim();
                String hour_3 = s.getNetHour3().trim();
                String hour_4 = s.getNetHour4().trim();

                if (hour_1.indexOf("=") > 0 && hour_2.indexOf("=") > 0 && hour_3.indexOf("=") > 0 && hour_4.indexOf("=") > 0) {
                    JSONArray jsonArray_hour = new JSONArray();
                    JSONObject jsonObj_hour_1 = new JSONObject();
                    JSONObject jsonObj_hour_2 = new JSONObject();
                    JSONObject jsonObj_hour_3 = new JSONObject();
                    JSONObject jsonObj_hour_4 = new JSONObject();
                    try {
                        //占比,0.32
                        jsonObj_hour_1.put("value", Float.parseFloat(hour_1.split("=")[1].trim()));
                        //区间名,10-20h
                        jsonObj_hour_1.put("name", hour_1.split("=")[0].trim());
                        jsonArray_hour.add(jsonObj_hour_1);

                        //占比,0.32
                        jsonObj_hour_2.put("value", Float.parseFloat(hour_2.split("=")[1].trim()));
                        //区间名,10-20h
                        jsonObj_hour_2.put("name", hour_2.split("=")[0].trim());
                        jsonArray_hour.add(jsonObj_hour_2);

                        //占比,0.32
                        jsonObj_hour_3.put("value", Float.parseFloat(hour_3.split("=")[1].trim()));
                        //区间名,10-20h
                        jsonObj_hour_3.put("name", hour_3.split("=")[0].trim());
                        jsonArray_hour.add(jsonObj_hour_3);

                        //占比,0.32
                        jsonObj_hour_4.put("value", Float.parseFloat(hour_4.split("=")[1].trim()));
                        //区间名,10-20h
                        jsonObj_hour_4.put("name", hour_4.split("=")[0].trim());
                        jsonArray_hour.add(jsonObj_hour_4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return Result.error("json数据转换失败");
                    }
                    array.add(jsonArray_hour.toArray());
                }

                //上网次数
                //上网次数区间1
                String time_1 = s.getNetTime1().trim();
                String time_2 = s.getNetTime2().trim();
                String time_3 = s.getNetTime3().trim();
                String time_4 = s.getNetTime4().trim();
                if (time_1.indexOf("=") > 0 && time_2.indexOf("=") > 0 && time_3.indexOf("=") > 0 && time_4.indexOf("=") > 0) {
                    //上网次数
                    JSONArray jsonArray_time = new JSONArray();
                    JSONObject jsonObj_time_1 = new JSONObject();
                    JSONObject jsonObj_time_2 = new JSONObject();
                    JSONObject jsonObj_time_3 = new JSONObject();
                    JSONObject jsonObj_time_4 = new JSONObject();
                    try {
                        //占比,0.32
                        jsonObj_time_1.put("value", Float.parseFloat(time_1.split("=")[1].trim()));
                        //区间名,10-20次
                        jsonObj_time_1.put("name", time_1.split("=")[0].trim());
                        jsonArray_time.add(jsonObj_time_1);

                        //占比,0.32
                        jsonObj_time_2.put("value", Float.parseFloat(time_2.split("=")[1].trim()));
                        //区间名
                        jsonObj_time_2.put("name", time_2.split("=")[0].trim());
                        jsonArray_time.add(jsonObj_time_2);

                        //占比,0.32
                        jsonObj_time_3.put("value", Float.parseFloat(time_3.split("=")[1].trim()));
                        //区间名,
                        jsonObj_time_3.put("name", time_3.split("=")[0].trim());
                        jsonArray_time.add(jsonObj_time_3);

                        //占比,0.32
                        jsonObj_time_4.put("value", Float.parseFloat(time_4.split("=")[1].trim()));
                        //区间名,
                        jsonObj_time_4.put("name", time_4.split("=")[0].trim());
                        jsonArray_time.add(jsonObj_time_4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return Result.error("json数据转换失败");
                    }
                    array.add(jsonArray_time.toArray());
                }

                //流量分布
                //上行流量
                String flow_upload = s.getUpload().trim();
                //下行流量
                String flow_download = s.getDownload().trim();
                if (flow_upload.indexOf("=") > 0 && flow_download.indexOf("=") > 0) {
                    //流量分布
                    JSONArray jsonArray_flow = new JSONArray();
                    JSONObject jsonObj_flow_1 = new JSONObject();
                    JSONObject jsonObj_flow_2 = new JSONObject();
                    try {
                        //占比,0.32
                        jsonObj_flow_1.put("value", Float.parseFloat(flow_upload.split("=")[1].trim()));
                        //区间名,上行流量
                        jsonObj_flow_1.put("name", flow_upload.split("=")[0].trim());
                        jsonArray_flow.add(jsonObj_flow_1);
                        //占比,0.32
                        jsonObj_flow_2.put("value", Float.parseFloat(flow_download.split("=")[1].trim()));
                        //区间名,下行流量
                        jsonObj_flow_2.put("name", flow_download.split("=")[0].trim());
                        jsonArray_flow.add(jsonObj_flow_2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return Result.error("json数据转换失败");
                    }
                    array.add(jsonArray_flow.toArray());
                }
                //10个学期的数据
                return Result.success(array.toArray(), school + "学生在" + schoolYear + "学年（" + term + "）平均每天的上网数据获取完成");
            }
        }
        return Result.error("查询参数不足");
    }

    /**
     * getGradeByTerms
     * 某学院学生平均每天上网时长与成绩的关系（2013-2017年是个学期）
     *
     * @param school 学院
     * @return 学生和上网时长的关系
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "/getGradeByTerms", method = RequestMethod.POST)
    @ApiOperation(value = "某学院学生平均每天上网时长与成绩的关系",notes = "某学院学生平均每天上网时长与成绩的关系（2013-2017年是个学期）")
    public Result getGradeByTerms(String school, String schoolYear, String term) {
        if (school != null && school.length() > 0 && schoolYear != null && schoolYear.length() > 0
                && term != null && term.length() > 0) {
            List<Object[]> array_term1 = new ArrayList<Object[]>();
            //查询
            List<ShowNetBehaviorGrade> list1 = showNetbehaviorgradeService.getGradeByTerms(school, schoolYear, term);
            if (list1 != null) {
                for (ShowNetBehaviorGrade s : list1) {
                    //分数区间共6个：<60,60-69,70-79,80-89,90-99,100
                    List<Object> dot_term1 = new ArrayList<Object>();
                    float hour_term1 = Float.parseFloat(s.getHour());
                    //上网时间
                    dot_term1.add(hour_term1);
                    //分数区间
                    dot_term1.add(s.getGrade());
                    array_term1.add(dot_term1.toArray());
                }
            }
            //按平均上网时长从小到大排序
            Collections.sort(array_term1, new Comparator<Object[]>() {
                @Override
                public int compare(Object[] o1, Object[] o2) {
                    //根据数量生序排列
                    return ((Float) o1[0]).compareTo((Float) o2[0]);
                }
            });
            return Result.success(array_term1.toArray(), school + "学生在" + schoolYear + "学年（" + term + "）平均每天上网时长与成绩关系的数据获取完成");
        }
        return Result.error("查询参数不足");
    }

    /**
     * getNetWords
     * 沉迷网络关键词云
     *
     * @return 关键词
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "/getNetWords", method = RequestMethod.POST)
    @ApiOperation(value = "沉迷网络关键词云",notes = "沉迷网络关键词云")
    public Result getNetWords() {
        List<ShowNetBehaviorWords> list = showNetbehaviorwordsService.getAllWords();
        if (list != null) {
            JSONArray jsonArray = new JSONArray();
            for (ShowNetBehaviorWords s : list) {
                JSONObject jsonObject_Amounts = new JSONObject();
                try {
                    //词名
                    jsonObject_Amounts.put("name", s.getKeyWords());
                    //时长
                    jsonObject_Amounts.put("value", s.getCounts());
                    //占比
                    jsonObject_Amounts.put("tip", s.getPencentage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Result.error("jsn数据转换失败");
                }
                jsonArray.add(jsonObject_Amounts);

            }
            return Result.success(jsonArray.toArray(), "沉迷网络关键词云的数据获取完成");
        }
        return Result.error("查询参数不足");
    }

    /**
     * 获得负责人具体信息
     *
     * @param principalName 负责人名称
     * @return 负责人信息
     */
    @RequestMapping(value = "getEmployee", method = RequestMethod.POST)
    @ApiOperation(value = "获得负责人具体信息",notes = "获得负责人具体信息")
    public Result getEmployee(String principalName) {
        Employee employee = new Employee();
        if (principalName != null && principalName.length() != 0) {
            //查询负责人信息
            employee = employeeService.getEmployeeByname(principalName);
        }
        return Result.success(employee, "查询完成");
    }


    /**
     * getPoorNumberData
     * 二级页面-网络设备查询
     *
     * @param deviceNumber    设备编号
     * @param installLocation 设备放置地点
     * @param startDate       监测时间-开始时间
     * @param endDate         监测时间-结束时间
     * @param pageNum         当前页
     * @param pageSize        每页<= 0时导出
     * @return
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "networkDeviceNum", method = RequestMethod.POST)
    @ApiOperation(value = "二级页面-网络设备查询",notes = "二级页面-网络设备查询")
    public Result getPoorNumberData(String deviceNumber, String installLocation, String startDate, String endDate, int pageNum, int pageSize) {
        //先按条件查询
        Page obj = netbehaviordeviceService.getNetworkDeviceNum(deviceNumber, installLocation, startDate, endDate, pageNum, pageSize);
        //查询结果
        return Result.success(obj, "查询完成");
    }

    /**
     * getBehaviorAndGrade
     * 二级页面-学生网络行为和成绩查询
     *
     * @param stuname   学生姓名
     * @param college   学院
     * @param stuClass  专业
     * @param startDate 统计时间-开始时间
     * @param endDate   统计时间-结束时间
     * @param pageNum   当前页
     * @param pageSize  每页记录数，<= 0时导出
     * @return 学生网络行为和成绩查询
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "getBehaviorAndGrade", method = RequestMethod.POST)
    @ApiOperation(value = "二级页面-学生网络行为和成绩查询",notes = "二级页面-学生网络行为和成绩查询pageSize<=0查询全部")
    public Result getBehaviorAndGrade(String stuname, String college, String stuClass, String startDate, String endDate, int pageNum, int pageSize) {
        //先按条件查询
        Page obj = netbehaviorandgradeService.getBehaviorAndGrade(stuname, college, stuClass, startDate, endDate, pageNum, pageSize);
        //查询结果
        return Result.success(obj, "查询完成");
    }

    /**
     * getStuNetWords
     * 二级页面-学生沉迷网络原因查询
     *
     * @param studentName 学生姓名
     * @param collegeName 学院
     * @param major       专业
     * @param startDate   监测时间-开始时间
     * @param endDate     监测时间-结束时间
     * @param pageNum     当前页
     * @param pageSize    每页记录数，<= 0时导出
     * @return 沉迷网络原因
     * @author lw
     * @date 2018-9-7
     */
    @RequestMapping(value = "getStuNetWords", method = RequestMethod.POST)
    @ApiOperation(value = "二级页面-学生沉迷网络原因查询" ,notes = "二级页面-学生沉迷网络原因查询pageSize<= 0时查询全部")
    public Result getStuNetWords(String studentName, String collegeName, String major, String startDate, String endDate, int pageNum, int pageSize) {
        //先按条件查询
        Page obj = stuNetBehaviorWordsService.getStuNetWords(studentName, collegeName, major, startDate, endDate, pageNum, pageSize);
        //查询结果
        return Result.success(obj, "查询完成");
    }

    /**
     * 根据uuid查找学生上网的具体信息
     *
     * @param uuid 数据编号
     * @return 学生上网的具体信息
     */
    @RequestMapping(value = "findStuBehaviorAndGrade", method = RequestMethod.POST)
    @ApiOperation(value ="根据uuid查找学生上网的具体信息",notes = "根据uuid查找学生上网的具体信息")
    public Result findStuBehaviorAndGrade(String uuid) {
        if (uuid != null || uuid.length() > 0) {
            //获得学生上网信息
            NetBehaviorAndGrade net = netbehaviorandgradeService.findStuBehaviorAndGrade(uuid);
            return Result.success(net, "查询完成");
        }
        return null;
    }

    /**
     * 查找最新统计一个月的网络设备使用率top10的
     *
     * @return
     */
    @RequestMapping(value = "getTopDevice", method = RequestMethod.GET)
    @ApiOperation(value ="查找最新统计一个月的网络设备使用率top10的",notes = "查找最新统计一个月的网络设备使用率top10的")
    public Result getTopDevice() {
        List<NetBehavoirDevice> deviceslist = netbehaviordeviceService.findTopDevice();
        return Result.success(deviceslist, "查询完成");
    }

    /**
     * 学生平均上网时长top
     *
     * @return 平均上网时长top
     */
    @RequestMapping(value = "getTopStuNet", method = RequestMethod.GET)
    @ApiOperation(value ="学生平均上网时长top",notes = "学生平均上网时长top")
    public Result getTopStuNet() {
        List<NetBehaviorAndGrade> netlist = netbehaviorandgradeService.getTopStuNet();
        for (NetBehaviorAndGrade n:netlist){
            System.out.println(n);
        }
        return Result.success(netlist, "查询完成");
    }

    /**
     * 学生沉迷上网top10
     *
     * @return 学生沉迷上网top10
     */
    @RequestMapping(value = "getKeyWords", method = RequestMethod.GET)
    @ApiOperation(value ="学生沉迷上网top10",notes = "学生沉迷上网top10")
    public Result getKeyWords() {
        List<Object[]> netlist = stuNetBehaviorWordsService.getKeyWords();
        return Result.success(netlist, "查询完成");
    }

}
