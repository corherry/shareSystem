package com.zhbit.util;

public class PageUtil {


    public static String getPageSql(String columnName, String tableName, String where, String order, String ascOrDesc){

        StringBuffer sql = new StringBuffer("select ");
        sql.append(columnName).append(" from ");
        sql.append(tableName);
        sql.append(" where 1=1 ").append(where);
        sql.append(" order by ").append(order).append(" ").append(ascOrDesc);
        sql.append(" limit ?,?");

        return sql.toString();

    }
}
