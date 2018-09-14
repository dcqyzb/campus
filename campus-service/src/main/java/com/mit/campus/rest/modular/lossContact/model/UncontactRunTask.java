package com.mit.campus.rest.modular.lossContact.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 二级页面-失联预警状态类
 * @Author: Mr.Deng
 * @company mitesofor
 */
@TableName("tb_ucruntask")
@Data
public class UncontactRunTask extends Model<UncontactRunTask> {

    private String uuid;
    /**
     * 失联号
     */
    private String ucId;
    /**
     * 所属节点标识
     */
    private String nodeIden;
    /**
     * 所属节点名
     */
    private String nodeName;
    /**
     * 流程第几步
     */
    private String step;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 责任人姓名
     */
    private String responsible;
    /**
     * 责任人联系方式
     */
    private String resPhone;
    /**
     * 内容描述
     */
    private String description;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
