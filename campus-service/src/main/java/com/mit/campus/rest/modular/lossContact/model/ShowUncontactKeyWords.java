package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 学生失联关键词云数据
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("show_uncontactkeywords")
@Data
public class ShowUncontactKeyWords extends Model<ShowUncontactKeyWords> {

    private static final long serialVersionUID = 1039902902773794264L;
    private String uuid;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 次数
     */
    private int number;
    /**
     * 占比
     */
    private String proportion;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
