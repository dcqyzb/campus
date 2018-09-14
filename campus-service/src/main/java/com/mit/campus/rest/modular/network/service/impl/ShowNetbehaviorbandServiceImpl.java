package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.network.dao.ShowNetbehaviorbandMapper;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorBand;
import com.mit.campus.rest.modular.network.service.IShowNetbehaviorbandService;
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
public class ShowNetbehaviorbandServiceImpl extends ServiceImpl<ShowNetbehaviorbandMapper, ShowNetBehaviorBand> implements IShowNetbehaviorbandService {
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
    @Override
    public List<ShowNetBehaviorBand> getNetworkBand(String college, String countTime) {
        EntityWrapper<ShowNetBehaviorBand> wrapper=new EntityWrapper<>();
        wrapper.and("college like {0}",college.trim()).and("countTime like {0}",countTime);
        List<ShowNetBehaviorBand> list = selectList(wrapper);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }
}
