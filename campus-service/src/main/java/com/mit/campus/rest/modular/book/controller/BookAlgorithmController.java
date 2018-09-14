package com.mit.campus.rest.modular.book.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.campus.rest.algorithm.AprioriRuleModel;
import com.mit.campus.rest.algorithm.model.*;
import com.mit.campus.rest.modular.book.dao.*;
import com.mit.campus.rest.modular.book.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 图书测算的无敌算法控制层
 * </p>
 *
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 17:25
 */
@RestController
@Slf4j
@RequestMapping("/algorithmCore")
@Api(value="图书测算算法接口",tags={"图书测算算法"})
public class BookAlgorithmController {
    @Autowired
    ReaderinfoMapper readerinfoMapper;
    @Autowired
    ShowReadertyperateMapper showReadertyperateMapper;
    @Autowired
    BookaprioriinputMapper bookaprioriinputMapper;
    @Autowired
    BookinfoMapper bookinfoMapper;
    @Autowired
    ShowBooktyperateMapper showBooktyperateMapper;
    @Autowired
    BooksequationMapper booksequationMapper;
    @Autowired
    BookcirculationinfoMapper bookcirculationinfoMapper;
    @Autowired
    ShowBookforecastMapper showBookforecastMapper;
    @Autowired
    SubbookcirculationinfoMapper subbookcirculationinfoMapper;
    @Autowired
    SubbooksequationMapper subbooksequationMapper;
    @Autowired
    ShowSubbookforecastMapper showSubbookforecastMapper;
    @Autowired
    ReaderaprioriruleMapper readeraprioriruleMapper;
    @Autowired
    ReaderaprioriinputMapper readeraprioriinputMapper;
    @Autowired
    BookaprioriruleMapper bookaprioriruleMapper;
    @Autowired
    BookaprioriforecastMapper bookaprioriforecastMapper;
    @Autowired
    StuborrowrecordMapper stuborrowrecordMapper;

    /**
     * 将读者按图书借阅次数进行聚类,并将统计结果存入前端接口，用到的表ReaderInfo，ShowReaderTypeRate
     *
     * @throws NumberInvalieException 数据无效异常
     * @author lw
     * @date 2018-9-6
     */
    @RequestMapping(value = "/getReaderCluster", method = RequestMethod.POST)
    @ApiOperation(value="读者的聚类分类", notes="不返回任何东西")
    public void getReaderCluster() throws NumberInvalieException {
        // 获取所有的读者信息
        List<ReaderInfo> reader = readerinfoMapper.selectList(null);
        // 将所有读者的图书借阅次数排序，取两头和二者平均数作为聚类的初始中心点
        List<String> arrBorrowTimes = new ArrayList<String>();
        for (ReaderInfo r : reader) {
            arrBorrowTimes.add(r.getBorrowTimes());
        }
        KAverage ka = new KAverage(arrBorrowTimes);
        // 将聚类结果存入读者信息表
        double[][] sampleValues = ka.doCaculate(3);
        for (int m = 0; m < sampleValues.length; m++) {
            reader.get(m).setCluster(Double.toString(sampleValues[m][1]));
            readerinfoMapper.updateAllColumnById(reader.get(m));
        }
        // 根据读者信息统计读者类型占比
        ShowReaderTypeRate readTypeRate = null;
        String[] year = {"2013", "2014", "2015", "2016", "2017"};
        String[] type = {"活跃", "一般", "懒惰"};
        for (int m = 0; m < year.length; m++) {
            for (int n = 0; n < type.length; n++) {
                readTypeRate = new ShowReaderTypeRate();
                readTypeRate.setYear(year[m]);// 学年
                readTypeRate.setReaderType(type[n]);// 类型
                int count = findReaderByCondition(year[m], type[n]);// 获取符合条件的读者个数
                if (count != 0) {
                    float rate = (float) (year.length * count) / reader.size();// 计算该类读者类型的占比
                    readTypeRate.setReaderTypeRate(Float.toString(rate));
                    showReadertyperateMapper.insert(readTypeRate);
                }
            }
        }
    }


    /**
     * 根据年份和读者类型查找读者
     */
    public int findReaderByCondition(String year, String type) {
        String cluster = "";
        // 此处有待改进，kmeans算法每次的分组有可能会变
        if (type.trim().equals("懒惰")) {
            cluster = "2.0";
        } else if (type.trim().equals("一般")) {
            cluster = "1.0";
        }
        if (type.trim().equals("活跃")) {
            cluster = "0.0";
        }
        EntityWrapper<ReaderInfo> wrapper = new EntityWrapper<>();
        if (null != year && !year.trim().isEmpty()) {
            wrapper.and("schoolYear={0}", year);
        }
        if (null != type && !type.trim().isEmpty()) {
            wrapper.and("cluster={0}", cluster);
        }
        try {
            int count = readerinfoMapper.selectCount(wrapper);
            return count;
        } catch (Exception e) {

        }
        return 0;
    }


