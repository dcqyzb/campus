package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ShowEnrollDeTreeMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollDeTree;
import com.mit.campus.rest.modular.enrolldecision.service.IShowEnrollDeTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 16:27
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowEnrollDeTreeServiceImpl
        extends ServiceImpl<ShowEnrollDeTreeMapper, ShowEnrollDeTree>
        implements IShowEnrollDeTreeService {
    @Autowired
    private ShowEnrollDeTreeMapper showEnrollDeTreeMapper;

    /**
     * @param major
     * @return
     */
    @Override
    public List<ShowEnrollDeTree> getAllTreeInfo(String major) {
        List<ShowEnrollDeTree> list = showEnrollDeTreeMapper.selectList(
                new EntityWrapper<ShowEnrollDeTree>().like("collegeName", major)
        );
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }
}
