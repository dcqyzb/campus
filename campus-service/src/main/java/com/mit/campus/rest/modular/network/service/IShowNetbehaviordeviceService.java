package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorDevice;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface IShowNetbehaviordeviceService extends IService<ShowNetBehaviorDevice> {


    /**
     * getNetDeviceInfo
     * 获取无线网络设备的上网统计数据
     *
     * @param month 年份（如2017-01）
     * @return ShowNetBehaviorDevice列表
     * @author lw
     * @date：2018-9-7
     */
    List<ShowNetBehaviorDevice> getNetDeviceInfo(String month);
}
