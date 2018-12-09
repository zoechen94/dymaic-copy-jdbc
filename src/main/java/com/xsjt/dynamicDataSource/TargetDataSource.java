/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2017, xsjt- All Rights Reserved.
 * Project Name:spring-boot-learn  
 * File Name:TargetDataSource.java  
 * Package Name:com.xsjt.dynamicDataSource
 * Author   Joe
 * Date:2017年11月13日下午7:42:15
 * ---------------------------------------------------------------------------  
*/  
  
package com.xsjt.dynamicDataSource;  

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: 喜🐑
 * @create: 2018-12-09 14:32
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}
