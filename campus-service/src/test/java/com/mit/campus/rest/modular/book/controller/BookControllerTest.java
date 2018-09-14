package com.mit.campus.rest.modular.book.controller;

import com.mit.campus.rest.modular.book.service.BookCalculateServiceTest;
import com.mit.campus.rest.util.QueryCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author LW
 * @creatTime 2018-09-06 15:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTest {
    private static final Log LOGGER = LogFactory.getLog(BookCalculateServiceTest.class);

    /**
     * 模拟MVC对象
     **/
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext web;
    @Before
    public void setup() {
        // 在测试开始前初始化工作
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.web).build();
    }
    @Test
    public void getBookRank() throws Exception  {
        Map<String, Object> map = new HashMap<>();
        map.put("address", "合肥");
        // 模拟向testRest发送get请求
        // 预期返回值的媒体类型text/plain;charset=UTF-8
        // 返回执行请求的结果
        MvcResult result = mockMvc.perform(post("/bookCalculate/getBookRank")
//                .content(JSONObject.toJSONString(map))
                )
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        LOGGER.error(result.getResponse().getContentAsString());
//        System.out.println();
    }

    @Test
    public void getBookBorrowRate() {
    }

    @Test
    public void getReaderBorrowRate() {
    }

    @Test
    public void getBookForecast() {
    }

    @Test
    public void getLazyReader() {
    }

    @Test
    public void getBookBorrowRelate() {
    }

    @Test
    public void getUpsetBook() {
    }

    @Test
    public void findReaderByCondition() {
    }

    @Test
    public void findBookByCondition() {
    }

    @Test
    public void findBookForecastByCondition() {
    }

    @Test
    public void getBorrowType() {
    }

    @Test
    public void getBorrowRecordByReaderID() {
    }

    @Test
    public void getBookCollegeRate() {
    }

    @Test
    public void getBorrowRecordByBookID() {
    }

    @Test
    public void getSubBookInfo() {
    }

    @Test
    public void getSubBookBorrowRelate() {
    }
    @Test
    public void test() {
        QueryCondition qc = new QueryCondition("schoolYear", QueryCondition.EQ, "2017");
        System.out.println(qc.toString());
    }
}
