package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.BookInfo;
import com.mit.campus.rest.modular.book.model.ReaderBorrowRecord;
import com.mit.campus.rest.modular.book.model.ReaderInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 读者借阅记录service
 * </p>
 *
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:57
 */
public interface IReaderBorrowRecordService {
    /**
     * getBorrowRecordByReaderID
     * 获取读者借阅记录
     *
     * @param readerID 读者的id
     * @param year     年份
     * @return map<读者借阅记录   ,   书籍信息>
     */
    Map<ReaderBorrowRecord, BookInfo> getBorrowRecordByReaderID(String readerID, String year);


    /**
     * getBorrowRecordByBookID
     * 获取图书被借阅记录
     *
     * @param bookID 书籍的编号
     * @param year   年份
     * @return map<读者借阅记录   ,   读者信息>
     */
    Map<ReaderBorrowRecord, ReaderInfo> getBorrowRecordByBookID(String bookID, String year);


    /**
     * getBorrowRecordDate
     * 按书或读者查询借阅记录的时间
     *
     * @param readerID 读者编号
     * @param bookID   书籍编号
     * @param year     年份
     * @return 借阅记录列表
     */
    List<ReaderBorrowRecord> getBorrowRecordDate(String readerID, String bookID, String year);


}
