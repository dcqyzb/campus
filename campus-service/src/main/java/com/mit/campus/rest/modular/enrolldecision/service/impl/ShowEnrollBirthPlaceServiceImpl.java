package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ShowEnrollBirthPlaceMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollBirthPlace;
import com.mit.campus.rest.modular.enrolldecision.service.IShowEnrollBirthPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:54
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowEnrollBirthPlaceServiceImpl
        extends ServiceImpl<ShowEnrollBirthPlaceMapper, ShowEnrollBirthPlace>
        implements IShowEnrollBirthPlaceService {
    @Autowired
    private ShowEnrollBirthPlaceMapper showEnrollBirthPlaceMapper;

    /**
     * 生源地分布图
     *
     * @param year    毕业年份
     * @param college 学院名
     * @return
     */
    @Override
    public List<ShowEnrollBirthPlace> getAllBirthPlace(String year, String college) {
        if (year != null && college != null) {
            List<ShowEnrollBirthPlace> list = new ArrayList<ShowEnrollBirthPlace>();
            try {
                //按人数从大到小排列
                list = showEnrollBirthPlaceMapper.selectList(
                        new EntityWrapper<ShowEnrollBirthPlace>().eq("graduateYear", year.trim())
                                .and().like("collegeName", college.trim()).orderBy("counts", false)
                );
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    /**
     * @param year
     * @param college
     * @param city
     * @return
     */
    @Override
    public ShowEnrollBirthPlace getCityAmounts(String year, String college, String city) {
        List<ShowEnrollBirthPlace> list = showEnrollBirthPlaceMapper.selectList(
                new EntityWrapper<ShowEnrollBirthPlace>().eq("graduateYear", year)
                        .and().eq("birthPlace", city)
                        .and().like("collegeName", college)
        );
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
