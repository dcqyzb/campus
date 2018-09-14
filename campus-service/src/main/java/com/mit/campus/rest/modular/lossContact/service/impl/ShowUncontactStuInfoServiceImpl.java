package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.ShowUncontactStuInfoMapper;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactStuInfo;
import com.mit.campus.rest.modular.lossContact.service.IShowUncontactStuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:31
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowUncontactStuInfoServiceImpl
        extends ServiceImpl<ShowUncontactStuInfoMapper, ShowUncontactStuInfo>
        implements IShowUncontactStuInfoService {
    @Autowired
    private ShowUncontactStuInfoMapper showUncontactStuInfoMapper;

    @Override
    public List<ShowUncontactStuInfo> findUncontactStus() {
        List<ShowUncontactStuInfo> list = showUncontactStuInfoMapper.selectByMap(null);
        if (null != list && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<ShowUncontactStuInfo> findUncontactStus(String time) {
        List<ShowUncontactStuInfo> list = showUncontactStuInfoMapper.selectList(
                new EntityWrapper<ShowUncontactStuInfo>().like("warntime", time).orderBy("warntime", false)
        );
        if (null != list && list.size() > 0) {
            return list;
        }
        return null;
    }
}
