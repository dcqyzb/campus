package com.mit.campus.rest.modular.network.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.campus.rest.modular.network.model.Employee;
import com.mit.campus.rest.modular.network.dao.EmployeeMapper;
import com.mit.campus.rest.modular.network.service.IEmployeeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    /**
     * getEmployeeByname
     * 获取无线网络设备的上网统计数据
     *
     * @param principalName（负责人姓名）
     * @return 员工信息
     * @author lw
     * @date ：2018-9-7
     */
    @Override
	public Employee getEmployeeByname(String principalName) {
        EntityWrapper<Employee> wrapper=new EntityWrapper<>();
        wrapper.and("employeeName={0}",principalName);
		return selectOne(wrapper);
	}
}
