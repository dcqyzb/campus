package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowBooktyperateMapper;
import com.mit.campus.rest.modular.book.model.ShowBookTypeRate;
import com.mit.campus.rest.modular.book.service.IShowBookTypeRateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <P>
 *     图书类别占比实现
 * </P>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:13
 */
@Service
public class ShowBookTypeRateServiceImpl extends ServiceImpl<ShowBooktyperateMapper, ShowBookTypeRate> implements IShowBookTypeRateService {
    /**
     * 根据年份查找图书热门程度占比，热门、冷门、一般
     *
     * @param year 年份
     * @return 图书热门程度占比列表
     */
    @Override
    public List<ShowBookTypeRate> findBookTypeRate(String year) {
        List<ShowBookTypeRate> list = null;
        try {
            EntityWrapper<ShowBookTypeRate> wrapper = new EntityWrapper<>();
            wrapper.where("year={0}", year);
            list = this.selectList(wrapper);
            if (null != list && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
