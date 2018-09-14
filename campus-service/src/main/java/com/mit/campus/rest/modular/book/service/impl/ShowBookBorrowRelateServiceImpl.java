package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowBookborrowrelateMapper;
import com.mit.campus.rest.modular.book.model.ShowBookBorrowRelate;
import com.mit.campus.rest.modular.book.service.IShowBookBorrowRelateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  图书借阅联系实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 09:56
 */
@Service
public class ShowBookBorrowRelateServiceImpl extends ServiceImpl<ShowBookborrowrelateMapper, ShowBookBorrowRelate> implements IShowBookBorrowRelateService {
    /**
     * 获取图书借阅关系
     * @return 图书借阅关系
     */
    @Override
    public List<ShowBookBorrowRelate> findBookBorrowRelate() {
        List<ShowBookBorrowRelate> list = null;
        try {
//            查询所有
            list = this.selectList(null);
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
