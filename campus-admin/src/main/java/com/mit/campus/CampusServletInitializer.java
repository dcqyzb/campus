package com.mit.campus;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 程序启动类
 *
 * @author fengshuonan
 * @date 2017-05-21 9:43
 */
public class CampusServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CampusApplication.class);
    }
}
