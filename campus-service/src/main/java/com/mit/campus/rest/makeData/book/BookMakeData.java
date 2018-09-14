package com.mit.campus.rest.makeData.book;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mit.campus.rest.modular.book.dao.ShowSubbookforecastMapper;
import com.mit.campus.rest.modular.book.model.ShowSubBookForecast;
import com.mit.campus.rest.modular.book.model.SubBookInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-11 09:04
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class BookMakeData {
    /**
     * 造网络行为和成绩的关系数据
     */
    @Test
    public void makeDateSubBookForecast() throws FileNotFoundException {
        File file = new File("C:\\Users\\Adstrator\\Desktop\\show_subbookforecast.txt");
        String jsonS = "";
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file), "gbk");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                jsonS += line;
            }
            List<ShowSubBookForecast> list = JSON.parseArray(jsonS, ShowSubBookForecast.class);
            for (ShowSubBookForecast n : list) {
                n.insertAllColumn();
                /*log.error(n.toString());*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Autowired
    ShowSubbookforecastMapper showSubbookforecastMapper;
    @Test
    public void makeDataSubBookInfo() {
        List<ShowSubBookForecast> list = showSubbookforecastMapper.selectList(null);
        for (ShowSubBookForecast s:list){
            sendPost("http://117.40.142.28:8083/campusbd/api/bookCalculate/getSubBookInfo", "bookID="+s.getUuid());
        }
    }

    /**
     * 发送post请求去访问接口获取数据
     * @param url 访问地址
     * @param param 参数 k=v
     * @return 是否操作成功
     */
    public Boolean sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置请求方式为POST方法
            conn.setRequestMethod("POST");

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //此处可以设置请求参数，若接口的header中有串码可以在此设置。
            /** conn.setRequestProperty("header", "headerMsg");*/
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数，发送的数据在此处发送。
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //查询完成
            JSONObject json = JSONObject.parseObject(result);
            JSONArray array = json.getJSONArray("object");
            List<SubBookInfo> subBookInfos = array.toJavaList(SubBookInfo.class);
            if (subBookInfos!=null){
                for (SubBookInfo sub:subBookInfos){
                    sub.insertAllColumn();
                }
            }
            System.out.println(subBookInfos.toString());
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //result为请求结束后返回的response，可以让接口发布方设置数据是否发送成功的标识，具体怎么返回还需要你们自己协商。
        if (result.contains("SUCCESS")) {
            return true;
        } else {
            return false;
        }
    }

}
