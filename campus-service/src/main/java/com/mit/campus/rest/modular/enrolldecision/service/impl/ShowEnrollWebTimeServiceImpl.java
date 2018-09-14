package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ShowEnrollWebTimeMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollWebTime;
import com.mit.campus.rest.modular.enrolldecision.service.IShowEnrollWebTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 16:41
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class ShowEnrollWebTimeServiceImpl
        extends ServiceImpl<ShowEnrollWebTimeMapper, ShowEnrollWebTime>
        implements IShowEnrollWebTimeService {
    @Autowired
    private ShowEnrollWebTimeMapper showEnrollWebTimeMapper;

    /**
     * @param date
     * @return
     */
    @Override
    public String getAllWebVisit(String date) {
        if (date != null && date.length() > 0) {
            try {
                List<Map<String, Object>> list = showEnrollWebTimeMapper.selectMaps(
                        new EntityWrapper<ShowEnrollWebTime>().setSqlSelect("countsContent").and("countTime like {0}", date)
                );
                if (list != null && list.size() > 0) {
                    return list.get(0).get("countsContent").toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
