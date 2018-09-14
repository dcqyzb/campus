package com.mit.campus.rest.modular.book.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.book.dao.SubstuborrowrecordMapper;
import com.mit.campus.rest.modular.book.model.SubStuBorrowRecord;
import com.mit.campus.rest.modular.book.service.ISubStuBorrowRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     学生借阅信息记录实现
 * </p>
 * @author LW
 * @company mitesofor
 * @creatTime 2018-09-06 10:44
 */
@Service
public class SubStuBorrowRecordServiceImpl extends ServiceImpl<SubstuborrowrecordMapper, SubStuBorrowRecord> implements ISubStuBorrowRecordService {
    /**
     * getSubBookBorrowRelate
     * 二级页面-查看具体书的借阅关系
     * @param bookID 书籍编号
     * @return 书籍借阅列表
     */
    @Override
    public List<SubStuBorrowRecord> getSubBookBorrowRelate(String bookID) {
        List<SubStuBorrowRecord> list = null;
        EntityWrapper<SubStuBorrowRecord> wrapper = new EntityWrapper<>();
        wrapper.like("borrowRecord", bookID);
        try {
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
    /**
     * getSubStuBorrowRecordCount
     * 二级页面-获取借阅关系总个数
     * @return 借阅关系总个数
     */
    @Override
    public int getSubStuBorrowRecordCount() {
        int count = 0;
        try {
            count = selectCount(null);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }
}
