package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.ShowSubbookforecastMapper;
import com.mit.campus.rest.modular.book.model.ShowSubBookForecast;
import com.mit.campus.rest.modular.book.service.IShowSubBookForecastService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     二级菜单书籍预测实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:32
 */
@Service
public class ShowSubBookForecastServiceImpl extends ServiceImpl<ShowSubbookforecastMapper, ShowSubBookForecast> implements IShowSubBookForecastService {
    /**
     * findBookForecastByCondition
     * 按条件查询图书需求分析信息
     * pageSize<0 查询所有
     * @param bookName 书名
     * @param typeName 书类名
     * @param pageNum 当前页
     * @param pageSize 每页显示几个
     * @return 图书需求分析信息的page对象
     */
    @Override
    public Page<ShowSubBookForecast> findBookForecastByCondition(String bookName, String typeName,
                                                                 int pageNum, int pageSize) {
        List<ShowSubBookForecast> list = null;
        Page<ShowSubBookForecast> page = new Page(pageNum, pageSize);
        EntityWrapper<ShowSubBookForecast> wrapper = new EntityWrapper<>();
        //图书名模糊查询
        if (bookName != null && bookName.length() > 0) {
            wrapper.like("bookTypeName", bookName);
        }
        //书大类条件
        if (typeName != null && typeName.length() > 0) {
            wrapper.and("bookType={0}", typeName);
        }
        wrapper.orderBy("ABS(requireNum)", false);
        try {
            if (pageSize > 0) {
                return selectPage(page,wrapper);
            } else {
//                查询所有
                list = selectList(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (list != null && list.size() > 0) {
            page.setRecords(list);
        }
        return page;
    }

}