    /**
     * 将图书按图书借阅次数进行聚类,并将统计结果存入前端接口，所用表有BookInfo，ShowBookTypeRate
     *
     * @throws NumberInvalieException 数值无效异常
     * @author lw
     * @date 2018-9-6
     */
    @RequestMapping(value = "/getBookCluster", method = RequestMethod.POST)
    @ApiOperation(value="书籍的聚类分类", notes="不返回任何东西")
    public void getBookCluster() throws NumberInvalieException {

        // 获取所有的图书信息
        List<BookInfo> book = bookinfoMapper.selectList(null);
        // 将所有图书的图书借阅次数排序，取两头和二者平均数作为聚类的初始中心点
        List<String> arrBorrowTimes = new ArrayList<String>();
        for (BookInfo b : book) {
            arrBorrowTimes.add(b.getBorrowTimes());
        }
        KAverage ka = new KAverage(arrBorrowTimes);
        // 将聚类结果存入图书信息表
        double[][] sampleValues = ka.doCaculate(3);
        for (int m = 0; m < sampleValues.length; m++) {
            book.get(m).setCluster(Double.toString(sampleValues[m][1]));
            bookinfoMapper.updateAllColumnById(book.get(m));
        }
        // 根据图书信息统计图书类型占比
        ShowBookTypeRate bookTypeRate = null;
        String[] year = {"2013", "2014", "2015", "2016", "2017"};// 近五年
        String[] type = {"热门", "一般", "冷门"};// 图书类型
        for (int m = 0; m < year.length; m++) {
            for (int n = 0; n < type.length; n++) {
                bookTypeRate = new ShowBookTypeRate();
                bookTypeRate.setYear(year[m]);// 学年
                bookTypeRate.setBookType(type[n]);// 图书类型
                int count = findBookByCondition(year[m], type[n]);
                if (count != 0) {
                    float rate = (float) (year.length * count) / book.size();// 占比
                    bookTypeRate.setBookTypeRate(Float.toString(rate));
                    showBooktyperateMapper.insert(bookTypeRate);
                }
            }
        }
    }

    /**
     * 根据年份和图书类型查找图书
     *
     * @param year 年份
     * @param type 类型
     * @return 书籍数量
     */
    public int findBookByCondition(String year, String type) {
        String cluster = "";
        // 此处有待改进，kmeans算法每次的分组有可能会变
        if (type.trim().equals("冷门")) {
            cluster = "2.0";
        } else if (type.trim().equals("一般")) {
            cluster = "1.0";
        }
        if (type.trim().equals("热门")) {
            cluster = "0.0";
        }
        EntityWrapper<BookInfo> wrapper = new EntityWrapper<>();
        if (null != year && !year.trim().isEmpty()) {
            wrapper.and("year", year);
        }
        if (null != type && !type.trim().isEmpty()) {
            wrapper.and("cluster", cluster);
        }
        try {
            int count = bookinfoMapper.selectCount(wrapper);
            return count;
        } catch (Exception e) {

        }
        return 0;
    }


