package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.BookinfoMapper;
import com.mit.campus.rest.modular.book.dao.ReaderborrowrecordMapper;
import com.mit.campus.rest.modular.book.dao.ReaderinfoMapper;
import com.mit.campus.rest.modular.book.model.BookInfo;
import com.mit.campus.rest.modular.book.model.ReaderBorrowRecord;
import com.mit.campus.rest.modular.book.model.ReaderInfo;
import com.mit.campus.rest.modular.book.service.IBookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 对书籍操作的实现类
 * </p>
 *
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 09:01
 */
@Service
public class BookInfoServiceImpl extends ServiceImpl<BookinfoMapper, BookInfo> implements IBookInfoService {
    @Autowired
    ReaderborrowrecordMapper readerborrowrecordMapper;
    @Autowired
    ReaderinfoMapper readerinfoMapper;

    /**
     * findBookByCondition
     * 按条件查询图书信息
     * pageSize<0查询全部
     * @param bookName  书名
     * @param typeName  书类名
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @param cluster   借阅热度，热门2，一般1，冷门0
     * @param pageNum   当前页
     * @param pageSize  一页显示几个
     * @return 书籍对象的page对象
     */
    @Override
    public Page<BookInfo> findBookByCondition(String bookName, String typeName,
                                              String startYear, String endYear, String cluster,
                                              int pageNum, int pageSize) {
        List<BookInfo> list = null;
        Page<BookInfo> page = new Page(pageNum, pageSize);
        EntityWrapper<BookInfo> wrapper = new EntityWrapper<>();
        //读者姓名模糊查询
        if (bookName != null && bookName.length() > 0) {
            wrapper.like("bookName", bookName);
        }
        //书类条件
        if (typeName != null && typeName.length() > 0) {
            wrapper.and("typeName={0}", typeName);
        }
        //年份区间，设置默认值
        if (startYear == null || startYear.length() == 0) {
            startYear = " ";
        }
        //结束时间不可为空，所以设置为今年
        if (endYear == null || endYear.length() == 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            endYear = format.format(new Date());
        }
        wrapper.and().between("year", startYear, endYear);
        //读者聚类
        if (cluster != null && cluster.length() > 0) {
            wrapper.and("cluster={0}", cluster);
        }
        wrapper.orderBy("year,borrowTimes", false).orderBy("type");
        try {
            if (pageSize > 0) {
                return selectPage(page, wrapper);
            } else {
//                查询所有
                list = selectList(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (list != null && list.size() > 0) {
            page.setRecords(list);
        }
        return page;
    }

    /**
     * getBorrowType
     * 获取该学生某年借书的类型占比
     *
     * @param readerID 读者学号
     * @param year     年份
     * @return 书籍列表
     */
    @Override
    public List<BookInfo> getBorrowType(String readerID, String year) {
        List<BookInfo> all_books = new ArrayList<BookInfo>();
        List<ReaderBorrowRecord> records = null;
        List<BookInfo> books = null;
        List<ReaderInfo> reader = null;
        try {
            EntityWrapper<ReaderBorrowRecord> wrapper = new EntityWrapper<>();
            wrapper.where("readerID={0}", readerID).and("date like {0}", year+"%");
            records = readerborrowrecordMapper.selectList(wrapper);

            EntityWrapper<ReaderInfo> wrapperReaderInfo = new EntityWrapper<>();
            wrapper.where("readerID={0}", readerID).and("schoolYear={0}", year);
            reader = readerinfoMapper.selectList(wrapperReaderInfo);

            if (reader != null && reader.size() > 0) {
                if (records != null && records.size() > 0) {
                    for (int i = 0, len = reader.get(0).getBookSum(); i < len; i++) {
                        if (i < records.size()) {
                            ReaderBorrowRecord record = records.get(i);
                            all_books.add(selectById(record.getBookID()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return all_books;
    }

    /**
     * getBookName
     * 根据id获取书籍的名字
     * @param id 书籍编号
     * @return 书籍的名字
     */
    @Override
    public String getBookName(String id) {
        try {
            return selectById(id).getBookName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
