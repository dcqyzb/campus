package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.network.model.Employee;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     * getEmployeeByname
     * 获取无线网络设备的上网统计数据
     *
     * @param principalName（负责人姓名）
     * @return 员工信息
     * @author lw
     * @date ：2018-9-7
     */
    Employee getEmployeeByname(String principalName);
}
