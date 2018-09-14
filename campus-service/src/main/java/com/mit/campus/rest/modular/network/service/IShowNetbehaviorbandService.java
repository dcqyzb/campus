package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorBand;

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
public interface IShowNetbehaviorbandService extends IService<ShowNetBehaviorBand> {


    /**
     * getNetworkBand
     * 获取某年或某月的总带宽
     *
     * @param college   学院
     * @param countTime 统计时间
     * @return ShowNetBehaviorBand列表
     * @author lw
     * @date：2018-9-7
     */
    List<ShowNetBehaviorBand> getNetworkBand(String college, String countTime);

}
