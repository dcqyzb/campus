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
import com.mit.campus.rest.modular.book.service.IReaderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 读者信息实现
 * </p>
 *
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 09:46
 */
@Service
public class ReaderInfoServiceImpl extends ServiceImpl<ReaderinfoMapper, ReaderInfo> implements IReaderInfoService {
    @Autowired
    ReaderborrowrecordMapper readerborrowrecordMapper;
    @Autowired
    BookinfoMapper bookinfoMapper;

    /**
     * findReaderByCondition
     * 根据条件查找读者
     * pageSize<0 查询全部
     *
     * @param readerName 读者姓名
     * @param college    学院
     * @param endYear    结束年份
     * @param startYear  开始年份
     * @param cluster    读者聚类，懒惰，一般
     * @param pageNum    每页显示几个
     * @param pageSize   共有几页
     * @return 读者信息列表
     */
    @Override
    public Page<ReaderInfo> findReaderByCondition(String readerName, String college,
                                                  String startYear, String endYear, String cluster, int pageNum, int pageSize) {
        List<ReaderInfo> list = null;
        Page<ReaderInfo> page = new Page(pageNum, pageSize);
        EntityWrapper<ReaderInfo> wrapper = new EntityWrapper<>();
        //读者姓名模糊查询
        if (readerName != null && readerName.length() > 0) {
            wrapper.like("readerName", readerName);
        }
        //学院条件
        if (college != null && college.length() > 0) {
            wrapper.and("collegeName={0}", college);
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
        wrapper.and().between("schoolYear", startYear, endYear);
        //读者聚类
        if (cluster != null && cluster.length() > 0) {
            wrapper.and("cluster={0}", cluster);
        }
        wrapper.orderBy("schoolYear,bookSum,collegeName", false);

        try {
            if (pageSize > 0) {
//				分页查询
                return this.selectPage(page, wrapper);
            } else {
                //当pageSize 小于等于0 时，导出数据，查询全部
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
     * getBookCollegeRate
     * 获取某本书某年，学院借该书的占比
     *
     * @param bookID 书id
     * @param year   年份
     * @return 学院借该书的占比
     */
    @Override
    public List<ReaderInfo> getBookCollegeRate(String bookID, String year) {
        List<ReaderInfo> all_readers = new ArrayList<>();
        List<ReaderBorrowRecord> records = null;
        List<BookInfo> book = null;
        try {
            EntityWrapper<ReaderBorrowRecord> wrapper = new EntityWrapper<>();
            wrapper.where("bookID={0}", bookID).and("date like {0}", year+"%");
            records = readerborrowrecordMapper.selectList(wrapper);

            EntityWrapper<BookInfo> wrapperBookInfo = new EntityWrapper<>();
            wrapper.where("uuid={0}", bookID).and("year={0}", year);
            book = bookinfoMapper.selectList(wrapperBookInfo);

            if (book != null && book.size() > 0) {
                if (records != null && records.size() > 0) {
                    //该书该年所有借阅次数
                    int borrowTime = Integer.parseInt(book.get(0).getBorrowTimes());
                    for (int i = 0; i < borrowTime; i++) {
                        if (i < records.size()) {
                            ReaderBorrowRecord record = records.get(i);
                            EntityWrapper<ReaderInfo> wrapper1 = new EntityWrapper<>();
                            wrapper1.where("readerID={0}", record.getReaderID());
                            all_readers.addAll(selectList(wrapper1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return all_readers;
    }
}
