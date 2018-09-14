package com.mit.campus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 */
@SpringBootApplication
public class CampusApplication {

    private final static Logger logger = LoggerFactory.getLogger(CampusApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CampusApplication.class, args);
        logger.info("campusApplication is success!");
    }
}
