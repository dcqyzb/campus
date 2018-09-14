package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ElectricStuEntranceMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ElectricStuEntrance;
import com.mit.campus.rest.modular.enrolldecision.service.IElectricStuEntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:27
 * @Author: Mr.Deng
 * @Description:
 */
@Service
public class ElectricStuEntranceServiceImpl
        extends ServiceImpl<ElectricStuEntranceMapper, ElectricStuEntrance>
        implements IElectricStuEntranceService {
    @Autowired
    private ElectricStuEntranceMapper electricStuEntranceMapper;
}
