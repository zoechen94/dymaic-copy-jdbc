/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2017, xsjt- All Rights Reserved.
 * Project Name:spring-boot-learn  
 * File Name:DynamicDataSource.java  
 * Package Name:com.xsjt.dynamicDataSource
 * Author   Joe
 * Date:2017年11月13日下午7:49:49
 * ---------------------------------------------------------------------------  
*/  
package com.xsjt.dynamicDataSource;  
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * @author: 喜🐑
 * @create: 2018-12-06 10:11
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    public DataSource getAcuallyDataSource() {
        Object lookupKey = determineCurrentLookupKey();
        if(null == lookupKey) {
            return this;
        }
        DataSource determineTargetDataSource = this.determineTargetDataSource();
        return determineTargetDataSource==null ? this : determineTargetDataSource;
    }

}
