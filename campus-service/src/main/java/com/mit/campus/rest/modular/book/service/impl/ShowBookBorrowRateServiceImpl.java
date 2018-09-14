package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowBookborrowrateMapper;
import com.mit.campus.rest.modular.book.model.ShowBookBorrowRate;
import com.mit.campus.rest.modular.book.service.IShowBookBorrowRateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     图书借阅率实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 09:52
 */
@Service
public class ShowBookBorrowRateServiceImpl extends ServiceImpl<ShowBookborrowrateMapper, ShowBookBorrowRate> implements IShowBookBorrowRateService {

    /**
     * findBookBorrowRate
     * 根据年份查找图书借阅占比，以图书分类为维度
     *
     * @param year 年份
     * @return 图书借阅占比列表
     */
    @Override
    public List<ShowBookBorrowRate> findBookBorrowRate(String year) {
        List<ShowBookBorrowRate> list = null;
        try {
            EntityWrapper<ShowBookBorrowRate> wrapper = new EntityWrapper<>();
            wrapper.where("year={0}", year);
            list = selectList(wrapper);
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
