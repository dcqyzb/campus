package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.network.dao.ShowNetbehaviordeviceMapper;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorDevice;
import com.mit.campus.rest.modular.network.service.IShowNetbehaviordeviceService;
import org.springframework.stereotype.Service;

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
public class ShowNetbehaviordeviceServiceImpl extends ServiceImpl<ShowNetbehaviordeviceMapper, ShowNetBehaviorDevice> implements IShowNetbehaviordeviceService {
    /**
     * getNetDeviceInfo
     * 获取无线网络设备的上网统计数据
     *
     * @param month 年份（如2017-01）
     * @return ShowNetBehaviorDevice列表
     * @author lw
     * @date：2018-9-7
     */
    @Override
    public List<ShowNetBehaviorDevice> getNetDeviceInfo(String month) {
        EntityWrapper<ShowNetBehaviorDevice> wrapper = new EntityWrapper<>();
        wrapper.and("countMonth like {0}", month).orderBy("netUsage", false);
        List<ShowNetBehaviorDevice> list = selectList(wrapper);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }
}
