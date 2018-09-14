package com.mit.campus.rest.modular.network.service;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author LW
 * @creatTime 2018-09-05 11:35
 */
public class test {
    public static void main(String[] args) {
        //全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true) //是否支持AR模式
                .setAuthor("lw") //作者
                .setOutputDir("E:\\workspace_ee\\MITwork\\campus\\campus-service\\src\\main\\java")
//生成路径
                .setFileOverride(true)//文件覆盖
                .setServiceName("I%sService") //设置生成的service接口名
                .setBaseResultMap(false)// XML ResultMap
                .setBaseColumnList(false)// XML columList
                .setEnableCache(false)
//        首字母是否为I
                .setIdType(IdType.UUID);//主键策略
//数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://localhost:3306/guns_rest?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=UTC")
                .setDriverName("com.mysql.jdbc.Driver")
                .setUsername("root")
                .setPassword("123456");
//策略配置
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true) // 全局大写命名
                .setDbColumnUnderline(true) //表名 字段名 是否使用下滑 线命名
                .setNaming(NamingStrategy.underline_to_camel) // 数据 库表映射到实体的命名策略
                .setInclude(
                        "tb_stuonlineinfo"
//                        "tb_employee"
//                        ,"tb_netbehaviorandgrade"
//                        ,"tb_netbehaviordevice"
//                        ,"show_netbehaviorband"
//                        ,"show_netbehaviorgrade"
//                        ,"show_netbehaviorstats"
//                        ,"show_netbehaviorwords"
//                        ,"show_netbehaviordevice"
//                        ,"tb_stunetbehaviorwords"
                ) //生成的表
                .setTablePrefix("tb_"); // 表前缀
//包名策略
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.mit.campus.rest.modular.network")
                .setController(null)
                .setEntity("model")
                .setService("service")
                .setMapper("dao")
                .setXml(null);
//        pkConfig.setXml("resources.mapper");
        AutoGenerator ag = new
                AutoGenerator().setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig);
//        ag.execute();
    }
}
