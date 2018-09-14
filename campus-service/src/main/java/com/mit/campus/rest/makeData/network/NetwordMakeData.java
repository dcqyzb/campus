package com.mit.campus.rest.makeData.network;

import com.alibaba.fastjson.JSON;
import com.mit.campus.rest.makeData.ConstansForData;
import com.mit.campus.rest.modular.network.model.*;
import com.mit.campus.rest.modular.network.service.INetbehaviordeviceService;
import com.mit.campus.rest.modular.network.service.IShowNetbehaviordeviceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;
import java.util.Random;


/**
 * @author lw
 * @company mitesofor
 * @creatie 2018-09-10 09:44
 * 制造数据模式的数据
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class NetwordMakeData {
    @Autowired
    IShowNetbehaviordeviceService showNetbehaviordeviceService;
    @Autowired
    INetbehaviordeviceService netbehaviordeviceService;

    /**
     * 造员工数据
     * Employee
     */
    public Employee makeDataEmployee() {
        Employee employee = new Employee();

        String name = getUpsetUsername();
        String[] departments = ConstansForData.departmentarrys;
        String dep = departments[(int) (Math.random() * departments.length)];
        String[] positionarrys = ConstansForData.positionarrys;
        String pos = positionarrys[(int) (Math.random() * positionarrys.length)];
        String phone = getNumber();
//        生成邮箱
        String[] emLast = {"@56.com", "@qq.com", "@x.cn", "@qq.com"};
        int x = (int) (Math.random() * 10);
        String em = phone;
        if (x > 0 && x <= 2.5) {
            em = em + emLast[0];
        } else if (x > 2.5 && x <= 5) {
            em = em + emLast[1];
        } else if (x > 5 && x <= 7.5) {
            em = em + emLast[2];
        } else {
            em = em + emLast[3];
        }
        employee.setDepartment(dep);
        employee.setEmail(em);
        employee.setEmployeename(name);
        employee.setPhonenumber(phone);
        employee.setPosition(pos);
        return employee;
    }

    /**
     * 造网络设备与负责员工数据
     */
    @Test
    public void makeDataNetbehaviordevice() {
//        查询全部的源数据
        List<ShowNetBehaviorDevice> showNetBehaviorDeviceList = showNetbehaviordeviceService.selectList(null);
        if (showNetBehaviorDeviceList != null) {
            for (ShowNetBehaviorDevice s : showNetBehaviorDeviceList) {
                NetBehavoirDevice n = new NetBehavoirDevice();
                n.setUuid(s.getUuid());
                n.setBroadband(s.getBroadband());
                n.setClientCount(s.getClientCount());
                n.setCountMonth(s.getCountMonth());
                n.setDeviceNumber(s.getDeviceNumber());
                n.setFlowConsumped(s.getFlowConsumped());
                n.setInstallDate(s.getInstallDate());
                String[] splits = ConstansForData.place.split(",");
                n.setInstallLocation(splits[(int) (Math.random() * splits.length)]);
                n.setLimitedTime(s.getLimitedTime());
                n.setNetUsage(s.getNetUsage());
                Employee employee = makeDataEmployee();
                if (employee.selectOne("employeename={0}", employee.getEmployeename()) == null) {
                    employee.insert();
                    n.setPrincipalName(employee.getEmployeename());
                    int x = (int) (Math.random() * 10);
                    if (x > 0 && x < (10 / 3)) {
                        n.setIncrease(0);
                    } else if (x > (10 / 3) && x < (20 / 3)) {
                        n.setIncrease(-1);
                    } else {
                        n.setIncrease(1);
                    }
                    log.error(n.toString());
                    netbehaviordeviceService.insertAllColumn(n);
                } else {
                    continue;
                }
            }
        }
    }

    /**
     * 制造名字
     *
     * @return 名字
     */
    public String getUpsetUsername() {
        // 对数据进行复制
        String[] xing = ConstansForData.xing.split(",");
        String[] name = ConstansForData.ming.split(",");
        int x_i = xing.length;
        int n_i = name.length;
        // 随机生成姓名
        String xi = xing[(int) (Math.random() * x_i)];
        // 三位的名字
        String na;
        if ((Math.random() * 10) > 5) {
            na = name[(int) (Math.random() * n_i)] + name[(int) (Math.random() * n_i)];
        } else {
            na = name[(int) (Math.random() * n_i)];
        }
        return xi + na;
    }

    /**
     * 制造电话号码
     *
     * @return 电话号码
     */
    public String getNumber() {
        String[] numbers = ConstansForData.phonenumpre.split(",");
        int a = (int) ((Math.random() * 100) % numbers.length);
        //定义电话号码以***开头
        String number = numbers[a];
        //定义random，产生随机数
        Random random = new Random();
        for (int j = 0; j < 8; j++) {
            //生成0~9 随机数
            number += random.nextInt(9);
        }
//      输出一个电话号码
        return number;
    }

    /**
     * 造网络行为和成绩的关系数据
     */
    @Test
    public void makeDateNetBehaviorAndGrade() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\json.txt");
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
            List<NetBehaviorAndGrade> list = JSON.parseArray(jsonS, NetBehaviorAndGrade.class);
            for (NetBehaviorAndGrade n : list) {
                n.insertAllColumn();
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

    /**
     * 造沉迷网络原因
     */
    @Test
    public void makeDataStuNetBehaviorWords() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\jsonnet.txt");
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
            List<StuNetBehaviorWords> list = JSON.parseArray(jsonS, StuNetBehaviorWords.class);
            for (StuNetBehaviorWords n : list) {
                n.insertAllColumn();
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

}
