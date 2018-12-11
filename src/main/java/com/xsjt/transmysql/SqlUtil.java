package com.xsjt.transmysql;

import java.sql.Connection;
import java.util.List;

/**
 * @author: 喜🐑
 * @create: 2018-12-10 00:08
 **/
public class SqlUtil {
    //本地资源库
//    Connection natiCon = new ConnectionDateBases().getNativeConnection();
    //对方资源库
//    Connection sourceCon = new ConnectionDateBases().getSourceConnection();

    /**
     * 生成增加接入时间的列名
     **/
    public String alertSql(String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append("ALTER table " + tableName + " add     update_time timestamp null");
        return sb.toString();
    }

    /**
     * 全量同步
     * 本地结构跟源结构一致,查询本地结构加字段
     * 生成预处理数据库插入语句
     **/

    public String preSql(Connection sourceCon,String tableName) {
        String natiTable =tableName;
        List list = new SysDao().Find_table_field(sourceCon, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("  REPLACE INTO " + natiTable + "(");
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i) + ",");
        }
        sb.append(" update_time )values(");
        for (int i = 0; i < list.size()-1; i++) {
            sb.append("?,");
        }
        sb.append("CURRENT_TIMESTAMP )");


        return sb.toString();
    }


    /**
     * 增量同步
     * 本地结构跟源结构一致,查询本地结构加字段
     * 生成预处理数据库插入语句
     **/

    public String addPreSql(Connection sourceCon,String tableName) {
        String natiTable =  tableName;
        List list = new SysDao().Find_table_field(sourceCon, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO " + natiTable + "(");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i) + ",");
        }
        sb.append("update_time )values(");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?,");
        }
        sb.append("CURRENT_TIMESTAMP )");
        return sb.toString();
    }

    /**
     * 拿到另一个库中额中的表字段 by ysh
     **/

    public String allSql(Connection con, String databasename, String tablename,String sourceType) {

        List<TableStructure> list = new SysDao().listsql(con, databasename, tablename,sourceType);
        String alllsitsql = "";
        for (TableStructure tablestructure : list) {
            alllsitsql += "`" + tablestructure.getField() + "`" + "  " + tablestructure.getType() + " ";
            if (tablestructure.getNull().equals("NO")) {
                alllsitsql += "NOT NULL" + " ";
            }
            if (tablestructure.getExtra()!=null && tablestructure.getExtra().equals("auto_increment") && tablestructure.getKey().equals("PRI")) {
                alllsitsql += "AUTO_INCREMENT" + ",";
            }
            if (tablestructure.getDefault() == null && tablestructure.getNull().trim().equals("YES")) {
                alllsitsql += "DEFAULT NULL " + " ";
            }else if(tablestructure.getDefault()!=null){
                alllsitsql += "DEFAULT "+tablestructure.getDefault();
            }
            if (tablestructure.getKey()!=null && tablestructure.getKey().trim().contains("PRI")) {
                alllsitsql += " PRIMARY KEY (`" + tablestructure.getField() + "`)";
            }
            alllsitsql += ",";

        }
        String allString = alllsitsql.substring(0, alllsitsql.length() - 1);
        String sql2 = "CREATE TABLE "+ tablename + " (" + allString + " ) DEFAULT CHARSET=utf8";
        return sql2;
    }

    /**
     * 创建数据库 前缀一gp_clone命名
     **/
    public String createDataBase(String dataBaseName) {
        return "create   database  " + "clone_"+dataBaseName;
    }

    /**
     * 验证表是否存在 by ysh
     **/
    public String Tableexist(String tablename) {
        String sql1 = " DROP TABLE IF EXISTS " + tablename + ";";
        return sql1;
    }

    public String useSQL(String nativeName) {
        return "use " +nativeName + " ; "; //使用本地库
    }
}
