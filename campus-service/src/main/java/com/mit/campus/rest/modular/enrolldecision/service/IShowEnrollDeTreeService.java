package com.mit.campus.rest.modular.enrolldecision.service;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.enrolldecision.model.ShowEnrollDeTree;

import java.util.List;

/**
 * @program: campus-parent
 * @Date: 2018/9/7 16:27
 * @Author: Mr.Deng
 * @Description:
 * @company mitesofor
 */
public interface IShowEnrollDeTreeService extends IService<ShowEnrollDeTree> {

    /**
     * 专业倾向分析，决策树
     *
     * @param major
     * @return
     */
    List<ShowEnrollDeTree> getAllTreeInfo(String major);
}
