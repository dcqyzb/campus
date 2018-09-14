package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.ShowUncontactKeyWordsMapper;
import com.mit.campus.rest.modular.lossContact.model.ShowUncontactKeyWords;
import com.mit.campus.rest.modular.lossContact.service.IShowUncontactKeyWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:21
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowUncontactKeyWordsServiceImpl
        extends ServiceImpl<ShowUncontactKeyWordsMapper, ShowUncontactKeyWords>
        implements IShowUncontactKeyWordsService {

    @Autowired
    private ShowUncontactKeyWordsMapper showUncontactKeyWordsMapper;

    @Override
    public List<ShowUncontactKeyWords> findUncontactKeywords() {
        List<ShowUncontactKeyWords> list = showUncontactKeyWordsMapper.selectByMap(null);
        if (null != list && list.size() > 0) {
            return list;
        }
        return null;
    }
}
