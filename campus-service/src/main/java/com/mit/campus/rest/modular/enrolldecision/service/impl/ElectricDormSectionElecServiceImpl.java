package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.ElectricDormSectionElecMapper;
import com.mit.campus.rest.modular.enrolldecision.model.ElectricDormSectionElec;
import com.mit.campus.rest.modular.enrolldecision.service.IElectricDormSectionElecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:25
 * @Author: Mr.Deng
 * @Description:
 */
@Service
public class ElectricDormSectionElecServiceImpl
        extends ServiceImpl<ElectricDormSectionElecMapper, ElectricDormSectionElec>
        implements IElectricDormSectionElecService {
    @Autowired
    private ElectricDormSectionElecMapper electricDormSectionElecMapper;

}
