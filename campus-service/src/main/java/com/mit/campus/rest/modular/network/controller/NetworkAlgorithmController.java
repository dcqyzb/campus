package com.mit.campus.rest.modular.network.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.campus.rest.algorithm.model.KAverage;
import com.mit.campus.rest.algorithm.model.NumberInvalieException;
import com.mit.campus.rest.modular.network.dao.StuonlineinfoMapper;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorStats;
import com.mit.campus.rest.modular.network.model.StuOnlineInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 网络行为无敌算法控制类
 * </p>
 *
 * @author LW
 * @comy mitesofor
 * @creatTime 2018-09-11 16:46
 */
@RestController
@Slf4j
@Api(value = "网络行为算法", tags = "网络行为算法")
public class NetworkAlgorithmController {
    @Autowired
    StuonlineinfoMapper stuonlineinfoMapper;

    /**
     * getStuOnlineCluster
     * 将学生上网时长和次数分别进行聚类,并将统计结果存入前端接口，用到的表StuOnlineInfo，ShowReaderTypeRate
     *
     * @throws NumberInvalieException 数值无效异常
     * @author LW
     * @since 2018-9-11
     */
    @RequestMapping(value = "/getStuOnlineCluster", method = RequestMethod.POST)
    @ApiOperation(value = "学生上网时长和次数分别进行聚类", notes = "学生上网时长和次数分别进行聚类,用到的表StuOnlineInfo，ShowReaderTypeRate")
    public void getStuOnlineCluster() throws NumberInvalieException {
        // 将学生上网时长和次数分别聚类
        // 获取所有学生上网数据作为算法的训练数据
        List<StuOnlineInfo> findAllOnline = stuonlineinfoMapper.selectList(null);
        if (null != findAllOnline && findAllOnline.size() > 0) {
            // 将所有学生每天上网时长和次数进行排序，取两头和二者平均数作为聚类的初始中心点
            List<String> arrOnlineDuration = new ArrayList<String>();
            List<String> arrOnlineTimes = new ArrayList<String>();
            for (StuOnlineInfo o : findAllOnline) {
                arrOnlineDuration.add(o.getDuration());
            }
            for (StuOnlineInfo i : findAllOnline) {
                arrOnlineTimes.add(i.getTimes());
            }
            KAverage ka = new KAverage(arrOnlineDuration);
            // 将聚类结果存入学生上网信息表
            double[][] sampleValues = ka.doCaculate(4);
            KAverage ka1 = new KAverage(arrOnlineTimes);
            // 将聚类结果存入学生上网信息表
            double[][] sampleValues1 = ka1.doCaculate(4);
            for (int m = 0; m < sampleValues.length; m++) {
                // 将每条数据的聚类结果set进去
                findAllOnline.get(m).setDurationCluster(Double.toString(sampleValues[m][1]));
                findAllOnline.get(m).setTimesCluster(Double.toString(sampleValues1[m][1]));
                stuonlineinfoMapper.updateById(findAllOnline.get(m));
            }
        }
        // 将聚类结果用来统计学生上网信息的数据
        ShowNetBehaviorStats netBehaviorStats = null;
        String[] year = {"2013上", "2013下", "2014上", "2014下", "2015上", "2015下", "2016上", "2016下", "2017上", "2017下"};
        String[] collegeName = {"全校", "经管学院", "体教学院", "管理学院", "理学院", "化学学院", "生科学院", "食品学院", "材料学院", "环化学院", "机电学院",
                "建筑学院", "信息学院"};
        for (int m = 0; m < year.length; m++) {
            for (int n = 0; n < collegeName.length; n++) {
                netBehaviorStats = new ShowNetBehaviorStats();
                // 学院
                netBehaviorStats.setSchool(collegeName[n]);
                // 学年，如2017上
                netBehaviorStats.setTerm(year[m]);
                // 上行流量
                netBehaviorStats.setDownload("下行流量=0.8");
                // 下行流量
                netBehaviorStats.setUpload("上行流量=0.2");
                String[] cluster = {"0.0", "1.0", "2.0", "3.0"};
                for (int l = 0; l < cluster.length; l++) {
                    // 根据学年、学院、上网时长聚类查询学生信息表，查出每类的上网时长范围和占比
                    // 上网时长
                    List<StuOnlineInfo> stuOnlineDurationInfo = null;
                    if (collegeName[n] == "全校") {
                        // 若学院为全校，则获取全校学生数据
                        stuOnlineDurationInfo = getDurationInfo(year[m].substring(0, year[m].length() - 1),
                                year[m].substring(year[m].length() - 1, year[m].length()), "", cluster[l]);

                    } else {
                        stuOnlineDurationInfo = getDurationInfo(year[m].substring(0, year[m].length() - 1),
                                year[m].substring(year[m].length() - 1, year[m].length()), collegeName[n], cluster[l]);
                    }
                    if (stuOnlineDurationInfo != null && stuOnlineDurationInfo.size() != 0) {
                        float rate;
                        if (collegeName[n] == "全校") {
                            List<StuOnlineInfo> allstuOnlineDurationInfo = getDurationInfo(
                                    year[m].substring(0, year[m].length() - 1),
                                    year[m].substring(year[m].length() - 1, year[m].length()), "", "");
                            // 上网时长占比
                            rate = (float) (stuOnlineDurationInfo.size()) / allstuOnlineDurationInfo.size();
                        } else {
                            List<StuOnlineInfo> allstuOnlineDurationInfo = getDurationInfo(
                                    year[m].substring(0, year[m].length() - 1),
                                    year[m].substring(year[m].length() - 1, year[m].length()), collegeName[n], "");
                            rate = (float) (stuOnlineDurationInfo.size()) / allstuOnlineDurationInfo.size();
                        }
                        // 确定每类聚类的范围
                        List<Double> arrHour = new ArrayList<Double>();
                        for (StuOnlineInfo s : stuOnlineDurationInfo) {
                            arrHour.add(Double.parseDouble(s.getDuration()));
                        }
                        Collections.sort(arrHour);
                        double max = Collections.max(arrHour);
                        double min = Collections.min(arrHour);
                        if (cluster[l] == "0.0") {
                            netBehaviorStats.setNetHour1(min + "-" + max + "h=" + Float.toString(rate));
                        } else if (cluster[l] == "1.0") {
                            netBehaviorStats.setNetHour2(min + "-" + max + "h=" + Float.toString(rate));
                        } else if (cluster[l] == "2.0") {
                            netBehaviorStats.setNetHour3(min + "-" + max + "h=" + Float.toString(rate));
                        } else {
                            netBehaviorStats.setNetHour4(min + "-" + max + "h=" + Float.toString(rate));
                        }
                    } else {
                        if (cluster[l] == "0.0") {
                            netBehaviorStats.setNetHour1("NetHour1无数据");
                        } else if (cluster[l] == "1.0") {
                            netBehaviorStats.setNetHour2("NetHour2无数据");
                        } else if (cluster[l] == "2.0") {
                            netBehaviorStats.setNetHour3("NetHour3无数据");
                        } else {
                            netBehaviorStats.setNetHour4("NetHour4无数据");
                        }
                    }
                    // 上网次数
                    List<StuOnlineInfo> stuOnlineTimesInfo = null;
                    if (collegeName[n] == "全校") {
                        stuOnlineTimesInfo = getTimesInfo(year[m].substring(0, year[m].length() - 1),
                                year[m].substring(year[m].length() - 1, year[m].length()), "", cluster[l]);
                    } else {
                        stuOnlineTimesInfo = getTimesInfo(year[m].substring(0, year[m].length() - 1),
                                year[m].substring(year[m].length() - 1, year[m].length()), collegeName[n], cluster[l]);
                    }
                    if (stuOnlineTimesInfo != null && stuOnlineTimesInfo.size() != 0) {
                        float rate1;
                        if (collegeName[n] == "全校") {
                            List<StuOnlineInfo> allstuOnlineTimesInfo = getTimesInfo(
                                    year[m].substring(0, year[m].length() - 1),
                                    year[m].substring(year[m].length() - 1, year[m].length()), "", "");
                            rate1 = (float) (stuOnlineTimesInfo.size()) / allstuOnlineTimesInfo.size();
                        } else {
                            List<StuOnlineInfo> allstuOnlineTimesInfo = getTimesInfo(
                                    year[m].substring(0, year[m].length() - 1),
                                    year[m].substring(year[m].length() - 1, year[m].length()), collegeName[n], "");
                            rate1 = (float) (stuOnlineTimesInfo.size()) / allstuOnlineTimesInfo.size();
                        }
                        List<Double> arrTimes = new ArrayList<Double>();
                        for (StuOnlineInfo s : stuOnlineTimesInfo) {
                            arrTimes.add(Double.parseDouble(s.getTimes()));
                        }
                        Collections.sort(arrTimes);
                        double max1 = Collections.max(arrTimes);
                        double min1 = Collections.min(arrTimes);
                        if (cluster[l] == "0.0") {
                            netBehaviorStats.setNetTime1(min1 + "-" + max1 + "次=" + Float.toString(rate1));
                        } else if (cluster[l] == "1.0") {
                            netBehaviorStats.setNetTime2(min1 + "-" + max1 + "次=" + Float.toString(rate1));
                        } else if (cluster[l] == "2.0") {
                            netBehaviorStats.setNetTime3(min1 + "-" + max1 + "次=" + Float.toString(rate1));
                        } else {
                            netBehaviorStats.setNetTime4(min1 + "-" + max1 + "次=" + Float.toString(rate1));
                        }
                    } else {
                        if (cluster[l] == "0.0") {
                            netBehaviorStats.setNetTime1("NetTime1无数据");
                        } else if (cluster[l] == "1.0") {
                            netBehaviorStats.setNetTime2("NetTime2无数据");
                        } else if (cluster[l] == "2.0") {
                            netBehaviorStats.setNetTime3("NetTime3无数据");
                        } else {
                            netBehaviorStats.setNetTime4("NetTime4无数据");
                        }
                    }

                }
                netBehaviorStats.insert();
            }
        }
    }

