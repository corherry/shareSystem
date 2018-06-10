package com.zhbit.util;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class TargetRowMapper<T> implements RowMapper<T> {

    private Class<?> targetClazz;

    private HashMap<String, Field> fieldMap;

    public TargetRowMapper(Class<?> targetClazz){
        this.targetClazz = targetClazz;
        fieldMap = new HashMap<String, Field>();
        Field[] fields = targetClazz.getDeclaredFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }
    }

    public T mapRow(ResultSet rs, int arg1) throws SQLException {
        T obj = null;

        try {
            obj = (T) targetClazz.newInstance();

            final ResultSetMetaData metaData = rs.getMetaData();
            int columnLength = metaData.getColumnCount();
            String columnName = null;

            for (int i = 1; i <= columnLength; i++) {
                columnName = metaData.getColumnLabel(i);
                Class fieldClazz = fieldMap.get(columnName).getType();
                Field field = fieldMap.get(columnName);
                field.setAccessible(true);
                if (fieldClazz == int.class || fieldClazz == Integer.class) { // int
                    field.set(obj, rs.getInt(columnName));
                } else if (fieldClazz == boolean.class || fieldClazz == Boolean.class) { // boolean
                    field.set(obj, rs.getBoolean(columnName));
                } else if (fieldClazz == String.class) { // string
                    field.set(obj, rs.getString(columnName));
                } else if (fieldClazz == float.class) { // float
                    field.set(obj, rs.getFloat(columnName));
                } else if (fieldClazz == double.class || fieldClazz == Double.class) { // double
                    field.set(obj, rs.getDouble(columnName));
                } else if (fieldClazz == BigDecimal.class) { // bigdecimal
                    field.set(obj, rs.getBigDecimal(columnName));
                } else if (fieldClazz == short.class || fieldClazz == Short.class) { // short
                    field.set(obj, rs.getShort(columnName));
                } else if (fieldClazz == Date.class) { // date
                    field.set(obj, rs.getString(columnName));
                } else if (fieldClazz == Timestamp.class) { // timestamp
                    field.set(obj, rs.getString(columnName));
                } else if (fieldClazz == Long.class || fieldClazz == long.class) { // long
                    field.set(obj, rs.getLong(columnName));
                }

                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}
