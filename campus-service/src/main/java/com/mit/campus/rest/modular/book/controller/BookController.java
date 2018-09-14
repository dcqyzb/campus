package com.mit.campus.rest.modular.book.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.book.model.*;
import com.mit.campus.rest.modular.book.service.*;
import com.mit.campus.rest.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 图书测算控制类
 * </p>
 *
 * @author LW
 * @compang mitesofor
 * @creatTime 2018-09-06 14:22
 */

@RestController
@Slf4j
@RequestMapping("/bookCalculate")
@Api(value = "图书测算接口", tags = {"图书测算"})
public class BookController {
    @Autowired
    private IReaderBorrowRecordService readerBorrowRecordService;
    @Autowired
    private IReaderInfoService readerInfoService;
    @Autowired
    private IShowBookBorrowRateService showBookBorrowRateService;
    @Autowired
    private IShowBookBorrowRelateService showBookBorrowRelateService;
    @Autowired
    private IShowBookForecastService showBookForecastService;
    @Autowired
    private IShowBookRankService showBookRankService;
    @Autowired
    private IShowBookTypeRateService showBookTypeRateService;
    @Autowired
    private IShowLazyReaderService showLazyReaderService;
    @Autowired
    private IShowReaderBorrowRateService showReaderBorrowRateService;
    @Autowired
    private IShowReaderTypeRateService showReaderTypeRateService;
    @Autowired
    private IShowSubBookForecastService showSubBookForecastService;
    @Autowired
    private IShowUpsetBookService showUpsetBookService;
    @Autowired
    private ISubBookInfoService subBookInfoService;
    @Autowired
    private ISubStuBorrowRecordService subStuBorrowRecordService;
    @Autowired
    private IBookInfoService bookInfoService;

    /**
     * 获取图书排行榜
     *
     * @return 图书排行榜
     */
    @RequestMapping(value = "/getBookRank", method = RequestMethod.POST)
    @ApiOperation(value = "获取图书排行榜", notes = "获取图书排行榜")
    public Result getBookRank() {
        return Result.success(showBookRankService.findBookRank());
    }

