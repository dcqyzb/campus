package com.mit.campus.rest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 项目配置
 * @Date 2017/5/23 22:31
 */
@Component
@ConfigurationProperties(prefix = CampusProperties.PREFIX)
@Data
public class CampusProperties {

    public static final String PREFIX = "campus";

    private Boolean swaggerOpen = false;

}
