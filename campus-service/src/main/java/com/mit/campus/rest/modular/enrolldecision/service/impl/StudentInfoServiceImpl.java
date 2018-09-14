package com.mit.campus.rest.modular.enrolldecision.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.enrolldecision.dao.StudentInfoMapper;
import com.mit.campus.rest.modular.enrolldecision.model.StudentInfo;
import com.mit.campus.rest.modular.enrolldecision.service.IStudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 14:45
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class StudentInfoServiceImpl
        extends ServiceImpl<StudentInfoMapper, StudentInfo>
        implements IStudentInfoService {
    @Autowired
    private StudentInfoMapper studentInfoMapper;

}