    /**
     * 获取图书借阅占比，以图书分类为维度
     *
     * @param year 年份
     * @return 图书借阅占比
     */
    @RequestMapping(value = "/getBookBorrowRate", method = RequestMethod.POST)
    @ApiOperation(value = "获取图书借阅占比，以图书分类为维度", notes = "获取图书借阅占比，以图书分类为维度")
    public Result getBookBorrowRate(String year) {
        if (StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        //图书热门程度占比
        List<ShowBookTypeRate> bookTypeRates = showBookTypeRateService.findBookTypeRate(year);
        //各图书分类下的借阅占比
        List<ShowBookBorrowRate> bookBorrowRates = showBookBorrowRateService.findBookBorrowRate(year);
        /*
         * 将图书热门程度数据转化为：[{"name": <名称>, "value": <占比>}, ...]
         */
        JSONArray jsonArray1 = new JSONArray();
        JSONObject jsonObject1 = null;
        if (null != bookTypeRates && bookTypeRates.size() > 0) {
            for (int i = 0, len = bookTypeRates.size(); i < len; i++) {
                ShowBookTypeRate s = bookTypeRates.get(i);
                jsonObject1 = new JSONObject();
                jsonObject1.put("name", s.getBookType());
                jsonObject1.put("value", s.getBookTypeRate());
                jsonArray1.add(jsonObject1);
            }
        }
        /*
         * 同样将各图书分类的借阅占比数据转化为：[{"name": <分类名称>, "value": <占比>}, ...]
         */
        JSONArray jsonArray2 = new JSONArray();
        JSONObject jsonObject2 = null;
        if (null != bookBorrowRates && bookBorrowRates.size() > 0) {
            for (int i = 0, len = bookBorrowRates.size(); i < len; i++) {
                ShowBookBorrowRate s = bookBorrowRates.get(i);
                jsonObject2 = new JSONObject();
                jsonObject2.put("name", s.getTypeName());
                jsonObject2.put("value", s.getBookBorrowRate());
                jsonArray2.add(jsonObject2);
            }
        }
        Object[] o = {jsonArray1, jsonArray2};
        return Result.success(o);
    }

    /**
     * 获取图书借阅占比，以各学院为维度显示学院读者占比
     *
     * @param year 年份
     * @return 图书借阅占比
     */
    @RequestMapping(value = "/getReaderBorrowRate", method = RequestMethod.POST)
    @ApiOperation(value = "获取图书借阅占比，以各学院为维度显示学院读者占比", notes = "获取图书借阅占比，以各学院为维度显示学院读者占比")
    public Result getReaderBorrowRate(String year) {
        if (StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        //读者借阅程度占比
        List<ShowReaderTypeRate> readerTypeRates = showReaderTypeRateService.findReaderTypeRate(year);
        //各图书分类下的借阅占比
        List<ShowReaderBorrowRate> readerBorrowRates = showReaderBorrowRateService.findReaderBorrowRate(year);
        /*
         * 将图书热门程度数据转化为：[{"name": <名称>, "value": <占比>}, ...]
         */
        JSONArray jsonArray1 = new JSONArray();
        JSONObject jsonObject1 = null;
        if (null != readerTypeRates && readerTypeRates.size() > 0) {
            for (int i = 0, len = readerTypeRates.size(); i < len; i++) {
                ShowReaderTypeRate s = readerTypeRates.get(i);
                jsonObject1 = new JSONObject();
                jsonObject1.put("name", s.getReaderType());
                jsonObject1.put("value", s.getReaderTypeRate());
                jsonArray1.add(jsonObject1);
            }
        }
        /*
         * 同样将各图书分类的借阅占比数据转化为：[{"name": <分类名称>, "value": <占比>}, ...]
         */
        JSONArray jsonArray2 = new JSONArray();
        JSONObject jsonObject2 = null;
        if (null != readerBorrowRates && readerBorrowRates.size() > 0) {
            //去除重复的学院
            List<String> colleges = new ArrayList<String>();
            for (int i = 0, len = readerBorrowRates.size(); i < len; i++) {
                ShowReaderBorrowRate s = readerBorrowRates.get(i);
                //不包含该学院
                if (!colleges.contains(s.getCollege())) {
                    colleges.add(s.getCollege());
                    jsonObject2 = new JSONObject();
                    jsonObject2.put("name", s.getCollege());
                    jsonObject2.put("value", s.getCollegeBorrowRate());
                    jsonArray2.add(jsonObject2);
                }
            }
        }
        Object[] o = {jsonArray1, jsonArray2};
        return Result.success(o);
    }


    /**
     * 获取图书需求变化、预测情况
     *
     * @return 图书需求变化、预测情况
     */
    @RequestMapping(value = "/getBookForecast", method = RequestMethod.POST)
    @ApiOperation(value = "获取图书需求变化、预测情况", notes = "获取图书需求变化、预测情况")
    public Result getBookForecast() {
        //获取数据
        List<ShowBookForecast> list = showBookForecastService.findBookForecast();
        if (null != list && list.size() > 0) {
            // 数据处理，将其按复本数、建议购买数、超出预算数、预测数进行分类
            //每个分类的数据格式为[[<图书分类名称>, <数量>], ...]
            List<Object[]> fuben = new ArrayList<Object[]>();
            List<Object[]> jianyi = new ArrayList<Object[]>();
            List<Object[]> chaochu = new ArrayList<Object[]>();
            List<Object[]> yuce = new ArrayList<Object[]>();
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowBookForecast s = list.get(i);
                //图书分类名称
                String typeName = s.getTypeName();
                //复本数
                Object[] o_fuben = {typeName, Integer.parseInt(s.getDuplicateNum())};
                fuben.add(o_fuben);
                //建议数、超出预算数
                Object[] o_jianyi = new Object[2], o_chaochu = new Object[2];
                o_jianyi[0] = typeName;
                o_chaochu[0] = typeName;
                //复本数与预测数比较
                int compare = Integer.parseInt(s.getDuplicateNum()) - Integer.parseInt(s.getForecastNum());
                //复本数大于预测数
                if (compare > 0) {
                    o_jianyi[1] = 0;
                    o_chaochu[1] = compare;
                    //复本数小于预测数
                } else if (compare < 0) {
                    o_jianyi[1] = Math.abs(compare);
                    o_chaochu[1] = 0;
                } else {    //复本数等于预测数
                    o_jianyi[1] = 0;
                    o_chaochu[1] = 0;
                }
                jianyi.add(o_jianyi);
                chaochu.add(o_chaochu);

                //预测数
                Object[] o_yuce = {typeName, Integer.parseInt(s.getForecastNum())};
                yuce.add(o_yuce);
            }

            Object[] o = {fuben.toArray(), jianyi.toArray(), chaochu.toArray(), yuce.toArray()};
            return Result.success(o);
        }
        return Result.error("数据为空！");
    }

    /**
     * 获取懒惰读者原因分析
     *
     * @param year 年龄
     * @return 懒惰读者原因
     */
    @RequestMapping(value = "/getLazyReader", method = RequestMethod.POST)
    @ApiOperation(value = "获取懒惰读者原因分析", notes = "获取懒惰读者原因分析")
    public Result getLazyReader(String year) {
        if (StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        List<ShowLazyReader> list = showLazyReaderService.findLazyReader(year);
        if (null != list && list.size() > 0) {
            //返回数据格式：[[<原因>, <占比>], ...]
            List<Object[]> returnlist = new ArrayList<Object[]>();
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowLazyReader s = list.get(i);
                Object[] o = {s.getReason(), s.getRate()};
                returnlist.add(o);
            }
            return Result.success(returnlist);
        }
        return Result.error("数据为空！");
    }

    /**
     * getBookBorrowRelate
     * 获取图书借阅关系图
     *
     * @return Result    返回类型
     */
    @RequestMapping(value = "/getBookBorrowRelate", method = RequestMethod.POST)
    @ApiOperation(value = "获取图书借阅关系图", notes = "获取图书借阅关系图")
    public Result getBookBorrowRelate() {
        //获取图书借阅关系
        List<ShowBookBorrowRelate> list = showBookBorrowRelateService.findBookBorrowRelate();
        //图书分类字母对应关系
        Map<String, String> map = bookTypeInfo();
        if (null != list && list.size() > 0) {
            //定义节点数据、连接数据
            JSONArray nodesArray = new JSONArray(), linksArray = new JSONArray();
            //单个数据项
            JSONObject nodesObject = null, linksObject = null;
            Map<String, Integer> nodes = new HashMap<>();
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowBookBorrowRelate s = list.get(i);
                //关系对
                String typePair = s.getTypePair();
                //比率
                String rate = s.getRate();
                //关系对比率不为0，才将其返回
                if (rate.indexOf("%") != -1 &&
                        (int) (Float.parseFloat(rate.split("%")[0])) == 0) {
                    continue;
                }
                //将分类节点分开
                char[] types = new char[2];
                if (typePair.length() == 2) {
                    types = typePair.toCharArray();
                }
                //将关系对添加到map,key为图书类型字母，value为该类图书的次数
                for (int j = 0; j < types.length; j++) {
                    //都用大写
                    String typeChar = String.valueOf(types[j]).toUpperCase();
                    //已经存在该类图书，则数字加1
                    if (nodes.containsKey(typeChar)) {
                        nodes.put(typeChar, (nodes.get(typeChar) + 1));
                    } else {
                        nodes.put(typeChar, 1);
                    }
                }
                //数据类型为edge的数据项
                linksObject = new JSONObject();
                //单个连接数据项的源起点
                linksObject.put("source", map.get(String.valueOf(types[0])));
                //单个连接数据项的终节点
                linksObject.put("target", map.get(String.valueOf(types[1])));
                linksObject.put("value", Float.parseFloat(rate.split("%")[0]) * 100);
                linksArray.add(linksObject);
            }
            //node数据类型转换
            Set<String> nodesSet = nodes.keySet();
            Iterator<String> ite = nodesSet.iterator();
            int n = 0;
            while (ite.hasNext()) {
                String type = ite.next();
                nodesObject = new JSONObject();
                //数据名称
                nodesObject.put("name", map.get(type));
                nodesObject.put("value", nodes.get(type));
                nodesObject.put("category", n++);
                nodesArray.add(nodesObject);
            }
            //将节点数据与连接数据返回
            Object[] o = {nodesArray, linksArray};
            return Result.success(o);
        }
        return Result.error("数据为空！");
    }

