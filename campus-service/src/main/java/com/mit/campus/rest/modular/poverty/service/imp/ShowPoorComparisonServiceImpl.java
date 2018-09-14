package com.mit.campus.rest.modular.poverty.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.poverty.dao.ShowPoorComparisonMapper;
import com.mit.campus.rest.modular.poverty.model.ShowPoorComparison;
import com.mit.campus.rest.modular.poverty.service.IShowPoorComparisonService;

/**
 * 服务实现类
 *
*  学生消费对比
* @author shuyy
* @date 2018年9月6日
 */
@Service
public class ShowPoorComparisonServiceImpl
        extends ServiceImpl<ShowPoorComparisonMapper, ShowPoorComparison>
        implements IShowPoorComparisonService {

    @Autowired
    private ShowPoorComparisonMapper showPoorComparisonMapper;

    @Override
    public List<ShowPoorComparison> findPoorComparisonByYear(String year) {
        List<ShowPoorComparison> list = showPoorComparisonMapper.selectList(
                new EntityWrapper<ShowPoorComparison>().eq("yyyy", year)
        );
        return list;
    }
}
