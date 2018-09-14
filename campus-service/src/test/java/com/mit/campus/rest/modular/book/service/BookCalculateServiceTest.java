package com.mit.campus.rest.modular.book.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.book.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author LW
 * @creatTime 2018-09-05 17:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookCalculateServiceTest {
    private static final Log LOGGER = LogFactory.getLog(BookCalculateServiceTest.class);

    @Autowired
    IReaderBorrowRecordService readerBorrowRecordService;
    @Autowired
    IReaderInfoService readerInfoService;
    @Autowired
    IShowBookBorrowRateService showBookBorrowRateService;
    @Autowired
    IShowBookBorrowRelateService showBookBorrowRelateService;
    @Autowired
    IShowBookForecastService showBookForecastService;
    @Autowired
    IShowBookRankService showBookRankService;
    @Autowired
    IShowBookTypeRateService showBookTypeRateService;
    @Autowired
    IShowLazyReaderService showLazyReaderService;
    @Autowired
    IShowReaderBorrowRateService showReaderBorrowRateService;
    @Autowired
    IShowReaderTypeRateService showReaderTypeRateService;
    @Autowired
    IShowSubBookForecastService showSubBookForecastService;
    @Autowired
    IShowUpsetBookService showUpsetBookService;
    @Autowired
    ISubBookInfoService subBookInfoService;
    @Autowired
    ISubStuBorrowRecordService subStuBorrowRecordService;
    @Autowired
    IBookInfoService bookInfoService;

    @Test
    public void findBookForecast() {
        List<ShowBookForecast> list = showBookForecastService.findBookForecast();
        LOGGER.error(list);
    }

    @Test
    public void findBookBorrowRate() {
        List<ShowBookBorrowRate> list = showBookBorrowRateService.findBookBorrowRate("2017");
        LOGGER.error(list);
    }

    @Test
    public void findBookTypeRate() {
        List<ShowBookTypeRate> list = showBookTypeRateService.findBookTypeRate("2017");
        LOGGER.error(list);
    }

    @Test
    public void findReaderBorrowRate() {
        List<ShowReaderBorrowRate> list = showReaderBorrowRateService.findReaderBorrowRate("2017");
        LOGGER.error(list);
    }

    @Test
    public void findReaderTypeRate() {
        List<ShowReaderTypeRate> list = showReaderTypeRateService.findReaderTypeRate("2016");
        LOGGER.error(list);

    }

    @Test
    public void findLazyReader() {
        List<ShowLazyReader> list = showLazyReaderService.findLazyReader("2017");
        LOGGER.error(list);
    }

    @Test
    public void findBookBorrowRelate() {
        List<ShowBookBorrowRelate> list = showBookBorrowRelateService.findBookBorrowRelate();
        LOGGER.error(list);
    }

    @Test
    public void findBookRank() {
        List<ShowBookRank> list = showBookRankService.findBookRank();
        LOGGER.error(list);
    }

    @Test
    public void findUpsetBook() {
        List<ShowUpsetBook> list = showUpsetBookService.findUpsetBook("2017");
        LOGGER.error(list);
    }

    @Test
    public void findReaderByCondition() {
        Page<ReaderInfo> page = readerInfoService.findReaderByCondition("郑", "信息学院", "", "", "", 1, 100);
        LOGGER.error(page.getRecords());
        LOGGER.error("总页数：" + page.getPages());
        LOGGER.error("总数：" + page.getTotal());
        LOGGER.error("当前页：" + page.getCurrent());
        LOGGER.error("每页显示几个：" + page.getSize());
    }

    @Test
    public void findBookByCondition() {
        Page<BookInfo> page = bookInfoService.findBookByCondition("", "", "", "", "", 1, 10);
//        var totalCount = page.total,
//		      totalPage = page.pages,
//			  curPage = page.current,
//			  pageSize = page.size;
        LOGGER.error(page.getRecords());
        LOGGER.error("总页数：" + page.getPages());
        LOGGER.error("总数：" + page.getTotal());
        LOGGER.error("当前页：" + page.getCurrent());
        LOGGER.error("每页显示几个：" + page.getSize());
    }

    @Test
    public void findBookForecastByCondition() {
        Page<ShowSubBookForecast> page = showSubBookForecastService.findBookForecastByCondition("", "", 1, 10);
        LOGGER.error(page.getRecords());
        LOGGER.error("总页数：" + page.getPages());
        LOGGER.error("总数：" + page.getTotal());
        LOGGER.error("当前页：" + page.getCurrent());
        LOGGER.error("每页显示几个：" + page.getSize());
    }

    @Test
    public void getBorrowType() {
        List<BookInfo> list = bookInfoService.getBorrowType("35223436", "2018");
        LOGGER.error("list:" + list.size());
    }

    @Test
    public void getBookCollegeRate() {
        List<ReaderInfo> list = readerInfoService.getBookCollegeRate("11561157", "2018");
        LOGGER.error("list:" + list);
    }

    @Test
    public void getBorrowRecordDate() {
        List<ReaderBorrowRecord> list = readerBorrowRecordService.getBorrowRecordDate("", "", "2017");
        LOGGER.error(list);
    }

    @Test
    public void getBorrowRecordByReaderID() {
        Map<ReaderBorrowRecord, BookInfo> map = readerBorrowRecordService.getBorrowRecordByReaderID("111111", "2017");
        LOGGER.error(map.toString());
    }

    @Test
    public void getBorrowRecordByBookID() {
        Map<ReaderBorrowRecord, ReaderInfo> map = readerBorrowRecordService.getBorrowRecordByBookID("", "2017");
        LOGGER.error(map.toString());
    }

    @Test
    public void getSubBookInfo() {
        List<SubBookInfo> list = subBookInfoService.getSubBookInfo("");
        LOGGER.error(list);
    }

    @Test
    public void getSubBookBorrowRelate() {
        List<SubStuBorrowRecord> list = subStuBorrowRecordService.getSubBookBorrowRelate("");
        LOGGER.error(list);
    }

    @Test
    public void getSubStuBorrowRecordCount() {
        int i = subStuBorrowRecordService.getSubStuBorrowRecordCount();
        LOGGER.error(i);
    }

    @Test
    public void getBookName() {
        String s = bookInfoService.getBookName("10009452");
        LOGGER.error(s);
    }
}
