package org.jarvis.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JDBCTypeUtils {

    private static final Map<Integer, Class<?>> JDBCTYPE_JAVATYPE_MAP = new HashMap<Integer, Class<?>>() {
        {
            put(Types.TINYINT, Byte.class);
            put(Types.SMALLINT, Short.class);
            put(Types.INTEGER, Integer.class);
            put(Types.BIGINT, Long.class);
            put(Types.FLOAT, Double.class);
            put(Types.DOUBLE, Long.class);
            put(Types.NUMERIC, BigDecimal.class);
            put(Types.DECIMAL, BigDecimal.class);
            put(Types.BIT, Boolean.class);
            put(Types.BOOLEAN, Boolean.class);
            put(Types.CHAR, String.class);
            put(Types.VARCHAR, String.class);
            put(Types.LONGVARCHAR, String.class);
            put(Types.DATE, Date.class);
            put(Types.TIME, Time.class);
            put(Types.TIMESTAMP, Timestamp.class);
            put(Types.BINARY, byte[].class);
            put(Types.VARBINARY, byte[].class);
            put(Types.LONGVARBINARY, byte[].class);
        }
    };


    public static Class<?> convertToJavaType(int type) {
        return JDBCTYPE_JAVATYPE_MAP.get(type);
    }

}
