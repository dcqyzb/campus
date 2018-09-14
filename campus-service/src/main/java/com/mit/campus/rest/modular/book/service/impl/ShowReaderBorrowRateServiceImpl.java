package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowBookborrowrateMapper;
import com.mit.campus.rest.modular.book.dao.ShowReaderborrowrateMapper;
import com.mit.campus.rest.modular.book.model.ShowReaderBorrowRate;
import com.mit.campus.rest.modular.book.service.IShowReaderBorrowRateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     读者借阅率实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:21
 */
@Service
public class ShowReaderBorrowRateServiceImpl extends ServiceImpl<ShowReaderborrowrateMapper, ShowReaderBorrowRate> implements IShowReaderBorrowRateService {
    /**
     * 查找各个学院的读者借阅率
     * @param year 年份
     * @return 读者借阅率列表
     */
    @Override
    public List<ShowReaderBorrowRate> findReaderBorrowRate(String year) {
        List<ShowReaderBorrowRate> list = null;
        try {
            EntityWrapper<ShowReaderBorrowRate> wrapper=new EntityWrapper<>();
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
