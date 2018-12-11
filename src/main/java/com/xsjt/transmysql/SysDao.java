package com.xsjt.transmysql;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 喜🐑
 * @create: 2018-12-10 00:07
 **/
public class SysDao {
    private Logger log = LoggerFactory.getLogger(SysDao.class);
    ConnectionDateBases dataBase = new ConnectionDateBases();

    /**
     * 获得数据库名->以后用到
     **/
    public List getDateBaseDao(Connection con) {
        PreparedStatement ptst = null;
        ResultSet rs = null;
        List<String> datename = new ArrayList(); // 数据库名
        String sql = "show databases";
        try {
            ptst = con.prepareStatement(sql);
            rs = ptst.executeQuery();
            while (rs.next()) {
                datename.add(rs.getString("Database"));
            }
            dataBase.free(con, ptst, null, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datename;
    }

    /**
     * 生成数据库名字
     **/
    public void createDataBase(Connection con, String sourcedataname) {
        PreparedStatement ptst = null;
        String sql = new SqlUtil().createDataBase(sourcedataname);
        try {
            ptst = con.prepareStatement(sql);
            ptst.execute();
            dataBase.free(null, ptst, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得连接资源的某表
     **/
    public List getTableDao(Connection con, String dataname) {
        PreparedStatement ptst = null;
        ResultSet rs = null;
        List<String> Table = new ArrayList();
        String sql = "SELECT TABLE_NAME FROM information_schema.tables t WHERE t.table_schema = '" + dataname + "'";
        try {
            ptst = con.prepareStatement(sql);
            rs = ptst.executeQuery();
            while (rs.next()) {
                Table.add(rs.getString("TABLE_NAME"));
            }
            dataBase.free(null, ptst, null, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Table;
    }

    /**
     * 获得资源库某表的条数
     **/
    public List getTableCount(Connection con, String table, String fromDate, String toDate) {
        PreparedStatement ptst = null;
        ResultSet rs;
        List count = new ArrayList(); //数量
        try {
            String sql = "select count(1) from  " + table;
            if (fromDate != null && toDate != null) {
                sql = "select count(1) from  " + table + " where createtime > ' " + fromDate + " ' and  createtime < ' " + toDate + " ' ";
            }
            ptst = con.prepareStatement(sql);
            rs = ptst.executeQuery();
            while (rs.next()) {
                count.add(rs.getObject("count(1)"));
            }
            dataBase.free(null, ptst, null, rs);
        } catch (SQLException e) {
            return null;
        }

        return count;
    }

    /**
     * 查询某表所有字段
     **/
    public List Find_table_field(Connection con, String table) {
        PreparedStatement ptst = null;
        ResultSet rs;
        List field = new ArrayList(); //字段
        String sql = "desc " + table;
        try {
            ptst = con.prepareStatement(sql);
            rs = ptst.executeQuery();
            while (rs.next()) {
                field.add(rs.getObject("field"));
            }
            dataBase.free(null, ptst, null, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return field;
    }

    /**
     * 增加接入时间的字段
     **/
    public void alertTime(Connection con, String tableName) {
        PreparedStatement ptst = null;
        String sql = new SqlUtil().alertSql(tableName);
        try {
            ptst = con.prepareStatement(sql);
            ptst.executeUpdate();
            dataBase.free(null, ptst, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 全量清表
     **/
    public void trunTable(Connection con, String tableName) {
        PreparedStatement ptst = null;
        String sql = "TRUNCATE " + tableName;
        try {
            ptst = con.prepareStatement(sql);
            ptst.execute();
            dataBase.free(null, ptst, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 同步表数据
     **/
    public void launchSyncData(Connection coreConnection, Connection targetConn, String tableName, String fromdate, String todate, String nativeSource, String souceType) {
        if (fromdate == null || todate == null) {
            this.trunTable(targetConn, tableName);
        }
        try {
            Statement coreStmt = coreConnection.createStatement();
            //本地
            List<String> field = new SysDao().Find_table_field(targetConn, nativeSource + "." + tableName);
            //有时间限制
            List list = new SysDao().getTableCount(coreConnection, tableName, fromdate, todate);
            int size = Integer.parseInt(list.get(0).toString());
            int a = field.size();
            int b = a;
            int c = 0;
            //默认全量pre语句
            String preSql = new SqlUtil().preSql(targetConn, tableName);
            if (fromdate != null && todate != null) {
                //增量
                preSql = new SqlUtil().addPreSql(targetConn, tableName);
            }
            PreparedStatement targetPstmt = targetConn.prepareStatement(preSql);
            //批处理 1000条一次
            int page = size / 1000;
            for (int i = 0; i < page + 1; i++) {
                int size2 = i * 1000;
                String seleSql = "";
                if (souceType.equals("mysql")) {
                    seleSql = "select *  from " + tableName + " limit " + size2 + ",1000 ";
                } else {
                    seleSql = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM (SELECT * FROM " + tableName + ") A  )  WHERE RN BETWEEN " + (size2+1) +
                            " AND " + (size2 + 1000);
                }

                ResultSet coreRs = coreStmt.executeQuery(seleSql);
                while (coreRs.next()) {
                    while (++c < b) {
                        targetPstmt.setObject(c, coreRs.getObject(c));
                    }
                    c = 0;
                    targetPstmt.execute();
                }
                coreRs.close();
            }
            coreStmt.close();
            targetPstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询某库下的表所有字段
     **/
    public List<TableStructure> listsql(Connection con, String databasename, String tablename, String type) {
        PreparedStatement ptst = null;
        ResultSet rs;
        List<TableStructure> field = new ArrayList(); //字段
        String sql;

        switch (type) {
            case "mysql":
                sql = "describe " + databasename + "." + tablename + "";
                try {
                    ptst = con.prepareStatement(sql);
                    rs = ptst.executeQuery();
                    while (rs.next()) {
                        TableStructure tablestructure = new TableStructure();
                        tablestructure.setField(rs.getString("Field"));
                        tablestructure.setType(rs.getString("type"));
                        tablestructure.setNull(rs.getString("Null"));
                        tablestructure.setKey(rs.getString("key"));
                        tablestructure.setDefault(rs.getString("Default"));
                        tablestructure.setExtra(rs.getString("Extra"));
                        field.add(tablestructure);
                    }
                    dataBase.free(null, ptst, null, rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "oracle":
                sql = "select COLUMN_NAME as field,DATA_TYPE as type,DATA_LENGTH as length,NULLABLE ,data_default from \n" +
                        "user_tab_columns where Table_Name= '" + tablename + "'";

                try {
                    ptst = con.prepareStatement(sql);
                    rs = ptst.executeQuery();
                    while (rs.next()) {
                        TableStructure t = new TableStructure();
                        int length = rs.getInt("LENGTH");
                        String columnType = rs.getString("TYPE");
                        String defaultValue = rs.getString("DATA_DEFAULT");
                        String isNull = rs.getString("NULLABLE").equals("Y") ? "YES" : "NO";
                        t.setNull(isNull);
                        t.setField(rs.getString("FIELD").toLowerCase());
                        if ("INTEGER".equalsIgnoreCase(columnType)) {
                            //INTEGER -> int
                            t.setType(" int(" + length + ")");
                        } else if ("SMALLINT".equalsIgnoreCase(columnType)) {
                            //INTEGER -> smallint
                            t.setType(" smallint(" + length + ")");
                        } else if ("NUMBER".equalsIgnoreCase(columnType)) {
                            if (0 > 0) {
                                //number  -> double
                                t.setType(" double(" + length + "," + 2 + ")"); //TODO DODO
                            } else {
                                //number -> bigint
                                t.setType(" bigint(" + length + ")");
                            }
                        } else if ("DATE".equalsIgnoreCase(columnType) || "TIMESTAMP".equalsIgnoreCase(columnType)) {
                            //DATE TIMESTAMP -> datetime
                            t.setType(" datetime ");
                            if (!StringUtils.isEmpty(defaultValue)) {
                                if ("sysdate".equalsIgnoreCase(defaultValue.trim()) || defaultValue.trim().startsWith("to_char")) {
                                    defaultValue = " now() ";
                                }
                            }
                        } else if ("CHAR".equalsIgnoreCase(columnType)) {
                            // VARCHAR2 -> varchar
                            t.setType(" char(" + length + ")");
                        } else if ("VARCHAR2".equalsIgnoreCase(columnType)) {
                            // VARCHAR2 -> varchar
                            t.setType(" varchar(" + length + ")");
                        } else {
                            log.error("没有对应的类型处理  type = " + columnType);
                            log.error("退出");
                            return null;
                        }
                        if (!"DATE".equalsIgnoreCase(columnType) && !"TIMESTAMP".equalsIgnoreCase(columnType)) {
                            //DATE TIMESTAMP -> datetime
                            if (!StringUtils.isEmpty(defaultValue)) {
                                if ("sysdate".equalsIgnoreCase(defaultValue.trim()) || defaultValue.trim().startsWith("to_char")) {
                                    defaultValue = "now()";
                                    t.setType("datetime");
                                }
                            }
                        }
                        t.setDefault(defaultValue);
                        field.add(t);
                    }
                    dataBase.free(null, ptst, null, rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        return field;
    }


    /**
     * 切换用户进行 创建表结构
     **/

    public void moveUse(Connection source, Connection con, String localDB, String sourcebaseName, String tableName, String sourceType) {
        //clone_
        PreparedStatement move_ptst = null;
        PreparedStatement drop_ptst = null;
        PreparedStatement table_ptst = null;
        String move_sql = new SqlUtil().useSQL(localDB);
        String drop_sql = new SqlUtil().Tableexist(tableName);
        String create_sql = new SqlUtil().allSql(source, sourcebaseName, tableName, sourceType);
        try {
            move_ptst = con.prepareStatement(move_sql);
            move_ptst.execute();
            drop_ptst = con.prepareStatement(drop_sql);
            drop_ptst.execute();
            table_ptst = con.prepareStatement(create_sql);
            table_ptst.execute();
            dataBase.free(null, table_ptst, null, null);
            drop_ptst.close();
            move_ptst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
