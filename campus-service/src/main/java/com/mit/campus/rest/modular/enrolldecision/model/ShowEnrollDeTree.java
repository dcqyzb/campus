package com.mit.campus.rest.modular.enrolldecision.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Mr.Deng
 * @date 2018/09/07
 * @company mitesofor
 */
@TableName("show_enrollDeTree")
@Data
public class ShowEnrollDeTree extends Model<ShowEnrollDeTree> {

    private String uuid;
    /**
     * 学院名
     */
    private String collegeName;
    /**
     * 专业名
     */
    private String majorName;
    /**
     * 拼串，决策结果
     */
    private String propertyReg;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
