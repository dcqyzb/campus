package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ElectricCollegeDormGreenMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ElectricCollegeDormGreen;
import com.mit.campus.rest.modular.enrolldecision.service.IElectricCollegeDormGreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:10
 * @Author: Mr.Deng
 * @Description:
 */
@Service
public class ElectricCollegeDormGreenServiceImpl
        extends ServiceImpl<ElectricCollegeDormGreenMapper, ElectricCollegeDormGreen>
        implements IElectricCollegeDormGreenService {

    @Autowired
    private ElectricCollegeDormGreenMapper electricCollegeDormGreenMapper;


}
