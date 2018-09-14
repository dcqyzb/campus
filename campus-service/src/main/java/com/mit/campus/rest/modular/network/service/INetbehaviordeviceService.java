package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.NetBehavoirDevice;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface INetbehaviordeviceService extends IService<NetBehavoirDevice> {
	/**
	 * getPoorNumberData
	 * 二级页面-网络设备查询
	 * @author  lw
	 * @date：2018-9-4
	 * @param deviceNumber 设备编号
	 * @param installLocation 设备放置地点
	 * @param startDate 监测时间-开始时间
	 * @param endDate 监测时间-结束时间
	 * @param pageNum 当前页
	 * @param pageSize 每页记录数，<= 0时导出
	 * @return NetworkDevice的page对象
	 */
	Page getNetworkDeviceNum(String deviceNumber, String installLocation, String startDate, String endDate,
                                    int pageNum, int pageSize);

    /**
     * 查找最新统计一个月的网络设备使用率top10的
     *
     * @return NetBehavoirDevice列表
     */
    List<NetBehavoirDevice> findTopDevice();
}
