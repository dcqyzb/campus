package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ElectricDormElectricMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ElectricDormElectric;
import com.mit.campus.rest.modular.enrolldecision.service.IElectricDormElectricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:22
 * @Author: Mr.Deng
 * @Description:
 */
@Service
public class ElectricDormElectricServiceImpl
        extends ServiceImpl<ElectricDormElectricMapper, ElectricDormElectric>
        implements IElectricDormElectricService {
    @Autowired
    private ElectricDormElectricMapper electricDormElectricMapper;

}
