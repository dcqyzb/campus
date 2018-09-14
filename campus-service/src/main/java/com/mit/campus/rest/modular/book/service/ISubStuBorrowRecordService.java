package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.SubStuBorrowRecord;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:59
 */
public interface ISubStuBorrowRecordService {
    /**
     * getSubBookBorrowRelate
     * 二级页面-查看具体书的借阅关系
     * @param bookID 书籍编号
     * @return 书籍借阅列表
     */
    List<SubStuBorrowRecord> getSubBookBorrowRelate(String bookID);
    /**
     * getSubStuBorrowRecordCount
     * 二级页面-获取借阅关系总个数
     * @return 借阅关系总个数
     */
    int getSubStuBorrowRecordCount();

}
