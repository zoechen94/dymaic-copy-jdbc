/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2017, xsjt- All Rights Reserved.
 * Project Name:spring-boot-learn  
 * File Name:DynamicDataSourceAspect.java  
 * Package Name:com.xsjt.dynamicDataSource
 * Author   Joe
 * Date:2017年11月13日下午7:44:09
 * ---------------------------------------------------------------------------  
*/  
  
package com.xsjt.dynamicDataSource;  
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * @author: 喜🐑
 * @create: 2018-12-06 17:12
 */
@Aspect
//保证该AOP在@Transactional之前执行
@Order(-1)
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    
    /** 
     * @Description 在方法执行之前执行  @annotation(ds) 会拦截有ds这个注解的方法即有 TargetDataSource这个注解的
     * @param @param point
     * @param @param ds
     * @param @throws Throwable 参数 
     * @return void 返回类型  
     * @throws 
     */
    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds)
            throws Throwable {
        String dsId = ds.name();
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        }
        else {
            logger.debug("Use DataSource : {} > {}", ds.name(),point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceType(ds.name());
        }
    }
    
    /** 
     * @Description 在方法执行之后执行  @annotation(ds) 会拦截有ds这个注解的方法即有 TargetDataSource这个注解的 
     * @param @param point
     * @param @param ds 参数 
     * @return void 返回类型  
     * @throws 
     */
    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        logger.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
        DynamicDataSourceContextHolder.clearDataSourceType();
    }



}
  
