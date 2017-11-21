package com.xsjt.springboottest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.xsjt.SpringbootTestApplication;
import com.xsjt.dynamicDataSource.DynamicDataSourceRegister;

@ServletComponentScan
//注册动态多数据源
@Import({DynamicDataSourceRegister.class})
@MapperScan("com.xsjt.dao")
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootTestApplication.class);
	}

}
