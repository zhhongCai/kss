package com.kss.dataparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import tk.mybatis.mapper.common.Mapper;

/**
 * 项目初始化配置入口
 */
@Import(FdfsClientConfig.class)
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@MapperScan(markerInterface = Mapper.class,basePackages = "com.kss.dataparse.database")
public class ApplicationInitializer {

    private static Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    static {
        //关闭fastjson 循环引用出现ref
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
