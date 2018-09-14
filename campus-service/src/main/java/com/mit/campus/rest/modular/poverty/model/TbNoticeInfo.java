package com.mit.campus.rest.modular.poverty.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;

/**
 * 通知通告
 * @author shuyy
 * @Date 2018-09-05
 */
@TableName("tb_noticeinfo")
@Data
public class TbNoticeInfo extends Model<TbNoticeInfo> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
	 * 通知所属模块
	 */
    private String noticeBelong;
    /**
	 * 通知内容
	 */
    private String noticeContent;
    /**
	 * 通知时间
	 */
    private String noticeTime;
    /**
	 * 通知标题
	 */
    private String noticeTitle;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
