package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ElectricStuOnlineMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ElectricStuOnline;
import com.mit.campus.rest.modular.enrolldecision.service.IElectricStuOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:29
 * @Author: Mr.Deng
 * @Description:
 */
@Service
public class ElectricStuOnlineServiceImpl
        extends ServiceImpl<ElectricStuOnlineMapper, ElectricStuOnline>
        implements IElectricStuOnlineService {
    @Autowired
    private ElectricStuOnlineMapper electricStuOnlineMapper;

}
