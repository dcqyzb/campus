package com.mit.campus.rest.modular.lossContact.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.lossContact.model.UncontactRunTask;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:19
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IUncontactRunTaskService extends IService<UncontactRunTask> {

    /**
     * getLossWarnRunTask
     *
     * @param ucId 失联号
     * @Description: 获取失联处理流程的实时信息
     * @Return:
     * @Author: Mr.Deng
     */
    List<UncontactRunTask> getLossWarnRunTask(String ucId);
}
