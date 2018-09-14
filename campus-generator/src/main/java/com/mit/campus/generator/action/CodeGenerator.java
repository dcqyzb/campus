package com.mit.campus.generator.action;


import com.mit.campus.generator.action.config.CampusGeneratorConfig;

/**
 * 代码生成器,可以生成实体,dao,service,controller,html,js
 */
public class CodeGenerator {

    public static void main(String[] args) {

        /**
         * Mybatis-Plus的代码生成器:
         *      mp的代码生成器可以生成实体,mapper,mapper对应的xml,service
         */
        CampusGeneratorConfig generatorConfig = new CampusGeneratorConfig();
        generatorConfig.doMpGeneration();

        /**
         * 生成器: 代码生成器可以生成controller,html页面,页面对应的js
         */
        generatorConfig.doGeneration();
    }

}