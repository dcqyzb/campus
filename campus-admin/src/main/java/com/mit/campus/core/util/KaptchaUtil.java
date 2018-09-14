package com.mit.campus.core.util;

import com.mit.campus.config.properties.CampusProperties;
import com.mit.campus.core.util.SpringContextHolder;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(CampusProperties.class).getKaptchaOpen();
    }
}