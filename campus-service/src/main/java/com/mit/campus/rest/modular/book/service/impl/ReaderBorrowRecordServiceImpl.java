package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.BookinfoMapper;
import com.mit.campus.rest.modular.book.dao.ReaderborrowrecordMapper;
import com.mit.campus.rest.modular.book.dao.ReaderinfoMapper;
import com.mit.campus.rest.modular.book.model.BookInfo;
import com.mit.campus.rest.modular.book.model.ReaderBorrowRecord;
import com.mit.campus.rest.modular.book.model.ReaderInfo;
import com.mit.campus.rest.modular.book.service.IReaderBorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     读者借阅记录实现类
 * </p>
 *
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 09:27
 */
@Service
public class ReaderBorrowRecordServiceImpl extends ServiceImpl<ReaderborrowrecordMapper, ReaderBorrowRecord> implements IReaderBorrowRecordService {
    @Autowired
    ReaderinfoMapper readerinfoMapper;
    @Autowired
    BookinfoMapper bookinfoMapper;

    /**
     * getBorrowRecordByReaderID
     * 获取读者借阅记录
     *
     * @param readerID 读者编号
     * @param year     年份
     * @return map<读者借阅记录 , 读者信息>
     */
    @Override
    public Map<ReaderBorrowRecord, BookInfo> getBorrowRecordByReaderID(String readerID, String year) {
        Map<ReaderBorrowRecord, BookInfo> map = new HashMap<ReaderBorrowRecord, BookInfo>();
        List<ReaderBorrowRecord> records = null;
        List<ReaderInfo> reader = null;

        EntityWrapper<ReaderBorrowRecord> wrapper = new EntityWrapper<>();
        wrapper.where("readerID={0}", readerID).and("date like {0}", year+"%")
                .orderBy("date", false);

        EntityWrapper<ReaderInfo> wrapperReaderInfo = new EntityWrapper<>();
        wrapperReaderInfo.where("readerID={0}", readerID).and("schoolYear={0}", year);

        try {
            //该读者该年所有借阅记录
            records = selectList(wrapper);
            //该读者信息
            reader = readerinfoMapper.selectList(wrapperReaderInfo);
            if (reader != null && reader.size() > 0) {
                if (records != null && records.size() > 0) {
                    for (int i = 0, len = reader.get(0).getBookSum(); i < len; i++) {
                        //防止溢出
                        if (i < records.size()) {
                            ReaderBorrowRecord record = records.get(i);
                            BookInfo bookInfo = bookinfoMapper.selectById(record.getBookID());
                            if (bookInfo != null) {
								/*key : 借阅记录
								  value : 图书详情				*/
                                map.put(record, bookInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;

    }

    /**
     * getBorrowRecordByBookID
     * 获取图书被借阅记录
     *
     * @param bookID 书籍编号
     * @param year   年份
     * @return map<读者借阅记录 , 读者信息>
     */
    @Override
    public Map<ReaderBorrowRecord, ReaderInfo> getBorrowRecordByBookID(String bookID, String year) {
        Map<ReaderBorrowRecord, ReaderInfo> map = new HashMap<ReaderBorrowRecord, ReaderInfo>();
        List<ReaderBorrowRecord> records = null;
        List<ReaderInfo> readers = null;
        List<BookInfo> book = null;

        EntityWrapper<ReaderBorrowRecord> wrapper = new EntityWrapper<>();
        wrapper.where("bookID={0}", bookID).and("date like {0}", year+"%").orderBy("date", false);

        EntityWrapper<BookInfo> wrapperBookInfo = new EntityWrapper<>();
        wrapperBookInfo.where("uuid={0}", bookID).and("year", year);
        try {
            //该图书该年所有借阅记录
            records = selectList(wrapper);
            //该图书信息
            book = bookinfoMapper.selectList(wrapperBookInfo);

            if (book != null && book.size() > 0) {
                if (records != null && records.size() > 0) {
                    //该书该年所有借阅次数
                    int borrowTime = Integer.parseInt(book.get(0).getBorrowTimes());
                    for (int i = 0; i < borrowTime; i++) {
                        //防止溢出
                        if (i < records.size()) {
                            ReaderBorrowRecord record = records.get(i);
                            ReaderInfo readerInfo = readerinfoMapper.selectById(record.getReaderID());
                            if (readerInfo != null) {
								/*key : 借阅记录
								  value : 图书详情				*/
                                map.put(record, readerInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * getBorrowRecordDate
     * 按书或读者查询借阅记录的时间
     *
     * @param readerID 读者编号
     * @param bookID   书籍编号
     * @param year     年份
     * @return 借阅记录列表
     */
    @Override
    public List<ReaderBorrowRecord> getBorrowRecordDate(String readerID, String bookID, String year) {
        List<ReaderBorrowRecord> records = null;
        EntityWrapper<ReaderBorrowRecord> wrapper = new EntityWrapper<>();
        wrapper.and("date like {0}", year+"%");
        if (readerID != null && readerID.length() > 0) {
            wrapper.and("readerID={0}", readerID);
        }
        if (bookID != null && bookID.length() > 0) {
            wrapper.and("bookID={0}", bookID);
        }
        try {
            //该读者该年所有借阅记录
            wrapper.groupBy("date").orderBy("date", false);
            records = selectList(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return records;
    }
}
