package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mit.campus.rest.modular.network.model.ShowNetBehaviorWords;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
public interface IShowNetbehaviorwordsService extends IService<ShowNetBehaviorWords> {

    /**
	 * getAllWords
	 * 获取沉迷网络关键词云所有结果
	 * @author： lw
	 * @date：2018-9-7
	 * @return ShowNetBehaviorWords列表
	 */
	List<ShowNetBehaviorWords> getAllWords();
}
