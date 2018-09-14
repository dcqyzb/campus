package com.mit.campus.rest.modular.book.service;

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
                .setFileOverride(false)//文件覆盖
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
                        "tb_bookSequation"
                        ,"tb_bookCirculationInfo"
                        ,"tb_subBookSequation"
                        ,"tb_subBookCirculationInfo"
                        ,"tb_readerAprioriInput"
                        ,"tb_readerAprioriRule"
                        ,"tb_bookAprioriInput"
                        ,"tb_bookAprioriRule"
                        ,"tb_bookAprioriForecast"
                        ,"tb_stuBorrowRecord"


//                        "show_subbookforecast"
//                        "tb_bookInfo"
//                        ,"tb_readerInfo"
//                        ,"show_BookBorrowRate"
//                        ,"show_BookBorrowRelate"
//                        ,"show_BookForecast"
//                        ,"show_BookRank"
//                        ,"show_BookTypeRate"
//                        ,"show_LazyReader"
//                        ,"show_ReaderBorrowRate"
//                        ,"show_ReaderTypeRate"
//                        ,"show_UpsetBook"
//                        , "tb_subbookInfo"
//                        ,"tb_substuBorrowRecord"
//                        ,"tb_readerBorrowRecord"
                ) //生成的表
                .setTablePrefix("tb_"); // 表前缀
//包名策略
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.mit.campus.rest.modular.book")
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
