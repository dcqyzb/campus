package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowLazyreaderMapper;
import com.mit.campus.rest.modular.book.model.ShowLazyReader;
import com.mit.campus.rest.modular.book.service.IShowLazyReaderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     懒惰读者实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:17
 */
@Service
public class ShowLazyReaderServiceImpl extends ServiceImpl<ShowLazyreaderMapper, ShowLazyReader> implements IShowLazyReaderService {
    /**
     * 获取懒惰读者的原因分析
     * @return 懒惰读者的原因列表
     */
    @Override
    public List<ShowLazyReader> findLazyReader(String year) {
        List<ShowLazyReader> list = null;
        try {
            ShowLazyReader showLazyReader = new ShowLazyReader();
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