    /**
     * 根据学年、学院和上网时长的聚类查询学生信息
     *
     * @param year            年
     * @param term            学期
     * @param collegeName     学院
     * @param durationCluster 持续时间聚类
     * @return 无
     * @author LW
     * @date 2018-9-11
     */
    public List<StuOnlineInfo> getDurationInfo(String year, String term, String collegeName, String durationCluster) {
        List<StuOnlineInfo> stuOnlineDurationInfo = null;
        EntityWrapper<StuOnlineInfo> wrapper = new EntityWrapper<>();
        if (null != year && !year.trim().isEmpty()) {
            wrapper.and("year", year);
        }
        if (null != term && !term.trim().isEmpty()) {
            wrapper.and("term", term);
        }
        if (null != collegeName && !collegeName.trim().isEmpty()) {
            wrapper.and("college", collegeName);
        }
        if (null != durationCluster && !durationCluster.trim().isEmpty()) {
            wrapper.and("durationCluster", durationCluster);
        }
        try {
            stuOnlineDurationInfo = stuonlineinfoMapper.selectList(wrapper);
            return stuOnlineDurationInfo;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     *  根据学年、学院和上网次数的聚类查询学生信息
     * @param year 年份
     * @param term 学期
     * @param collegeName 学院名
     * @param timesCluster 时间聚类
     * @author LW
     * @date 2018-9-11
     * @return 无
     */
    public List<StuOnlineInfo> getTimesInfo(String year, String term, String collegeName, String timesCluster) {
        List<StuOnlineInfo> stuOnlineDurationInfo = null;
        EntityWrapper<StuOnlineInfo> wrapper = new EntityWrapper<>();
        if (null != year && !year.trim().isEmpty()) {
            wrapper.and("year", year);
        }
        if (null != term && !term.trim().isEmpty()) {
            wrapper.and("term", term);
        }
        if (null != collegeName && !collegeName.trim().isEmpty()) {
            wrapper.and("college", collegeName);
        }
        if (null != timesCluster && !timesCluster.trim().isEmpty()) {
            wrapper.and("timesCluster", timesCluster);
        }
        try {
            stuOnlineDurationInfo = stuonlineinfoMapper.selectList(wrapper);
            return stuOnlineDurationInfo;
        } catch (Exception e) {

        }
        return null;
    }

}
