package com.mit.campus.rest.modular.lossContact.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.lossContact.model.*;
import com.mit.campus.rest.modular.lossContact.service.*;
import com.mit.campus.rest.modular.lossContact.ui.model.LossSiteStatisticsModel;
import com.mit.campus.rest.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: campus-parent
 * @Date: 2018/9/6 10:20
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@RestController
@RequestMapping("/lossContactWarn")
public class LossContactWarnControll {
    @Resource
    private IShowUncontactKeyWordsService showUncontactKeyWordsService;

    @Resource
    private IShowUncontactPlaceService showUncontactPlaceService;

    @Resource
    private IShowUncontactStatisticsService showUncontactStatisticsService;

    @Resource
    private IShowUncontactStuInfoService showUncontactStuInfoService;

    @Resource
    private IUncontactReasonStatisticsService uncontactReasonStatisticsService;

    @Resource
    private IUncontactRunTaskService uncontactRunTaskService;

    @Resource
    private IUnContactSiteStatisticsService unContactSiteStatisticsService;

    @Resource
    private IUncontactWarnIncService uncontactWarnIncService;

    /**
     * 获取失联预警学生
     *
     * @return
     */
    @RequestMapping(value = "/getUncontactStus", method = RequestMethod.POST)
    public Result getUncontactStus(String time) {
        //返回对象
        List<ShowUncontactStuInfo> list = null;
        if (null == time || "".equals(time.trim())) {
            list = showUncontactStuInfoService.findUncontactStus();
        } else {
            //对月份进行处理，小于10的前面加0
            String[] times = time.split("-");
            String month = times[1];
            month = (Integer.parseInt(month) > 9) ? month : (month.indexOf("0") != -1) ? month : "0" + month;
            list = showUncontactStuInfoService.findUncontactStus(times[0] + "-" + month);
        }
        if (null != list && list.size() > 0) {
            return Result.success(list);
        }
        return Result.success(null);
    }

