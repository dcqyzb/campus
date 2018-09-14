package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ShowEnrollWebWordsMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWebWords;
import com.mit.campus.rest.modular.enrolldecision.service.IShowEnrollWebWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/10 11:06
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowEnrollWebWordsServiceimpl
        extends ServiceImpl<ShowEnrollWebWordsMapper, ShowEnrollWebWords>
        implements IShowEnrollWebWordsService {
    @Autowired
    private ShowEnrollWebWordsMapper showEnrollWebWordsMapper;

    /**
     * @return
     */
    @Override
    public List<ShowEnrollWebWords> getAllWebWords() {
        try {
            List<ShowEnrollWebWords> list = showEnrollWebWordsMapper.selectList(
                    new EntityWrapper<ShowEnrollWebWords>().and("startMonth like '2017-08%'").orderBy("counts", false)
            );
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
