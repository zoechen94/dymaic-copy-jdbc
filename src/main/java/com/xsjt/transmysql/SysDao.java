package com.xsjt.transmysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 喜🐑
 * @create: 2018-12-10 00:07
 **/
public class SysDao {
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
    public void launchSyncData(Connection coreConnection, Connection targetConn, String tableName, String fromdate, String todate, String nativeSource) {
        if (fromdate == null || todate == null) {
            this.trunTable(targetConn,  tableName);
        }
        try {
            Statement coreStmt = coreConnection.createStatement();
            //本地
            List<String> field = new SysDao().Find_table_field(targetConn, nativeSource + "." + tableName);
            //有时间限制
            List list = new SysDao().getTableCount(coreConnection, tableName, fromdate, todate);
            int size = Integer.parseInt(list.get(0).toString());
            int a = field.size(); //19
            int b = a;
            int c = 0;
            //默认全量pre语句
            String preSql =  new SqlUtil().preSql(targetConn,tableName);
            if (fromdate != null && todate != null) {
                //增量
                preSql = new SqlUtil().addPreSql(targetConn,tableName);
            }
            PreparedStatement targetPstmt = targetConn.prepareStatement(preSql);
            //批处理 10 条处理
            int page = size / 1000;
            for (int i = 0; i < page + 1; i++) {
                int size2 = i * 1000;
                String seleSql = "select *  from " + tableName + " limit " + size2 + ",1000 ";
                if (fromdate != null && todate != null) {
                    seleSql = "select *  from " + tableName + " where createtime > ' " + fromdate + " '  and  createtime <  ' " + todate + " '  limit " + size2 + ",1000";
                }
                ResultSet coreRs = coreStmt.executeQuery(seleSql);
                while (coreRs.next()) {
//                    targetPstmt.setObject(1, null);
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
     * by ysh 查询某库下的表所有字段
     **/
    public List<TableStructure> listsql(Connection con, String databasename, String tablename) {
        PreparedStatement ptst = null;
        ResultSet rs;
        List<TableStructure> field = new ArrayList(); //字段
        final String sql = "describe " + databasename + "." + tablename + "";
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
        return field;
    }



    /**
     * 切换用户进行 创建表结构
     * **/

    public void moveUse(Connection source,Connection con,String localDB, String sourcebaseName,String tableName) {
        //clone_
        PreparedStatement move_ptst = null;
        PreparedStatement drop_ptst = null;
        PreparedStatement table_ptst = null;
        String  move_sql = new SqlUtil().useSQL(localDB); //由sorcebaseName改为本地库
        String drop_sql=new SqlUtil().Tableexist(tableName);
        String create_sql=new SqlUtil().allSql(source,sourcebaseName,tableName);
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
