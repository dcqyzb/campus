package com.mit.campus.rest.modular.lossContact.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.lossContact.dao.UncontactRunTaskMapper;
import com.mit.campus.rest.modular.lossContact.model.UncontactRunTask;
import com.mit.campus.rest.modular.lossContact.service.IUncontactRunTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/5 14:36
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
@Service
public class UncontactRunTaskServiceImpl
        extends ServiceImpl<UncontactRunTaskMapper, UncontactRunTask>
        implements IUncontactRunTaskService {
    @Autowired
    private UncontactRunTaskMapper uncontactRunTaskMapper;

    @Override
    public List<UncontactRunTask> getLossWarnRunTask(String ucId) {
        if (ucId != null && ucId.length() > 0) {
            //按创建时间排序查询，所有的失联处理操作
            List<UncontactRunTask> list = uncontactRunTaskMapper.selectList(
                    new EntityWrapper<UncontactRunTask>().eq("ucId", ucId).orderBy("createTime")
            );
            if (!list.isEmpty()) {
                return list;
            }
        }
        return null;
    }
}