    /**
     * 获取失联及活跃区域
     *
     * @return
     */
    @RequestMapping(value = "/getUncontactPlace", method = RequestMethod.POST)
    public Result getUncontactPlace(String time) {
        if (time == null || "".equals(time.trim())) {
            return Result.error("参数为空不合理");
        }
        //对月份进行处理，小于10的前面加0
        String[] times = time.split("-");
        String month = times[1];
        month = (Integer.parseInt(month) > 9) ? month : (month.indexOf("0") != -1) ? month : "0" + month;
        List<ShowUncontactPlace> list = showUncontactPlaceService.findUncontactPlace(times[0] + "-" + month);
        if (null != list && list.size() > 0) {
            /*
             * 将失联区域、学生活跃区域分开
             */
            Map<String, List<ShowUncontactPlace>> map = new HashMap<String, List<ShowUncontactPlace>>();
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowUncontactPlace s = list.get(i);
                String type = s.getPlacetype();
                List<ShowUncontactPlace> childlist = null;
                if (map.containsKey(type)) {
                    childlist = map.get(type);
                } else {
                    childlist = new ArrayList<ShowUncontactPlace>();
                }
                childlist.add(s);
                map.put(type, childlist);
            }

            /*
             *列表数据转换
             * 转换成json后数据格式：[{"name":<区域类型>,"value":[{"name":<地点名称>,"value":[<坐标>,<程度指数>]>},...]}, ...}
             */
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = null;
            Set<String> set = map.keySet();
            Iterator<String> ite = set.iterator();
            while (ite.hasNext()) {
                //类型名称
                String typeName = ite.next();
                //该类型下的区域
                List<ShowUncontactPlace> childlist_type = map.get(typeName);
                jsonObject = new JSONObject();
                JSONArray value = new JSONArray();
                for (int i = 0, len = childlist_type.size(); i < len; i++) {
                    ShowUncontactPlace ss = childlist_type.get(i);
                    String[] coors = ss.getCoordinate().split(",");
                    List<Object> v = new ArrayList<Object>();
                    v.add(Double.parseDouble(coors[0]));
                    v.add(Double.parseDouble(coors[1]));
                    v.add(ss.getNumber());

                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("name", ss.getPlacename());
                    jsonObject2.put("value", v.toArray());

                    value.add(jsonObject2);
                }
                jsonObject.put("name", typeName);
                jsonObject.put("value", value);
                jsonArray.add(jsonObject);
            }
            return Result.success(jsonArray.toArray());
        } else {
            return Result.success(null);
        }
    }

    /**
     * 获取年月时间下的失联学生统计
     *
     * @param time
     * @return
     */
    @RequestMapping(value = "/getUncontactStatistics", method = RequestMethod.POST)
    public Result getUncontactStatistics(String time) {
        if (time == null || "".equals(time.trim())) {
            return Result.error("参数为空不合理");
        }
        //对月份进行处理，小于10的前面加0
        String[] times = time.split("-");
        String month = times[1];
        month = (Integer.parseInt(month) > 9) ? month : (month.indexOf("0") != -1) ? month : "0" + month;
        //获取年月下失联列表数据
        List<ShowUncontactStatistics> list = showUncontactStatisticsService.findUncontactStatistics(times[0] + "-" + month);
        if (null != list && list.size() > 0) {
            /*
             * 将列表按学院分类
             */
            Map<String, List<ShowUncontactStatistics>> map = new HashMap<String, List<ShowUncontactStatistics>>();
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowUncontactStatistics s = list.get(i);
                String college = s.getCollege();
                List<ShowUncontactStatistics> childlist = null;
                if (map.containsKey(college)) {
                    childlist = map.get(college);
                } else {
                    childlist = new ArrayList<ShowUncontactStatistics>();
                }
                childlist.add(s);
                map.put(college, childlist);
            }

            /* 列表数据转换
             * 转换成json后数据格式：[{"name":<学院>,"value":[[<失联地点名称>,<日>,<提示日期：月日>,<人数>,<失联学生名称>],...]}, ...]
             * */
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = null;
            Set<String> set = map.keySet();
            Iterator<String> ite = set.iterator();
            //对于各个学院
            while (ite.hasNext()) {
                //学院名称
                String collegeName = ite.next();
                //该学院下的失联学生统计列表
                List<ShowUncontactStatistics> childlist_college = map.get(collegeName);
                jsonObject = new JSONObject();
                JSONArray value = new JSONArray();
                for (int i = 0, len = childlist_college.size(); i < len; i++) {
                    ShowUncontactStatistics ss = childlist_college.get(i);
                    List<Object> v = new ArrayList<Object>();
                    v.add(ss.getPlace());
                    v.add(Integer.parseInt(ss.getDd()));
                    v.add(ss.getTimeslot());
                    v.add(ss.getNumber());
                    v.add(ss.getNames());

                    value.add(v.toArray());
                }
                jsonObject.put("name", collegeName);
                jsonObject.put("value", value);
                jsonArray.add(jsonObject);
            }
            return Result.success(jsonArray.toArray());
        } else {
            return Result.success(null);
        }
    }

    /**
     * 获取失联关键词
     *
     * @return
     */
    @RequestMapping(value = "/getUncontactKeywords", method = RequestMethod.POST)
    public Result getUncontactKeywords() {
        //获取失联关键词
        List<ShowUncontactKeyWords> list = showUncontactKeyWordsService.findUncontactKeywords();
        if (null != list && list.size() > 0) {
            /* 列表数据转换
             * 转换成json后数据格式：[{"name":<关键词>,"value":[<词频>,<百分比>]}, ...]
             * */
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = null;
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowUncontactKeyWords s = list.get(i);
                jsonObject = new JSONObject();
                String[] value = {String.valueOf(s.getNumber()), s.getProportion()};
                try {
                    jsonObject.put("name", s.getKeyword());
                    jsonObject.put("value", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Result.error("json数据转换失败");
                }
                jsonArray.add(jsonObject);
            }
            return Result.success(jsonArray.toArray());
        }
        return Result.success(null);
    }

    /**
     * @param
     * @Description: 二级页面-条件查询失联预警事件
     * @Return:
     * @Author: Mr.Deng
     * @Date: 2018年9月6日
     */
    @RequestMapping(value = "/getLossWarnIncidents", method = RequestMethod.POST)
    public Result getLossWarnIncidents(String stuClass, String stuName, String dealStatus,
                                       String startTime, String endTime, int pageNum, int pageSize) {
        //按条件查询
        Page obj = uncontactWarnIncService.getLossWarnIncidents(stuClass, stuName, dealStatus,
                startTime, endTime, pageNum, pageSize);
        //查询结果
        return Result.success(obj, "查询完成");
    }

    /**
     * @param ucId 失联号
     * @Description: 获取失联处理流程的实时信息
     * @Return:
     * @Author: Mr.Deng
     */
    @RequestMapping(value = "/getLossWarnRunTask", method = RequestMethod.POST)
    public Result getLossWarnRunTask(String ucId) {
        //按条件查询
        List<UncontactRunTask> obj = uncontactRunTaskService.getLossWarnRunTask(ucId);
        if (obj != null) {
            //查询结果
            return Result.success(obj, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * getTopWarn
     *
     * @Description: top10 -预警人数最多的十个学院
     * @Author: Mr.Deng
     */

    @RequestMapping(value = "/getTopWarn", method = RequestMethod.POST)
    public Result getTopWarn() {
        List<Map<String, Object>> list = uncontactWarnIncService.getTopCollegeWarn();
        if (null != list && list.size() > 0) {
            return Result.success(list, "查询成功");
        }
        return Result.error("查询失败");
    }

    /**
     * @param
     * @Description: 查询失联地点的统计信息
     * @Return:
     * @Author: Mr.Deng
     */
    @RequestMapping(value = "/getSiteStatistics", method = RequestMethod.POST)
    public Result getSiteStatistics(String siteName, String startTime, String endTime, int pageNum, int pageSize) {
        //按条件查询
        Page p = unContactSiteStatisticsService.getSiteStatistics(siteName, startTime, endTime, pageNum, pageSize);
        if (p != null) {
            //查询结果
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * @param place 失联地点
     * @param yyyy  年份
     * @Description: 查询某一地点在某年内的所有失联人数分布
     * @Return:
     * @Author: Mr.Deng
     * @Date:
     */
    @RequestMapping(value = "/getSiteLossIncident", method = RequestMethod.POST)
    public Result getSiteLossIncident(String place, String yyyy) {
        //按条件查询
        List<LossSiteStatisticsModel> p = showUncontactStatisticsService.getSiteLossIncident(place, yyyy);
        if (p != null) {
            //查询结果
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * getTopSite
     *
     * @param
     * @Description: top10-失联人数最多的十个地点
     * @Return:
     * @Author: Mr.Deng
     */
    @RequestMapping(value = "/getTopSite", method = RequestMethod.POST)
    public Result getTopSite() {
        List<Map<String, Object>> list = unContactSiteStatisticsService.getTopSite();
        if (null != list && list.size() > 0) {
            return Result.success(list, "查询成功");
        }
        return Result.error("查询失败");
    }

    /**
     * @param stuName    学生姓名
     * @param stuClass   学院
     * @param startTime  入学年份
     * @param endTime
     * @param lossReason 失联原因
     * @param pageNum    第几页
     * @param pageSize   每页条数
     * @Description: 查询二级页面，失联原因分析的数据
     * @Return:
     * @Author: Mr.Deng
     * @Date:
     */
    @RequestMapping(value = "/getReasonStatistics", method = RequestMethod.POST)
    public Result getReasonStatistics(String stuName, String stuClass, String startTime, String endTime,
                                      String lossReason, int pageNum, int pageSize) {
        //按条件查询ssReason, pageNum, pageSiz
        Page p = uncontactReasonStatisticsService.getReasonStatistics(stuName, stuClass, startTime, endTime, lossReason, pageNum, pageSize);
        if (p != null) {
            //查询结果
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * @param uuid
     * @Description: 二级页面查询学生标签
     * @Return:
     * @Author: Mr.Deng
     */
    @RequestMapping(value = "/getReasonById", method = RequestMethod.POST)
    public Result getReasonById(String uuid) {
        //按条件查询 UncontactReasonStatistics
        UncontactReasonStatistics p = uncontactReasonStatisticsService.getById(uuid);
        if (p != null) {
            //查询结果
            return Result.success(p, "查询完成");
        }
        return Result.error("查询为空");
    }

    /**
     * @param
     * @Description: top10-失联人数最多的十个原因
     * @Return:
     * @Author: Mr.Deng
     */
    @RequestMapping(value = "/getTopReason", method = RequestMethod.POST)
    public Result getTopReason() {
        List<Map<String, Object>> list = uncontactReasonStatisticsService.getTopReason();
        if (null != list && list.size() > 0) {
            return Result.success(list, "查询成功");
        }
        return Result.error("查询失败");
    }
}
