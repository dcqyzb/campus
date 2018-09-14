package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ShowEnrollWebCityMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWebCity;
import com.mit.campus.rest.modular.enrolldecision.service.IShowEnrollWebCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 15:25
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowEnrollWebCityServiceImpl
        extends ServiceImpl<ShowEnrollWebCityMapper, ShowEnrollWebCity>
        implements IShowEnrollWebCityService {
    @Autowired
    private ShowEnrollWebCityMapper showEnrollWebCityMapper;

    /**
     * @param month
     * @return
     */
    @Override
    public List<ShowEnrollWebCity> getAllWebCity(String month) {
        int max_ = 12;
        List<ShowEnrollWebCity> list = showEnrollWebCityMapper.selectList(
                new EntityWrapper<ShowEnrollWebCity>().like("month", month).orderBy("counts", false)
        );
        if (list != null && list.size() > 0) {
            if (list.size() > max_) {
                list.subList(max_, list.size()).clear();
            }
            return list;
        }
        return null;

    }
}
