package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.network.dao.ShowNetbehaviorwordsMapper;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorWords;
import com.mit.campus.rest.modular.network.model.StuNetBehaviorWords;
import com.mit.campus.rest.modular.network.service.IShowNetbehaviorwordsService;
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
public class ShowNetbehaviorwordsServiceImpl extends ServiceImpl<ShowNetbehaviorwordsMapper, ShowNetBehaviorWords> implements IShowNetbehaviorwordsService {

    @Override
    public List<ShowNetBehaviorWords> getAllWords() {
        EntityWrapper<ShowNetBehaviorWords> wrapper = new EntityWrapper<>();
        wrapper.orderBy("counts", false);
        List<ShowNetBehaviorWords> list = selectList(wrapper);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }
}
