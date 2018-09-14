package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowBookforecastMapper;
import com.mit.campus.rest.modular.book.model.ShowBookForecast;
import com.mit.campus.rest.modular.book.service.IShowBookForecastService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  图书预测实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:03
 */
@Service
public class ShowBookForecastServiceImpl extends ServiceImpl<ShowBookforecastMapper, ShowBookForecast> implements IShowBookForecastService {

    /**
     * 获取图书需求变化、预测
     *
     * @return 图书需求变化、预测
     */
    @Override
    public List<ShowBookForecast> findBookForecast() {
        List<ShowBookForecast> list = null;
        try {
            list = selectList(null);
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
