package com.mit.campus.rest.modular.network.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 网络设备信息统计类
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-07
 */
@TableName("tb_netbehaviordevice")
@Data
public class NetBehavoirDevice extends Model<NetBehavoirDevice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    /**
     * 宽带速率
     */
    private String broadband;
    /**
     * 终端连接数
     */
    @TableField("clientCount")
    private String clientCount;
    /**
     * 统计月
     */
    @TableField("countMonth")
    private String countMonth;
    /**
     * 设备编号
     */
    @TableField("deviceNumber")
    private String deviceNumber;
    /**
     * 流量统计，m
     */
    @TableField("flowConsumped")
    private String flowConsumped;
    /**
     * 安装日期
     */
    @TableField("installDate")
    private String installDate;
    @TableField("installLocation")
    private String installLocation;
    /**
     * 年限
     */
    @TableField("limitedTime")
    private String limitedTime;
    /**
     * 使用率
     */
    @TableField("netUsage")
    private String netUsage;
    /**
     * 负责人名称
     */
    @TableField("principalName")
    private String principalName;
    /**
     * 增长趋势
     */
    private Integer increase;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}