    /**
     * forecastDuplicateNum
     * 通过回归分析算法预测图书复本数，所用表有BookSequation，BookCirculationInfo
     *
     * @author lw
     * @date ：2018-9-6
     */
    @RequestMapping(value = "/forecastDuplicateNum", method = RequestMethod.POST)
    @ApiOperation(value="回归分析算法预测图书复本数", notes="不返回任何东西")
    public void forecastDuplicateNum() {
        BookSequation bookSequation = null;
        // 所有图书类型，共22类
        String[] bookType = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "X", "Z"};
        for (int i = 0; i < bookType.length; i++) {
            bookSequation = new BookSequation();
            bookSequation.setType(bookType[i]);// 图书类型
            List<BookCirculationInfo> books = getBookCalculateData(bookType[i]);
            // 图书借阅量
            List<Double> arrBorrowTimes = new ArrayList<Double>();
            // 新书总册数
            List<Double> arrNewBookTotalNum = new ArrayList<Double>();
            // 新书总种数
            List<Double> arrNewBookTotalType = new ArrayList<Double>();
            // 总藏书册数
            List<Double> arrTotalNum = new ArrayList<Double>();
            // 文献利用率
            List<Double> arrBookUseRate = new ArrayList<Double>();
            // 新书复本率
            List<Double> arrDuplicateNum = new ArrayList<Double>();
            for (BookCirculationInfo bs : books) {
                // 将五个自变量和一个因变量传入回归算法中
                arrBorrowTimes.add(Double.parseDouble(bs.getBorrowTimes()));
                arrNewBookTotalNum.add(Double.parseDouble(bs.getNewBookTotalNum()));
                arrNewBookTotalType.add(Double.parseDouble(bs.getNewBookTotalType()));
                arrTotalNum.add(Double.parseDouble(bs.getTotalNum()));
                arrBookUseRate.add(Double.parseDouble(bs.getBookUseRate()));
                arrDuplicateNum.add(Double.parseDouble(bs.getDuplicateNum()));
            }
            double[][] x = new double[5][arrBorrowTimes.size()];
            for (int j = 0; j < arrBorrowTimes.size(); j++) {
                x[0][j] = arrBorrowTimes.get(j);
            }
            for (int j = 0; j < arrNewBookTotalNum.size(); j++) {
                x[1][j] = arrNewBookTotalNum.get(j);
            }
            for (int j = 0; j < arrNewBookTotalType.size(); j++) {
                x[2][j] = arrNewBookTotalType.get(j);
            }
            for (int j = 0; j < arrTotalNum.size(); j++) {
                x[3][j] = arrTotalNum.get(j);
            }
            for (int j = 0; j < arrBookUseRate.size(); j++) {
                x[4][j] = arrBookUseRate.get(j);
            }
            double[] y = new double[arrBorrowTimes.size()];
            for (int m = 0; m < arrDuplicateNum.size(); m++) {
                y[m] = arrDuplicateNum.get(m);
            }
            double coeff;
            // 自变量的个数
            int m = x.length;
            // 观察数据的组数
            int n = y.length;
            // 相关系数过滤之后的矩阵
            double[][] mainMatrix;
            double[][] resultMatrix;
            // 相关系数过滤之后的自变量
            String mainVal = "";
            // 最后的回归方程
            String sequation = "";

            // 计算自变量与因变量的相关系数，值越接近1表示越相关
            for (int s = 0; s < m; s++) {
                coeff = Coeff.coeff(x[s], y);
                System.out.println("第" + s + "个自变量与因变量的相关系数为： " + coeff);
                if (coeff > 0.5 || coeff < -0.5) {
                    // 符合条件的自变量下标
                    mainVal += s + "&";
                }
            }
            if (mainVal.length() == 0) {
                System.out.println(bookType[i] + " 无相关变量");
            } else {
                int valNum = mainVal.split("&").length;
                String[] valString = mainVal.split("&");
                mainMatrix = new double[valNum][n];
                for (int t = 0; t < valNum; t++) {
                    int index = Integer.parseInt(valString[t]);
                    mainMatrix[t] = x[index];
                }

                resultMatrix = ManyResgress.manyResgress(mainMatrix, y);
                if (resultMatrix == null) {
                    System.out.println(bookType[i] + " 检验过滤后的线性方程为空！");
                    bookSequation.setSequation("");
                    bookSequation.setNewVal(null);
                    // 表示需要重新计算模型，resultMatrix代表剩余的变量
                } else if (resultMatrix[0].length == y.length) {
                    // 多元线性回归
                    if (resultMatrix.length >= 2) {
                        double[][] newresultMatrix = ManyResgress.manyResgress(resultMatrix, y);
                        for (int v = 0; v < newresultMatrix[0].length - 1; v++) {
                            sequation += newresultMatrix[0][v] + "x" + (v + 1) + "+";
                        }
                        sequation = "y=" + sequation + newresultMatrix[0][newresultMatrix[0].length - 1];
                        System.out.println(bookType[i] + "检验过滤后的线性方程为 ：" + sequation);
                        bookSequation.setSequation(sequation);
                        // 找到剩余变量中最小的值存入数据库，以便代入方程计算复本数
                        String newVal = "";
                        double min = 0.0;
                        for (int a = 0; a < resultMatrix.length; a++) {
                            List<Double> arr = new ArrayList<>();
                            for (int b = 0; b < resultMatrix[a].length; b++) {
                                arr.add(resultMatrix[a][b]);
                            }
                            min = Collections.min(arr);
                            newVal += min + "&";
                        }
                        bookSequation.setNewVal(newVal);
                        // 一元线性回归，resultMatrix代表以为矩阵
                    } else if (resultMatrix.length == 1) {
                        sequation = OneResgress.oneResgress(resultMatrix, y);
                        System.out.println(bookType[i] + "检验过滤后的线性方程为 ：" + sequation);
                        bookSequation.setSequation(sequation);
                        // 找到剩余变量中最小的值存入数据库，以便代入方程计算复本数
                        String newVal = "";
                        double min = 0.0;
                        for (int a = 0; a < resultMatrix.length; a++) {
                            List<Double> arr = new ArrayList<Double>();
                            for (int b = 0; b < resultMatrix[a].length; b++) {
                                arr.add(resultMatrix[a][b]);
                            }
                            min = Collections.min(arr);
                            newVal += min + "&";
                        }
                        bookSequation.setNewVal(newVal);
                    } else {
                        return;
                    }
                } else {
                    // 表示模型检验通过，resultMatrix代表方程系数，此处表示没有剔除变量，矩阵为mainMatrix
                    for (int v = 0; v < resultMatrix[0].length - 1; v++) {
                        sequation += resultMatrix[0][v] + "x" + (v + 1) + "+";
                    }
                    sequation = "y=" + sequation + resultMatrix[0][resultMatrix[0].length - 1];
                    System.out.println(bookType[i] + "检验过滤后的线性方程为 ：" + sequation);
                    bookSequation.setSequation(sequation);
                    // 找到剩余变量中最小的值存入数据库，以便代入方程计算复本数
                    String newVal = "";
                    double min = 0.0;
                    for (int a = 0; a < resultMatrix.length; a++) {
                        List<Double> arr = new ArrayList<>();
                        for (int b = 0; b < resultMatrix[a].length; b++) {
                            arr.add(resultMatrix[a][b]);
                        }
                        min = Collections.min(arr);
                        newVal += min + "&";
                    }
                    bookSequation.setNewVal(newVal);
                }
            }
            booksequationMapper.insert(bookSequation);
        }
    }


    /**
     * 根据图书类查询图书信息
     *
     * @param bookType 书本类型
     * @return BookCirculationInfo 列表
     */
    public List<BookCirculationInfo> getBookCalculateData(String bookType) {
        List<BookCirculationInfo> findBooks = null;
        EntityWrapper<BookCirculationInfo> wrapper = new EntityWrapper<>();
        if (null != bookType && !bookType.trim().isEmpty()) {
            wrapper.and("bookType={0}", bookType);
        }
        try {
            findBooks = bookcirculationinfoMapper.selectList(wrapper);
            if (null != findBooks && findBooks.size() > 0) {
                return findBooks;
            }
        } catch (Exception e) {

        }
        return null;

    }


    /**
     * 过上上面的接口计算回归方程，再通过方程计算复本数
     *
     * @author lw
     * @date ：2018-9-7
     */
    @RequestMapping(value = "/getDuplicateNumBySequation", method = RequestMethod.POST)
    @ApiOperation(value="方程计算复本数", notes="不返回任何东西")
    public void getDuplicateNumBySequation() {
        // 获取所有的图书回归方程
        List<BookSequation> sq = booksequationMapper.selectList(null);
        // 将自变量代入方程
        for (BookSequation s : sq) {
            String sequation = "";
            sequation = s.getSequation();
            if (s.getNewVal().split("&").length == 1) {
                sequation = sequation.replace("x", "*" + s.getNewVal().split("&")[0]);
            } else {
                for (int i = 0; i < s.getNewVal().split("&").length; i++) {
                    sequation = sequation.replace("x" + (i + 1), "*" + s.getNewVal().split("&")[i]);
                }
            }
            // 获取方程右边的多项式
            String rightSequation = sequation.split("=")[1];
            // duplicateNum为图书复本数，步骤为：常量+a1x1+a2x2+....
            double duplicateNum = Double
                    // java中用+号分割需加//
                    .parseDouble(rightSequation.split("\\+")[rightSequation.split("\\+").length - 1]);
            for (int m = 0; m < rightSequation.split("\\+").length - 1; m++) {
                duplicateNum += (Double.parseDouble(rightSequation.split("\\+")[m].split("\\*")[0]))
                        * (Double.parseDouble(rightSequation.split("\\+")[m].split("\\*")[1]));
            }
            // 获取每类图书的回归方程，将将计算出来的复本数set进去
            List<ShowBookForecast> typeBook = getBookByType(s.getType());
            if (typeBook.size() != 0) {
                for (ShowBookForecast tb : typeBook) {
                    int a = Integer.parseInt(new DecimalFormat("0").format(duplicateNum));
                    tb.setForecastNum(String.valueOf(a));
                    showBookforecastMapper.updateAllColumnById(tb);
                }
            }
        }
    }

    /**
     * 根据图书类查询图书预测信息
     *
     * @param type 类型
     * @return ShowBookForecast列表
     */
    public List<ShowBookForecast> getBookByType(String type) {
        List<ShowBookForecast> getBookByType = null;
        EntityWrapper<ShowBookForecast> wrapper = new EntityWrapper<>();
        if (null != type && !type.trim().isEmpty()) {
            wrapper.and("type={0}", type);
        }
        try {
            showBookforecastMapper.selectList(wrapper);
            if (null != getBookByType && getBookByType.size() > 0) {
                return getBookByType;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 通过回归分析算法预测每本复本数，所用表有SubBookSequation，SubBookCirculationInfo，二级页面
     *
     * @author lw
     * @date: 2018-9-7
     */
    @RequestMapping(value = "/forecastSubDuplicateNum", method = RequestMethod.POST)
    @ApiOperation(value="回归分析算法预测每本复本数", notes="二级页面，所用表SubBookSequation，SubBookCirculationInfo，不返回任何数据")
    public void forecastSubDuplicateNum() {
        SubBookSequation bookSequation = null;
        // 所有图书类型，共22类
        String[] bookName = {"Spark大数据处理： 原理、算法与实例", "学电脑从入门到精通", "基于模型的设计及其嵌入式实现", "数学分析", "高等代数", "大学英语", "大学物理", "大学语文", "计算思维与计算文化", "中国上下五千年", "平凡世界", "金粉世家", "中国共产党章程", "Java高级编程", "泛函分析", "矩阵论", "物联网与泛在网通信技术", "Matlab基础教程", "明朝那些事", "穆斯林的葬礼", "JavaScript从入门到精通", "大学英语四级"};
        for (int i = 0; i < bookName.length; i++) {
            bookSequation = new SubBookSequation();
            bookSequation.setName(bookName[i]);// 图书类型
            List<SubBookCirculationInfo> books = getSubBookCalculateData(bookName[i]);
            // 图书借阅量
            List<Double> arrBorrowTimes = new ArrayList<Double>();
            // 新书总册数
            List<Double> arrNewBookTotalNum = new ArrayList<Double>();
            // 新书总种数
            List<Double> arrNewBookTotalType = new ArrayList<Double>();
            // 总藏书册数
            List<Double> arrTotalNum = new ArrayList<Double>();
            // 文献利用率
            List<Double> arrBookUseRate = new ArrayList<Double>();
            // 新书复本率
            List<Double> arrDuplicateNum = new ArrayList<Double>();
            for (SubBookCirculationInfo bs : books) {
                // 将五个自变量和一个因变量传入回归算法中
                arrBorrowTimes.add(Double.parseDouble(bs.getBorrowTimes()));
                arrNewBookTotalNum.add(Double.parseDouble(bs.getNewBookTotalNum()));
                arrNewBookTotalType.add(Double.parseDouble(bs.getNewBookTotalType()));
                arrTotalNum.add(Double.parseDouble(bs.getTotalNum()));
                arrBookUseRate.add(Double.parseDouble(bs.getBookUseRate()));
                arrDuplicateNum.add(Double.parseDouble(bs.getDuplicateNum()));
            }
            double[][] x = new double[5][arrBorrowTimes.size()];
            for (int j = 0; j < arrBorrowTimes.size(); j++) {
                x[0][j] = arrBorrowTimes.get(j);
            }
            for (int j = 0; j < arrNewBookTotalNum.size(); j++) {
                x[1][j] = arrNewBookTotalNum.get(j);
            }
            for (int j = 0; j < arrNewBookTotalType.size(); j++) {
                x[2][j] = arrNewBookTotalType.get(j);
            }
            for (int j = 0; j < arrTotalNum.size(); j++) {
                x[3][j] = arrTotalNum.get(j);
            }
            for (int j = 0; j < arrBookUseRate.size(); j++) {
                x[4][j] = arrBookUseRate.get(j);
            }
            double[] y = new double[arrBorrowTimes.size()];
            for (int m = 0; m < arrDuplicateNum.size(); m++) {
                y[m] = arrDuplicateNum.get(m);
            }
            double coeff;
            // 自变量的个数
            int m = x.length;
            // 观察数据的组数
            int n = y.length;
            // 相关系数过滤之后的矩阵
            double[][] mainMatrix;
            double[][] resultMatrix;
            // 相关系数过滤之后的自变量
            String mainVal = "";
            // 最后的回归方程
            String sequation = "";

            // 计算自变量与因变量的相关系数，值越接近1表示越相关
            for (int s = 0; s < m; s++) {
                coeff = Coeff.coeff(x[s], y);
                System.out.println("第" + s + "个自变量与因变量的相关系数为：" + coeff);
                if (coeff > 0.5 || coeff < -0.5) {
                    // 符合条件的自变量下标
                    mainVal += s + "&";
                }
            }
            if (mainVal.length() == 0) {
                System.out.println(bookName[i] + "无相关变量");
            } else {
                int valNum = mainVal.split("&").length;
                String[] valString = mainVal.split("&");
                mainMatrix = new double[valNum][n];
                for (int t = 0; t < valNum; t++) {
                    int index = Integer.parseInt(valString[t]);
                    mainMatrix[t] = x[index];
                }

                resultMatrix = ManyResgress.manyResgress(mainMatrix, y);
                if (resultMatrix == null) {
                    System.out.println(bookName[i] + "检验过滤后的线性方程为空！");
                    bookSequation.setSequation("");
                    bookSequation.setNewVal(null);
                } else if (resultMatrix[0].length == y.length) {
                    // 表示需要重新计算模型，resultMatrix代表剩余的变量
                    if (resultMatrix.length >= 2) {
                        // 多元线性回归
                        double[][] newresultMatrix = ManyResgress.manyResgress(resultMatrix, y);
                        for (int v = 0; v < newresultMatrix[0].length - 1; v++) {
                            sequation += newresultMatrix[0][v] + "x" + (v + 1) + "+";
                        }
                        sequation = "y=" + sequation + newresultMatrix[0][newresultMatrix[0].length - 1];
                        System.out.println(bookName[i] + "检验过滤后的线性方程为：" + sequation);
                        bookSequation.setSequation(sequation);
                        // 找到剩余变量中最小的值存入数据库，以便代入方程计算复本数
                        String newVal = "";
                        double min = 0.0;
                        for (int a = 0; a < resultMatrix.length; a++) {
                            List<Double> arr = new ArrayList<Double>();
                            for (int b = 0; b < resultMatrix[a].length; b++) {
                                arr.add(resultMatrix[a][b]);
                            }
                            min = Collections.min(arr);
                            newVal += min + "&";
                        }
                        bookSequation.setNewVal(newVal);
                    } else if (resultMatrix.length == 1) {
                        // 一元线性回归，resultMatrix代表以为矩阵
                        sequation = OneResgress.oneResgress(resultMatrix, y);
                        System.out.println(bookName[i] + "检验过滤后的线性方程为：" + sequation);
                        bookSequation.setSequation(sequation);
                        // 找到剩余变量中最小的值存入数据库，以便代入方程计算复本数
                        String newVal = "";
                        double min = 0.0;
                        for (int a = 0; a < resultMatrix.length; a++) {
                            List<Double> arr = new ArrayList<Double>();
                            for (int b = 0; b < resultMatrix[a].length; b++) {
                                arr.add(resultMatrix[a][b]);
                            }
                            min = Collections.min(arr);
                            newVal += min + "&";
                        }
                        bookSequation.setNewVal(newVal);
                    } else {
                        return;
                    }
                } else {
                    // 表示模型检验通过，resultMatrix代表方程系数，此处表示没有剔除变量，矩阵为mainMatrix
                    for (int v = 0; v < resultMatrix[0].length - 1; v++) {
                        sequation += resultMatrix[0][v] + "x" + (v + 1) + "+";
                    }
                    sequation = "y=" + sequation + resultMatrix[0][resultMatrix[0].length - 1];
                    System.out.println(bookName[i] + "检验过滤后的线性方程为：" + sequation);
                    bookSequation.setSequation(sequation);
                    // 找到剩余变量中最小的值存入数据库，以便代入方程计算复本数
                    String newVal = "";
                    double min = 0.0;
                    for (int a = 0; a < resultMatrix.length; a++) {
                        List<Double> arr = new ArrayList<Double>();
                        for (int b = 0; b < resultMatrix[a].length; b++) {
                            arr.add(resultMatrix[a][b]);
                        }
                        min = Collections.min(arr);
                        newVal += min + "&";
                    }
                    bookSequation.setNewVal(newVal);
                }
            }
            bookSequation.insert();
        }
    }

    /**
     * 根据图书查询图书信息，二级页面
     *
     * @param bookName 书本名称
     * @return SubBookCirculationInfo列表
     */
    public List<SubBookCirculationInfo> getSubBookCalculateData(String bookName) {
        List<SubBookCirculationInfo> findBooks = null;
        EntityWrapper<SubBookCirculationInfo> wrapper = new EntityWrapper<>();
        if (null != bookName && !bookName.trim().isEmpty()) {
            wrapper.and("bookName={0}", bookName);
        }
        try {
            findBooks = subbookcirculationinfoMapper.selectList(wrapper);
            if (null != findBooks && findBooks.size() > 0) {
                return findBooks;
            }
        } catch (Exception e) {

        }
        return null;

    }

    /**
     * 过上上面的接口计算回归方程，再通过方程计算复本数，二级页面
     *
     * @author lw
     * @date：2018-9-7
     */

    @RequestMapping(value = "/getSubDuplicateNumBySequation", method = RequestMethod.POST)
    @ApiOperation(value = "计算回归方程，再通过方程计算复本数",notes = "二级页面,不返回任何数据")
    public void getSubDuplicateNumBySequation() {
        // 获取所有的图书回归方程
        List<SubBookSequation> sq = subbooksequationMapper.selectList(null);
        // 将自变量代入方程
        for (SubBookSequation s : sq) {
            String sequation = "";
            sequation = s.getSequation();
            if (s.getNewVal().split("&").length == 1) {
                sequation = sequation.replace("x", "*" + s.getNewVal().split("&")[0]);
            } else {
                for (int i = 0; i < s.getNewVal().split("&").length; i++) {
                    sequation = sequation.replace("x" + (i + 1), "*" + s.getNewVal().split("&")[i]);
                }
            }
            String rightSequation = sequation.split("=")[1];// 获取方程右边的多项式
            // duplicateNum为图书复本数，步骤为：常量+a1x1+a2x2+....
            double duplicateNum = Double
                    .parseDouble(rightSequation.split("\\+")[rightSequation.split("\\+").length - 1]);// java中用+号分割需加//
            for (int m = 0; m < rightSequation.split("\\+").length - 1; m++) {
                duplicateNum += (Double.parseDouble(rightSequation.split("\\+")[m].split("\\*")[0]))
                        * (Double.parseDouble(rightSequation.split("\\+")[m].split("\\*")[1]));
            }
            List<ShowSubBookForecast> typeBook = getSubBookByType(s.getName());// 获取每类图书的回归方程，将将计算出来的复本数set进去
            if (typeBook.size() != 0) {
                for (ShowSubBookForecast tb : typeBook) {
                    int a = Integer.parseInt(new DecimalFormat("0").format(duplicateNum));
                    if (a > Integer.parseInt(tb.getDuplicateNum())) {
                        tb.setRequireNum("+" + String.valueOf(a - Integer.parseInt(tb.getDuplicateNum())));
                    } else if (a < Integer.parseInt(tb.getDuplicateNum())) {
                        tb.setRequireNum("-" + String.valueOf(Integer.parseInt(tb.getDuplicateNum()) - a));
                    } else {
                        tb.setRequireNum("0");
                    }

                    tb.setForecastNum(String.valueOf(a));
                    tb.updateById();
                }
            }
        }
    }

    /**
     * 根据图书名查询图书预测信息，二级页面
     *
     * @param name 书本类型名称
     * @return ShowSubBookForecast列表
     */
    public List<ShowSubBookForecast> getSubBookByType(String name) {
        List<ShowSubBookForecast> getBookByTypeName = null;
        EntityWrapper<ShowSubBookForecast> wrapper = new EntityWrapper<>();
        if (null != name && !name.trim().isEmpty()) {
            wrapper.and("bookTypeName={0}", name);
        }
        try {
            getBookByTypeName = showSubbookforecastMapper.selectList(wrapper);
            if (null != getBookByTypeName && getBookByTypeName.size() > 0) {
                return getBookByTypeName;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 将通过聚类后的读者，通过关联算法查找懒惰读者借阅量低的原因，所用表有ReaderAprioriInput， ReaderAprioriRule，ReaderInfo，showLazyReader
     *
     * @author lw
     * @date：2018-9-7
     */
    @RequestMapping(value = "/findLazyReaderReason", method = RequestMethod.POST)
    @ApiOperation(value = "关联算法查找懒惰读者借阅量低的原因",notes = "用到的表ReaderAprioriInput， ReaderAprioriRule，ReaderInfo，showLazyReader，不返回任何数据")
    public void findLazyReaderReason() {
        // 获取所有的懒惰读者信息
        List<ReaderAprioriInput> reader = findAllReaderInput("2.0");
        List<List<String>> record = new ArrayList<List<String>>();
        if (reader != null && reader.size() > 0) {
            for (ReaderAprioriInput r : reader) {
                List<String> l = new ArrayList<String>();
                // 学院
                l.add(r.getCollegeName());
                // 所属楼栋
                l.add(r.getDormitory());
                // 专业
                l.add(r.getMajor());
                // 读者身份，一年级
                l.add(r.getReaderIdentity());
                // 性别
                l.add(r.getReaderSex());
                // 聚类结果
                l.add(r.getCluster());
                record.add(l);
            }
        }
        List<AprioriRuleModel> rs = AprioriCommon.getRules(0.6, 0.1, record, "[2.0]");
        for (AprioriRuleModel rule : rs) {
            System.out.println(rule.getRule1() + "==>" + rule.getRule2() + "--- " + "【支持度】：" + rule.getSupport()
                    + "  【置信度】：" + rule.getConfidence());
        }
        /*
         * 用关联算法跑出规则
         */
        if (rs.size() == 0) {
            System.out.println("没有找到规则");
        }
        // 存放读者关联规则
        ReaderAprioriRule api = null;
        // 用于存放单个的规则
        List<String> boss = new ArrayList<String>();
        List<String> letter = null;
        for (int i = rs.size() - 1; i >= 0; i--) {
            // 从强关联规则中寻找规则个数最多的关联规则
            api = new ReaderAprioriRule();
            AprioriRuleModel rule = rs.get(i);
            if (i == rs.size() - 1) {
                api.setSupport(rule.getSupport());
                api.setConfidence(rule.getConfidence());
                api.setRule1(rule.getRule1());
                String ruleStr = rule.getRule1().replace(" ", "");
                String[] ruleArr = ruleStr.substring(1, ruleStr.length() - 1).split(",");
                boss = Arrays.asList(ruleArr);
                api.setRule2(rule.getRule2());
                api.insert();
            } else {
                if (rule.getRule1().length() >= rs.get(rs.size() - 1).getRule1().length()) {
                    String ruleStr = rule.getRule1().replace(" ", "");
                    String[] ruleArr = ruleStr.substring(1, ruleStr.length() - 1).split(",");
                    letter = Arrays.asList(ruleArr);
                    if (!boss.containsAll(letter)) {
                        // 防止获取重复的关联规则
                        api.setSupport(rule.getSupport());
                        api.setConfidence(rule.getConfidence());
                        api.setRule1(rule.getRule1());
                        api.setRule2(rule.getRule2());
                        api.insert();
                    }
                } else {
                    break;
                }
            }
        }
        // 根据关联规则统计原因占比
        List<ReaderAprioriRule> ra = readeraprioriruleMapper.selectList(null);
        // 用set集合，出掉重复项
        Set<String> arrReason = new HashSet<String>();
        for (ReaderAprioriRule r : ra) {
            // 去掉[]
            String reason = r.getRule1().toString().substring(1, r.getRule1().toString().length() - 1);
            for (int i = 0; i < reason.split(",").length; i++) {
                arrReason.add(reason.split(",")[i]);
            }
        }
        // 获取所有读者信息
        List<ReaderInfo> ri = readerinfoMapper.selectList(null);
        ShowLazyReader showLazyReader = null;
        String[] year = {"2013", "2014", "2015", "2016", "2017"};

        for (int j = 0; j < year.length; j++) {
            // 按年获取读者信息
            List<ReaderInfo> rys = findAllReaderByYear(year[j]);
            if (rys != null) {
                for (String reason : arrReason) {
                    String reasonStr = "";
                    int count = 0;
                    for (ReaderInfo ry : rys) {
                        // 将每个读者的信息连成字符串
                        reasonStr = ry.getDormitory() + ry.getMajor() + ry.getCollegeName() + ry.getReaderSex()
                                + ry.getReaderIdentity();
                        if (reasonStr.contains(reason.trim())) {
                            // 若含有符合规则的项，将+1
                            count++;
                        }
                    }
                    showLazyReader = new ShowLazyReader();
                    // 计算每类原因的读者占比
                    float rate = (float) count / ri.size();
                    showLazyReader.setRate(Float.toString(rate));
                    // 懒惰原因
                    showLazyReader.setReason(reason);
                    // 学年
                    showLazyReader.setYear(year[j]);
                    showLazyReader.insert();
                }
            }
        }
    }

    /**
     * 根据聚类类型查询读者信息
     *
     * @param cluster 读者类型 0.0 1.0 2.0
     * @return ReaderAprioriInput列表
     */
    public List<ReaderAprioriInput> findAllReaderInput(String cluster) {
        List<ReaderAprioriInput> findAllReaderInput = null;
        EntityWrapper<ReaderAprioriInput> wrapper = new EntityWrapper<>();
        if (null != cluster && !cluster.trim().isEmpty()) {
            wrapper.and("cluster={0}", cluster);
        }
        try {
            findAllReaderInput = readeraprioriinputMapper.selectList(wrapper);
            if (null != findAllReaderInput && findAllReaderInput.size() > 0) {
                return findAllReaderInput;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 根据年份查询读者信息
     *
     * @param year 年份
     * @return 读者信息列表
     */
    public List<ReaderInfo> findAllReaderByYear(String year) {
        List<ReaderInfo> findAllReaderByYear = null;
        EntityWrapper<ReaderInfo> wrapper = new EntityWrapper<>();
        if (null != year && !year.trim().isEmpty()) {
            wrapper.and("schoolYear={0}", year);
        }
        try {
            findAllReaderByYear = readerinfoMapper.selectList(wrapper);
            if (null != findAllReaderByYear && findAllReaderByYear.size() > 0) {
                return findAllReaderByYear;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 通过关联算法查找冷门图书借阅量低的原因,所用表有：BookAprioriInput,BookAprioriRule, ShowUpsetBook, BookAprioriForecast
     *
     * @author lw
     * @date：208-9-7
     */
    @RequestMapping(value = "/findUpsetBookReason", method = RequestMethod.POST)
    @ApiOperation(value = "关联算法查找冷门图书借阅量低的原因",notes = "不返回任何数据，用到的表：BookAprioriInput,BookAprioriRule, ShowUpsetBook, BookAprioriForecast")
    public void findUpsetBookReason() {
        // 获取所有的冷门图书信息,0.0代表冷门
        List<BookAprioriInput> book = findAllBookInput("0.0");
        // 将所有冷门图书的信息放进record数组
        List<List<String>> record = new ArrayList<List<String>>();
        if (book != null && book.size() > 0) {
            for (BookAprioriInput b : book) {
                List<String> l = new ArrayList<String>();
                l.add(b.getDamage());
                l.add(b.getPages());
                l.add(b.getPosition());
                l.add(b.getVersion());
                l.add(b.getCluster());
                record.add(l);
            }
        }
        // 调用关联规则算法，获取关联规则
        List<AprioriRuleModel> rs = AprioriCommon.getRules(0.1, 0.1, record, "[0.0]");
        for (AprioriRuleModel rule : rs) {
            System.out.println(rule.getRule1() + "==>" + rule.getRule2() + "--- " + "【支持度】：" + rule.getSupport()
                    + "  【置信度】：" + rule.getConfidence());
        }
        /*
         * 用关联算法跑出规则
         */
        if (rs.size() == 0) {
            System.out.println("没有找到规则");
        }
        // 用于存放图书的关联规则
        BookAprioriRule api = null;
        List<String> boss = new ArrayList<String>();
        List<String> letter = null;
        //// 从强关联规则中寻找规则个数最多的关联规则
        for (int i = rs.size() - 1; i >= 0; i--) {
            api = new BookAprioriRule();
            AprioriRuleModel rule = rs.get(i);
            if (i == rs.size() - 1) {
                // 支持度
                api.setSupport(rule.getSupport());
                // 置信度
                api.setConfidence(rule.getConfidence());
                // 左边关联规则
                api.setRule1(rule.getRule1());
                String ruleStr = rule.getRule1().replace(" ", "");
                String[] ruleArr = ruleStr.substring(1, ruleStr.length() - 1).split(",");
                boss = Arrays.asList(ruleArr);
                // 右边关联规则
                api.setRule2(rule.getRule2());
                api.insert();
            } else {
                if (rule.getRule1().length() >= rs.get(rs.size() - 1).getRule1().length()) {
                    String ruleStr = rule.getRule1().replace(" ", "");
                    String[] ruleArr = ruleStr.substring(1, ruleStr.length() - 1).split(",");
                    letter = Arrays.asList(ruleArr);
                    if (!boss.containsAll(letter)) {
                        api.setSupport(rule.getSupport());
                        api.setConfidence(rule.getConfidence());
                        api.setRule1(rule.getRule1());
                        api.setRule2(rule.getRule2());
                        api.insert();
                    }
                } else {
                    break;
                }
            }
        }

        // 根据关联规则统计原因占比
        List<BookAprioriRule> ba = bookaprioriruleMapper.selectList(null);
        // 存放关联原因
        Set<String> arrReason = new HashSet<String>();
        for (BookAprioriRule b : ba) {
            String reason = b.getRule1().toString().substring(1, b.getRule1().toString().length() - 1);
            for (int i = 0; i < reason.split(",").length; i++) {
                arrReason.add(reason.split(",")[i]);
            }
        }
        // BookAprioriInput即是训练数据又是预测数据
        ShowUpsetBook showUpsetBook = null;
        String[] year = {"2013", "2014", "2015", "2016", "2017"};
        String[] type = {"A&马、列、毛", "B&哲学宗教", "C&社科总论", "D&政治法律", "E&军事", "F&经济", "G&文教体育", "H&语言文字", "I&文学", "J&艺术",
                "K&历史地理", "N&自然科学总论", "O&数理化学", "P&天文地球", "Q&生物科学", "R&医药卫生", "S&农业科学", "T&工业技术", "U&航空、航天", "V&交通运输",
                "X&环境科学", "Z&综合类"};
        for (int j = 0; j < year.length; j++) {
            for (int l = 0; l < type.length; l++) {
                // 按年份、图书类型查询图书
                List<BookAprioriForecast> bas = findAllBookrByYear(year[j], type[l].split("&")[0]);
                if (bas != null) {
                    for (String reason : arrReason) {
                        String reasonStr = "";
                        int count = 0;
                        for (BookAprioriForecast bai : bas) {
                            reasonStr = bai.getDamage() + bai.getPages() + bai.getPosition() + bai.getVersion();// 将每本图书的信息拼串，若包含关联原因则计数
                            if (reasonStr.contains(reason.trim())) {
                                count++;
                            }
                        }
                        showUpsetBook = new ShowUpsetBook();
                        // 关联原因数量
                        showUpsetBook.setBookReasonNum(Integer.toString(count));
                        // 关联原因
                        showUpsetBook.setReason(reason);
                        // 年份
                        showUpsetBook.setYear(year[j]);
                        // 图书类
                        showUpsetBook.setType(type[l].split("&")[0]);
                        // 类名
                        showUpsetBook.setTypeName(type[l].split("&")[1]);
                        showUpsetBook.insert();
                    }
                }
            }
        }
    }

    /**
     * 按照聚类查找图书关联规则
     *
     * @param cluster 类型
     * @return BookAprioriInput列表
     */
    public List<BookAprioriInput> findAllBookInput(String cluster) {
        List<BookAprioriInput> findAllBookInput = null;
        EntityWrapper<BookAprioriInput> wrapper = new EntityWrapper<>();
        if (null != cluster && !cluster.trim().isEmpty()) {
            wrapper.and("cluster={0}", cluster);
        }
        try {
            findAllBookInput = bookaprioriinputMapper.selectList(wrapper);
            if (null != findAllBookInput && findAllBookInput.size() > 0) {
                return findAllBookInput;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 根据年份、类型查询图书
     *
     * @param year 年份
     * @param type 类型
     * @return BookAprioriForecast列表
     */

    public List<BookAprioriForecast> findAllBookrByYear(String year, String type) {
        List<BookAprioriForecast> findAllBookrByYear = null;
        EntityWrapper<BookAprioriForecast> wrapper = new EntityWrapper<>();
        if (null != year && !year.trim().isEmpty()) {
            wrapper.and("year={0}", year);
        }
        if (null != type && !type.trim().isEmpty()) {
            wrapper.and("type={0}", type);
        }
        try {
            findAllBookrByYear = bookaprioriforecastMapper.selectList(wrapper);
            if (null != findAllBookrByYear && findAllBookrByYear.size() > 0) {
                return findAllBookrByYear;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 根据学生在借书籍的记录，获取图书借阅关系图
     *
     * @author lw
     * @date：2018-9-7
     */

    @RequestMapping(value = "/getBookBorrowRelate", method = RequestMethod.POST)
    @ApiOperation(value = "图书借阅关系图",notes = "不返回任何数据")
    public void getBookBorrowRelate() {
        // 获取所有学生的在借书记录
        List<StuBorrowRecord> borrowRecords = stuborrowrecordMapper.selectList(null);
        String[] bookType = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "X", "Z"};
        String typePair = "";
        ShowBookBorrowRelate bookBorrowRelate = null;
        // 用两个for循环获取所有书类对，两两配对，再获取类对在借书记录中同时出现的概率
        for (int i = 0; i < bookType.length; i++) {
            for (int j = (i + 1); j < bookType.length; j++) {
                typePair = bookType[i] + bookType[j];
                int count = 0;
                for (StuBorrowRecord record : borrowRecords) {
                    if (record.getBorrowRecord().contains(bookType[i])
                            && record.getBorrowRecord().contains(bookType[j])) {
                        count++;
                    }
                }
                bookBorrowRelate = new ShowBookBorrowRelate();
                bookBorrowRelate.setTypePair(typePair);
                if (count == 0) {
                    bookBorrowRelate.setRate("0%");
                } else {
                    // 保留两位有效数字，组成百分比
                    DecimalFormat df = new DecimalFormat("#.00");
                    float rate = (float) count / borrowRecords.size();
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    bookBorrowRelate.setRate(df.format(rate * 100) + "%");
                }
                bookBorrowRelate.insert();
            }
        }
    }
}