    /**
     * 图书分类规则
     *
     * @return
     */
    private Map<String, String> bookTypeInfo() {
        String[] type = {"A&马、列、毛", "B&哲学宗教", "C&社科总论", "D&政治法律", "E&军事", "F&经济", "G&文教体育",
                "H&语言文字", "I&文学", "J&艺术", "K&历史地理", "N&自然科学总论", "O&数理化学", "P&天文地球", "Q&生物科学",
                "R&医药卫生", "S&农业科学", "T&工业技术", "U&航空、航天", "V&交通运输", "X&环境科学", "Z&综合类"};
        Map<String, String> map = new HashMap<>();
        for (int i = 0, len = type.length; i < len; i++) {
            String[] s = type[i].split("&");
            map.put(s[0], s[1]);
        }
        return map;
    }

    /**
     * 获取冷门图书原因分析
     *
     * @param year 年份
     * @return 冷门图书原因分析
     */
    @RequestMapping(value = "/getUpsetBook", method = RequestMethod.POST)
    @ApiOperation(value = "获取冷门图书原因分析", notes = "获取冷门图书原因分析")
    public Result getUpsetBook(String year) {
        if (StringUtils.isBlank(year)) {
            return Result.error("数据为空！");
        }
        List<ShowUpsetBook> list = showUpsetBookService.findUpsetBook(year);
        if (null != list && list.size() > 0) {
            //定义连接数据
            JSONArray linksArray = new JSONArray();
            JSONObject linksObject = null;
            //定义原因种类及个数
            Map<String, Integer> reasons = new HashMap<String, Integer>();
            //定义图书分类及其个数
            Map<String, Integer> typeNames = new HashMap<String, Integer>();
            for (int i = 0, len = list.size(); i < len; i++) {
                ShowUpsetBook s = list.get(i);
                String reason = s.getReason();
                String typeName = s.getTypeName();
                String num = s.getBookReasonNum();
                //统计每个原因的总个数
                if (reasons.containsKey(reason)) {
                    reasons.put(reason, reasons.get(reason) + Integer.parseInt(num));
                } else {
                    reasons.put(reason, Integer.parseInt(num));
                }
                //统计每个图书分类下的总个数
                if (typeNames.containsKey(typeName)) {
                    typeNames.put(typeName, typeNames.get(typeName) + Integer.parseInt(num));
                } else {
                    typeNames.put(typeName, Integer.parseInt(num));
                }

                linksObject = new JSONObject();
                //源节点
                linksObject.put("source", typeName);
                //目标节点
                linksObject.put("target", reason);
                //值大小
                linksObject.put("value", num);
                //添加到数组
                linksArray.add(linksObject);
            }

            //定义节点数据，需统计每个数据项的value后
            JSONArray nodesArray = new JSONArray();
            JSONObject nodesObject = null;
            Set<String> reasonSet = reasons.keySet();
            Iterator<String> reasonIte = reasonSet.iterator();
            while (reasonIte.hasNext()) {
                String r = reasonIte.next();
                nodesObject = new JSONObject();
                nodesObject.put("name", r);
                nodesObject.put("value", reasons.get(r));
                nodesObject.put("type", 0);
                nodesArray.add(nodesObject);
            }

            Set<String> typeSet = typeNames.keySet();
            Iterator<String> typeIte = typeSet.iterator();
            while (typeIte.hasNext()) {
                String t = typeIte.next();
                nodesObject = new JSONObject();
                nodesObject.put("name", t);
                nodesObject.put("value", typeNames.get(t));
                nodesObject.put("type", 1);
                nodesArray.add(nodesObject);
            }
            //将节点数据与连接数据返回
            Object[] o = {nodesArray, linksArray};
            return Result.success(o);
        }
        return Result.error("数据为空！");
    }


