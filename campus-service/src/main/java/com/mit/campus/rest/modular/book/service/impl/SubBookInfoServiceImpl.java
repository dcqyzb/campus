package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.SubbookinfoMapper;
import com.mit.campus.rest.modular.book.model.SubBookInfo;
import com.mit.campus.rest.modular.book.service.ISubBookInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     二级菜单书籍信息实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:40
 */
@Service
public class SubBookInfoServiceImpl extends ServiceImpl<SubbookinfoMapper, SubBookInfo> implements ISubBookInfoService {
    /**
     * getSubBookInfo
     * 二级页面-图书需求趋势的二级类别下具体书信息
     * @param bookID 书籍编号
     * @return 二级列表具体书籍信息列表
     */
    @Override
    public List<SubBookInfo> getSubBookInfo(String bookID) {
        List<SubBookInfo> list = null;
        try {
            EntityWrapper wrapper=new EntityWrapper();
            wrapper.where("typeID={0}", bookID);
            list = selectList(wrapper);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
