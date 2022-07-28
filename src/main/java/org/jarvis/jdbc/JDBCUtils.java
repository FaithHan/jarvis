package org.jarvis.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jarvis.misc.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * docker run -itd  --name mysql-test  -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 --platform linux/amd64  mysql:8.0.26
 */
public class JDBCUtils {

    /**
     * jdbcURL: jdbc:mysql://127.0.0.1:3306
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @return
     */
    public static DataSource getHikariDatasource(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    public static String generateClassFile(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet columns = databaseMetaData.getColumns(null, null, "person", null);
        List<String> fieldNameList = new ArrayList<>();
        List<Class<?>> fieldTypeList = new ArrayList<>();
        while (columns.next()) {
            ResultSetMetaData metaData = columns.getMetaData();
            String columnName = columns.getString("COLUMN_NAME");
            int datatype = columns.getInt("DATA_TYPE");
            fieldNameList.add(StringUtils.toCamelCase(columnName));
            fieldTypeList.add(JDBCTypeUtils.convertToJavaType(datatype));
        }

        List<String> linesGroup = new ArrayList<>();

        String packageLines = fieldTypeList.stream()
                .map(Class::getCanonicalName)
                .filter(typeName -> !typeName.startsWith("java.lang"))
                .distinct()
                .sorted()
                .map(typeName -> String.format("import %s;", typeName))
                .collect(Collectors.joining("\n"));

        linesGroup.add(packageLines);

        linesGroup.add(String.format("public class %s {", StringUtils.capitalize(StringUtils.toCamelCase(tableName))));

        for (int i = 0; i < fieldNameList.size(); i++) {
            linesGroup.add(String.format("    private %s %s;", fieldTypeList.get(i).getSimpleName(), fieldNameList.get(i)));
        }
        linesGroup.add("}");

        return String.join("\n\n", linesGroup);
    }
}
