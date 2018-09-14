package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ShowEnrollWorkCityMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWorkCity;
import com.mit.campus.rest.modular.enrolldecision.service.IShowEnrollWorkCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 15:20
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowEnrollWorkCityServiceImpl
        extends ServiceImpl<ShowEnrollWorkCityMapper, ShowEnrollWorkCity>
        implements IShowEnrollWorkCityService {
    @Autowired
    private ShowEnrollWorkCityMapper showEnrollWorkCityMapper;

    /**
     * @param year
     * @param college
     * @param area
     * @return
     */
    @Override
    public List<ShowEnrollWorkCity> getAllWorkInfo(String year, String college, String area) {
        if (year != null && college != null && area != null && college.length() > 0 && area.length() > 0 && year.length() > 0) {
            List<ShowEnrollWorkCity> list = showEnrollWorkCityMapper.selectList(
                    new EntityWrapper<ShowEnrollWorkCity>().like("workArea", area).and()
                            .like("graduteYear", year).and()
                            .like("collegeName", college)
                            .orderBy("cityCounts", false)
            );
            if (list != null && list.size() > 0) {
                return list;
            }
        }
        return null;
    }
}
