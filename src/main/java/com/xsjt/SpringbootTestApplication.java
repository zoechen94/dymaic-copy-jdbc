package com.xsjt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.xsjt.dynamicDataSource.DynamicDataSourceRegister;

@SpringBootApplication
@ServletComponentScan
// 注册动态多数据源
@Import({DynamicDataSourceRegister.class})
@MapperScan("com.xsjt.dao")
// 启用定时任务
@EnableScheduling
public class SpringbootTestApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootTestApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestApplication.class, args);
	}
}