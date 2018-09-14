package com.mit.campus.rest.modular.book.service;

import com.mit.campus.rest.modular.book.model.SubBookInfo;

import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 08:58
 */
public interface ISubBookInfoService {
    /**
     * getSubBookInfo
     * 二级页面-图书需求趋势的二级类别下具体书信息
     * @param bookID 书籍编号
     * @return 二级列表具体书籍信息列表
     */
    List<SubBookInfo> getSubBookInfo(String bookID);
}
