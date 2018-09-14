package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowUpsetbookMapper;
import com.mit.campus.rest.modular.book.model.ShowUpsetBook;
import com.mit.campus.rest.modular.book.service.IShowUpsetBookService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     冷门书籍实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:36
 */
@Service
public class ShowUpsetBookServiceImpl extends ServiceImpl<ShowUpsetbookMapper, ShowUpsetBook> implements IShowUpsetBookService {
    /**
     * 获取冷门图书原因分析
     * @return 原因列表
     */
    @Override
    public List<ShowUpsetBook> findUpsetBook(String year) {
        List<ShowUpsetBook> list = null;
        try {
            EntityWrapper wrapper=new EntityWrapper();
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
