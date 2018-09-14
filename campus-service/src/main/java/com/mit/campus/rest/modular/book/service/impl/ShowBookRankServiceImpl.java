package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowBookrankMapper;
import com.mit.campus.rest.modular.book.model.ShowBookRank;
import com.mit.campus.rest.modular.book.service.IShowBookRankService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     图书排行榜实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:09
 */
@Service
public class ShowBookRankServiceImpl extends ServiceImpl<ShowBookrankMapper, ShowBookRank> implements IShowBookRankService {
    /**
     * 获取图书排行榜列表
     *
     * @return 图书排行榜列表
     */
    @Override
    public List<ShowBookRank> findBookRank() {
        List<ShowBookRank> returnlist = null;
        try {
            returnlist = selectList(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (null != returnlist && returnlist.size() > 0) {
            return returnlist;
        }
        return null;
    }
}
