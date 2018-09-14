package com.mit.campus.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mit.campus.core.mutidatasource.aop.MultiSourceExAop;

/**
 * 多数据源配置
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "guns", name = "muti-datasource-open", havingValue = "true")
public class DefaultMultiConfig {

    @Bean
    public MultiSourceExAop multiSourceExAop() {
        return new MultiSourceExAop();
    }
}
