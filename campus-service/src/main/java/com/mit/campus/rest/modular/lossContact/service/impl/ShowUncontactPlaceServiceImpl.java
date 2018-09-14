package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.ShowUncontactPlaceMapper;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactPlace;
import com.mit.campus.rest.modular.lossContact.service.IShowUncontactPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:26
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowUncontactPlaceServiceImpl
        extends ServiceImpl<ShowUncontactPlaceMapper, ShowUncontactPlace>
        implements IShowUncontactPlaceService {

    @Autowired
    private ShowUncontactPlaceMapper showUncontactPlaceMapper;

    @Override
    public List<ShowUncontactPlace> findUncontactPlace(String time) {
        List<ShowUncontactPlace> list = showUncontactPlaceMapper.selectList(
                new EntityWrapper<ShowUncontactPlace>().eq("yyMM", time));
        if (null != list && list.size() > 0) {
            return list;
        }
        return null;
    }
}
