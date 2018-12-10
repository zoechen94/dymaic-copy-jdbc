package com.xsjt.dynamicDataSource;

import com.xsjt.transmysql.ConnectionDateBases;
import com.xsjt.transmysql.SysDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 喜🐑
 * @create: 2018-12-09 16:27
 */
@Aspect
@Component
@Order(-2)
public class ControllerAspect {

    @Autowired
    Environment ev ;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;


    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();
    @Pointcut("execution(public * com.xsjt.controller.*.*(..))")
    public void addAdvice(){}

    @Before("addAdvice()")
    public void Interceptor( JoinPoint point) throws SQLException {
        Object[] paramValues = point.getArgs();
        String[] paramNames = ((CodeSignature) point
                .getSignature()).getParameterNames();
        int k=-1;
        int tableIndex=0;
        for(int i=0;i<paramNames.length;i++){
            if("database".equals(paramNames[i])){
                k=i;
                for(int j=0;i<paramNames.length;j++){
                    if("table".equals(paramNames[j])){
                        tableIndex=j;
                        break;
                    }
                }
                break;
            }
        }
        if(k!=-1){
            String ds=paramValues[k].toString();
            executeSql(getLocalDB(url),ds,paramValues[tableIndex].toString());
        }
    }

    /**
     * 截取jdbc:mysql://localhost:3306/db1?useUnicode=true&characterEncoding=utf-8 中的db1(数据库)
     * @param url
     * @return
     */
    public String getLocalDB(String url){
        String localDB=url.substring(url.lastIndexOf("/")+1,url.length());
        return localDB.substring(0,localDB.lastIndexOf("?"));
    }

    /**
     * 执行表复制的操作
     * @param nativeDataBase
     * @param ds
     * @param tableName
     * @return
     */
    public int executeSql(String nativeDataBase,String ds,String tableName) {
        //本地数据库名字
        Connection natiCon = new ConnectionDateBases().getConnection(driver,url,userName,password);
        Map<String,String> map=initMap(ev,ds);
        String sourceDataName=getLocalDB(map.get("url"));//真正的数据库名字 这里的url是键
        Connection targetCon = new ConnectionDateBases().getConnection(map.get("driver-class-name"),
                map.get("url"),map.get("username"),map.get("password"));
        SysDao dao = new SysDao();
        //创建表结构
        new SysDao().moveUse(targetCon,natiCon,nativeDataBase,sourceDataName,tableName);
        //多加一个时间字段
        dao.alertTime(natiCon,  tableName);
        //同步数据
        dao.launchSyncData(targetCon, natiCon, tableName, null, null,nativeDataBase);//todo date delete
        if (targetCon != null) {
            try {
                targetCon.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (natiCon != null) {
            try {
                natiCon.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private Map<String,String> initMap(Environment env,String ds) {
        // 读取主数据源
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "custom.datasource."+ds+".");
        Map<String, String> dsMap = new HashMap<String, String>();
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driver-class-name",propertyResolver.getProperty("driver-class-name"));
        dsMap.put("url", propertyResolver.getProperty("url"));
        dsMap.put("username", propertyResolver.getProperty("username"));
        dsMap.put("password", propertyResolver.getProperty("password"));
        return dsMap;
    }
}
