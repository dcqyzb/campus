package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@TableName("show_netbehaviordevice")
@Data
public class ShowNetBehaviorDevice extends Model<ShowNetBehaviorDevice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    @TableField("countMonth")
    private String countMonth;
    /**
     * 设备编号
     */
    @TableField("deviceNumber")
    private String deviceNumber;
    /**
     * 终端连接数
     */
    @TableField("clientCount")
    private String clientCount;
    /**
     * 安装时间
     */
    @TableField("installDate")
    private String installDate;
    /**
     * 年限
     */
    @TableField("limitedTime")
    private String limitedTime;
    /**
     * 安装位置
     */
    @TableField("installLocation")
    private String installLocation;
    /**
     * 当前宽带
     */
    private String broadband;
    /**
     * 宽带使用率
     */
    @TableField("netUsage")
    private String netUsage;
    /**
     * 流量消耗
     */
    @TableField("flowConsumped")
    private String flowConsumped;
    /**
     * 图上横坐标
     */
    private String xaxis;
    /**
     * 图上纵坐标
     */
    private String yaxis;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
