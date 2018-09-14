package com.mit.campus.rest.modular.book.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.book.model.BookInfo;

import java.util.List;

/**
 * <p>
 * 书籍的service层接口
 * </p>
 *
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:54
 */
public interface IBookInfoService {
    /**
     * findBookByCondition
     * 按条件查询图书信息
     * pageSize<0 查询全部
     *
     * @param bookName  书名
     * @param typeName  书类名
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @param cluster   借阅热度，热门2，一般1，冷门0
     * @param pageNum   当前页
     * @param pageSize  每页显示几个
     * @return 书籍信息的page对象
     */
    Page<BookInfo> findBookByCondition(String bookName, String typeName, String startYear, String endYear, String cluster,
                                       int pageNum, int pageSize);


    /**
     * getBorrowType
     * 获取该学生某年借书的类型占比
     *
     * @param readerID 读者学号
     * @param year     年份
     * @return 书籍列表
     */
    List<BookInfo> getBorrowType(String readerID, String year);

    /**
     * getBookName
     * 根据id获取书籍的名字
     *
     * @param id 书籍id
     * @return 书的名字
     */
    String getBookName(String id);

}
