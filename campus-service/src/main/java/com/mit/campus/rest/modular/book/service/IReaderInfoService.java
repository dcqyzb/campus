package com.mit.campus.rest.modular.book.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.book.model.ReaderInfo;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:50
 */
public interface IReaderInfoService {
    /**
     * findReaderByCondition
     * 按条件查询读者信息
     * pageSize<0 查询全部
     * @param readerName 读者姓名
     * @param college 学院
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @param cluster 读者聚类，懒惰0.0，一般2.0,活跃1.0
     * @param pageNum 当前页
     * @param pageSize 每页显示几个
     * @return 读者信息的page对象
     */
    Page<ReaderInfo> findReaderByCondition(String readerName, String college, String startYear, String endYear, String cluster, int pageNum, int pageSize);

    /**
     * getBookCollegeRate
     * 获取某本书某年，学院借该书的占比
     *
     * @param bookID 书id
     * @param year   年份
     * @return 学院借该书的占比
     */
    List<ReaderInfo> getBookCollegeRate(String bookID, String year) ;
}
