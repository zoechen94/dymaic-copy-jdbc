package com.xsjt.transmysql;

import java.sql.*;

/**
 * @author: 喜🐑
 * @create: 2018-12-10 00:14
 **/
public class ConnectionDateBases {

    /**
     * 根据驱动参数得到数据库连接
     * @param driver 驱动
     * @param url    数据库ip
     * @param username  用户名
     * @param password  密码
     * @return
     */
    public Connection getConnection(String driver,String url,String username,String password){
        Connection conn=null;
        try {
            Class.forName(driver);
            conn=DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 释放连接
     *
     * @param conn
     */
    private static void freeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放statement
     *
     * @param statement
     */
    private static void freeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放statement
     *
     * @param statement
     */
    private static void freePreStatement(PreparedStatement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 释放resultset
     *
     * @param rs
     */
    private static void freeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param statement
     * @param rs
     */
    public static void free(Connection conn, PreparedStatement preparedStatement, Statement statement, ResultSet rs) {
        if (rs != null) {
            freeResultSet(rs);
        }
        if (preparedStatement != null) {
            freePreStatement(preparedStatement);
        }
        if (statement != null) {
            freeStatement(statement);
        }
        if (conn != null) {
            freeConnection(conn);
        }
    }
}