    /**
     * findReaderByCondition
     * 按条件查询读者信息
     *
     * @param readerName 读者姓名
     * @param college    学院
     * @param startYear  年份区间
     * @param endYear    结束年份
     * @param cluster    读者聚类，懒惰2.0，一般1.0,活跃0.0
     * @param pageNum    当前页
     * @param pageSize   每页显示几个
     * @return page读者信息对象
     */
    @RequestMapping(value = "/findReaderByCondition", method = RequestMethod.POST)
    @ApiOperation(value = "按条件查询读者信息", notes = "按条件查询读者信息,pageNum<0查询全部")
    public Result findReaderByCondition(String readerName,
                                        String college, String startYear, String endYear, String cluster,
                                        int pageNum, int pageSize) {
        //先按条件查询
        Page obj = readerInfoService.findReaderByCondition(readerName, college, startYear, endYear, cluster, pageNum, pageSize);
        return Result.success(obj, "查询完成");
    }

    /**
     * findBookByCondition
     * 按条件查询图书信息
     *
     * @param bookName  书名
     * @param typeName  书类名
     * @param startYear 年份
     * @param endYear   结束年份
     * @param cluster   借阅热度，热门，一般，冷门
     * @param pageNum   当前页
     * @param pageSize  每页显示几个
     * @return page读者信息对象
     */
    @RequestMapping(value = "/findBookByCondition", method = RequestMethod.POST)
    @ApiOperation(value = "按条件查询图书信息", notes = "按条件查询图书信息,pageNum<0查询全部")
    public Result findBookByCondition(String bookName,
                                      String typeName, String startYear, String endYear, String cluster,
                                      int pageNum, int pageSize) {
        //先按条件查询
        Page obj = bookInfoService.findBookByCondition(bookName, typeName, startYear, endYear, cluster, pageNum, pageSize);
        return Result.success(obj, "查询完成");
    }

