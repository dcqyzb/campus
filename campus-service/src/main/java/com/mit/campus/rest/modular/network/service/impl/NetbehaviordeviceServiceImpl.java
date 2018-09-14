package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.network.dao.NetbehaviordeviceMapper;
import com.mit.campus.rest.modular.network.model.NetBehavoirDevice;
import com.mit.campus.rest.modular.network.service.INetbehaviordeviceService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@Service
public class NetbehaviordeviceServiceImpl extends ServiceImpl<NetbehaviordeviceMapper, NetBehavoirDevice> implements INetbehaviordeviceService {
    /**
     * getPoorNumberData
     * 二级页面-网络设备查询
     *
     * @param deviceNumber    设备编号
     * @param installLocation 设备放置地点
     * @param startDate       监测时间-开始时间
     * @param endDate         监测时间-结束时间
     * @param pageNum         当前页
     * @param pageSize        每页记录数，<= 0时导出
     * @return NetworkDevice的page对象
     * @author lw
     * @date：2018-9-4
     */
    @Override
    public Page getNetworkDeviceNum(String deviceNumber, String installLocation, String startDate, String endDate,
                                    int pageNum, int pageSize) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<NetBehavoirDevice> wrapper = new EntityWrapper<>();
        wrapper.orderBy("countMonth", false).orderBy("installDate",true);
        List<NetBehavoirDevice> list = null;
        Page<NetBehavoirDevice> page = new Page();
        boolean export = false;

        if (pageSize > 0) {
            //每页记录数
            page.setCurrent(pageNum);
            page.setSize(pageSize);
        } else {
            //当pageSize 小于等于0 时，导出
            export = true;
        }
        //时间查询条件
        if (startDate != null && startDate.length() == 7) {
            //默认从一个月第一天开始
            startDate = startDate + "-01";
        } else {
            startDate = " ";
        }
        if (endDate != null && endDate.length() == 7) {
            //默认从一个月第一天开始
            endDate = endDate + "-01";
        } else {
            //结束时间不可为空，所以设置为今天的日期
            endDate = format.format(new Date());
        }
        //按时间区间查询的条件语句
        wrapper.between("countMonth", startDate, endDate);
        //设备编号条件
        if (deviceNumber != null && deviceNumber.length() > 0) {
            wrapper.like("deviceNumber", deviceNumber);
        }
        //设备放置地点条件
        if (installLocation != null && installLocation.length() > 0) {
            wrapper.like("installLocation", installLocation);
        }
        if (export) {
            //导出数据，查询全部
            //要导出的数据
            list = selectList(wrapper);
        } else {
            //分页查询
            return selectPage(page, wrapper);
        }
        if (list != null && list.size() > 0) {
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 查找最新统计一个月的网络设备使用率top10的
     *
     * @return NetBehavoirDevice列表
     */
    @Override
    public List<NetBehavoirDevice> findTopDevice() {
//		String sql="select a.* from tb_netbehaviordevice a where a.countMonth = ("
//				+ "select  countMonth from tb_netbehaviordevice order by countMonth desc LIMIT 1)"
//				+ " ORDER BY netUsage DESC LIMIT 10";
        EntityWrapper<NetBehavoirDevice> wrapper = new EntityWrapper<>();
        wrapper.and("countMonth = (" +
                "select  countMonth from tb_netbehaviordevice order by countMonth desc LIMIT 1)")
                .orderBy("netUsage",false).last("LIMIT 10");
        List<NetBehavoirDevice> drvicelist = selectList(wrapper);

        return drvicelist;
    }
}
