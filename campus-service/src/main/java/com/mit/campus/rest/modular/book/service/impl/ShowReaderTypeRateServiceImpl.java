package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowReadertyperateMapper;
import com.mit.campus.rest.modular.book.model.ShowReaderTypeRate;
import com.mit.campus.rest.modular.book.service.IShowReaderTypeRateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     读者类型率实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:25
 */
@Service
public class ShowReaderTypeRateServiceImpl extends ServiceImpl<ShowReadertyperateMapper, ShowReaderTypeRate> implements IShowReaderTypeRateService {
    /**
     * 查找读者分类占比，懒惰读者、活跃读者、一般
     * @param year 年份
     * @return 读者分类占比，懒惰读者、活跃读者、一般
     */
    @Override
    public List<ShowReaderTypeRate> findReaderTypeRate(String year) {
        List<ShowReaderTypeRate> list = null;
        try {
            EntityWrapper<ShowReaderTypeRate> wrapper=new EntityWrapper<>();
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