    /**
     * findBookForecastByCondition
     * 按条件查询图书需求分析信息
     *
     * @param bookName 书名
     * @param typeName 书类名
     * @param pageNum  当前页
     * @param pageSize 每页显示几个
     * @return 图书需求分析信息page对象
     */
    @RequestMapping(value = "/findBookForecastByCondition", method = RequestMethod.POST)
    @ApiOperation(value = "按条件查询图书需求分析信息", notes = "按条件查询图书需求分析信息,pageNum<0查询全部")
    public Result findBookForecastByCondition(String bookName,
                                              String typeName,
                                              int pageNum, int pageSize) {
        //先按条件查询
        Page obj = showSubBookForecastService.findBookForecastByCondition(bookName, typeName, pageNum, pageSize);
        return Result.success(obj, "查询完成");
    }

    /**
     * getBorrowType
     * 获取该学生某年借书的类型占比
     *
     * @param readerID 读者学号
     * @param year     年份
     * @return 该学生某年借书的类型占比
     */
    @RequestMapping(value = "/getborrowType", method = RequestMethod.POST)
    @ApiOperation(value = "获取该学生某年借书的类型占比", notes = "获取该学生某年借书的类型占比,pageNum<0查询全部")
    public Result getBorrowType(String readerID, String year) {
        //验证参数
        if (StringUtils.isBlank(readerID) || StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        //先按条件查询
        List<BookInfo> books = bookInfoService.getBorrowType(readerID, year);
        if (books == null || books.isEmpty()) {
            return Result.error("查询结果为空");
        }
        //将书类名存入HashSet
        HashSet<String> typeNames = new HashSet<>();
        for (int k = 0, len = books.size(); k < len; k++) {
            typeNames.add(books.get(k).getTypeName());
        }
        //每类书的个数
        int typeCount;
        //总借书数
        int sum = books.size();
        /*
         * 将各图书分类的借阅占比数据转化为：[{"name": <分类名称>, "value": <占比>}, ...]
         */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for (String typeName : typeNames) {
            typeCount = 0;
            for (int m = 0, len = books.size(); m < len; m++) {
                if (books.get(m).getTypeName().equals(typeName)) {
                    typeCount++;
                }
            }
            jsonObject = new JSONObject();
            jsonObject.put("name", typeName);
            jsonObject.put("value", (float) typeCount / sum);
            jsonArray.add(jsonObject);
        }
        return Result.success(jsonArray);
    }

    /**
     * getReaderBorrowReacord
     * 获取读者借阅记录
     *
     * @param readerID 读者编号
     * @param year     年份
     * @return 该读者借阅记录
     */
    @RequestMapping(value = "/getBorrowRecordByReaderID", method = RequestMethod.POST)
    @ApiOperation(value = "获取读者借阅记录", notes = "获取读者借阅记录")
    public Result getBorrowRecordByReaderID(String readerID, String year) {
        //验证参数
        if (StringUtils.isBlank(readerID) || StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        //先按条件查询借阅记录和图书信息
        Map<ReaderBorrowRecord, BookInfo> map = readerBorrowRecordService.getBorrowRecordByReaderID(readerID, year);
        //借阅的时间记录（不重复的时间）
        List<ReaderBorrowRecord> records_list = readerBorrowRecordService.getBorrowRecordDate(readerID, "", year);
        if (map == null && map.size() == 0) {
            return Result.error("查询为空");
        }
        /*
         * 数据转化为：[{"name": <借阅时间>, "value": <{书名，作者，出版社}，{}，{}>}, ...]
         */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        //遍历不重复借阅的时间记录，保证了记录时间的降序
        for (ReaderBorrowRecord item : records_list) {
            String date = item.getDate();
            List<String[]> list = new ArrayList<String[]>();
            for (Map.Entry<ReaderBorrowRecord, BookInfo> entry : map.entrySet()) {
                if (entry.getKey().getDate().equals(date)) {
                    BookInfo book = entry.getValue();
                    String[] str = {book.getBookName(),
                            book.getAuthor(),
                            book.getPublish()};
                    list.add(str);
                }
            }
            if (list != null && list.size() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("name", date.split(" ")[0]);
                jsonObject.put("value", list);
                jsonArray.add(jsonObject);
            }
        }
        return Result.success(jsonArray);
    }

    /**
     * getBookCollegeRate
     * 获取某本书某年，学院借该书的占比
     *
     * @param bookID 书id
     * @param year   年份
     * @return 某本书某年，学院借该书的占比
     */
    @RequestMapping(value = "/getBookCollegeRate", method = RequestMethod.POST)
    @ApiOperation(value = "获取某本书某年，学院借该书的占比", notes = "获取某本书某年，学院借该书的占比")
    public Result getBookCollegeRate(String bookID, String year) {
        //验证参数
        if (StringUtils.isBlank(bookID) || StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        //先按条件查询
        List<ReaderInfo> readers = readerInfoService.getBookCollegeRate(bookID, year);
        if (readers == null && readers.size() == 0) {
            return Result.error("查询为空");
        }
        //将书类名存入HashSet
        HashSet<String> colleges = new HashSet<>();
        for (int k = 0, len = readers.size(); k < len; k++) {
            colleges.add(readers.get(k).getCollegeName());
        }
        //每类书的个数
        int typeCount;
        //被借总次数
        int sum = readers.size();
        /*
         * 将各学院的借阅占比数据转化为：[{"name": <学院>, "value": <占比>}, ...]
         */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for (String college : colleges) {
            typeCount = 0;
            for (int m = 0, len = readers.size(); m < len; m++) {
                if (readers.get(m).getCollegeName().equals(college)) {
                    typeCount++;
                }
            }
            jsonObject = new JSONObject();
            jsonObject.put("name", college);
            jsonObject.put("value", (float) typeCount / sum);
            jsonArray.add(jsonObject);
        }
        return Result.success(jsonArray);
    }

    /**
     * getBookBorrowReacord
     * 获取图书借阅记录
     *
     * @param bookID 书籍编号
     * @param year   年份
     * @return 图书借阅记录
     */
    @RequestMapping(value = "/getBorrowRecordByBookID", method = RequestMethod.POST)
    @ApiOperation(value = "获取图书借阅记录", notes = "获取图书借阅记录")
    public Result getBorrowRecordByBookID(String bookID, String year) {
        //验证参数
        if (StringUtils.isBlank(bookID) || StringUtils.isBlank(year)) {
            return Result.error("参数不能为空");
        }
        //先按条件查询
        Map<ReaderBorrowRecord, ReaderInfo> map = readerBorrowRecordService.getBorrowRecordByBookID(bookID, year);
        //借阅的时间记录（不重复的时间）
        List<ReaderBorrowRecord> records_list = readerBorrowRecordService.getBorrowRecordDate("", bookID, year);
        if (map == null && map.size() == 0) {
            return Result.error("查询为空");
        }
        //将书类名存入HashSet
        HashSet<String> dates = new HashSet<>();
        for (ReaderBorrowRecord key : map.keySet()) {
            dates.add(key.getDate());
        }
        /*
         * 数据转化为：[{"name": <借阅时间>, "value": <{学号，姓名，学院}，{}，{}>}, ...]
         */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        //遍历不重复借阅的时间记录，保证了记录时间的降序
        for (ReaderBorrowRecord item : records_list) {
            String date = item.getDate();
            List<String[]> list = new ArrayList<String[]>();
            for (Map.Entry<ReaderBorrowRecord, ReaderInfo> entry : map.entrySet()) {
                if (entry.getKey().getDate().equals(date)) {
                    ReaderInfo reader = entry.getValue();
                    String[] str = {reader.getReaderID(),
                            reader.getReaderName(),
                            reader.getCollegeName()};
                    list.add(str);
                }
            }
            if (list != null && list.size() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("name", date.split(" ")[0]);
                jsonObject.put("value", list);
                jsonArray.add(jsonObject);
            }
        }
        return Result.success(jsonArray);
    }

    /**
     * getSubBookInfo
     * 二级页面-图书需求趋势的二级类别下具体书信息
     *
     * @param bookID 书籍编号
     * @return 图书需求趋势的二级类别下具体书信息
     */
    @RequestMapping(value = "/getSubBookInfo", method = RequestMethod.POST)
    @ApiOperation(value = "二级页面-图书需求趋势的二级类别下具体书信息", notes = "二级页面-图书需求趋势的二级类别下具体书信息")
    public Result getSubBookInfo(String bookID) {
        //先按条件查询
        List<SubBookInfo> obj = subBookInfoService.getSubBookInfo(bookID);
        return Result.success(obj, "查询完成");
    }

    /**
     * getSubBookInfo
     * 二级页面-查看具体书的借阅关系
     *
     * @param bookID 书籍编号
     * @return 查看具体书的借阅关系
     */
    @RequestMapping(value = "/getSubBookBorrowRelate", method = RequestMethod.POST)
    @ApiOperation(value = "二级页面-查看具体书的借阅关系", notes = "二级页面-查看具体书的借阅关系")
    public Result getSubBookBorrowRelate(String bookID, String bookName) {
        //获取图书借阅关系
        List<SubStuBorrowRecord> list = subStuBorrowRecordService.getSubBookBorrowRelate(bookID);
        //总数据量
        int all_count = subStuBorrowRecordService.getSubStuBorrowRecordCount();
        if (null != list && list.size() > 0) {
            //定义节点数据、连接数据
            JSONArray nodesArray = new JSONArray(), linksArray = new JSONArray();
            //单个数据项
            JSONObject nodesObject = null, linksObject = null;
            Map<String, Integer> nodes = new HashMap<>();
            for (int i = 0, len = list.size(); i < len; i++) {
                SubStuBorrowRecord s = list.get(i);
                //在借书id
                String[] bookIDs = s.getBorrowRecord().split(",");

                //将关系对添加到map,key为图书类型字母，value为该类图书的次数
                for (int j = 0; j < bookIDs.length; j++) {
                    if (!bookID.equals(bookIDs[j])) {
                        String typeChar = bookID + "," + bookIDs[j];
                        //已经存在该对，则数字加1
                        if (nodes.containsKey(typeChar)) {
                            nodes.put(typeChar, (nodes.get(typeChar) + 1));
                        } else {
                            nodes.put(typeChar, 1);
                        }
                    }
                }
            }
            //该书的信息点
            nodesObject = new JSONObject();
            //书名
            nodesObject.put("name", bookName);
            //相关书个数
            nodesObject.put("value", list.size());
            nodesObject.put("category", 0);
            nodesArray.add(nodesObject);

            int n = 1;
            for (String key : nodes.keySet()) {
                //数据类型为edge的数据项
                linksObject = new JSONObject();
                //单个连接数据项的源起点
                linksObject.put("source", bookName);
                //单个连接数据项的终节点
                linksObject.put("target", bookInfoService.getBookName(key.split(",")[1]));
                linksObject.put("value", nodes.get(key) / (float) all_count);
                linksArray.add(linksObject);

                //添加相关书的信息点
                nodesObject = new JSONObject();
                //书名
                nodesObject.put("name", bookInfoService.getBookName(key.split(",")[1]));
                //相关书个数
                nodesObject.put("value", 1);
                nodesObject.put("category", n++);
                nodesArray.add(nodesObject);
            }
            //将节点数据与连接数据返回
            Object[] o = {nodesArray, linksArray};
            return Result.success(o);
        }
        return Result.error("数据为空");
    }
}
