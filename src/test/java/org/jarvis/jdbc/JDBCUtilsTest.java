package org.jarvis.jdbc;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

class JDBCUtilsTest {

    @Test
    void getHikariDatasource() throws SQLException {
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from person");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int age = resultSet.getInt(3);
            System.out.printf("id: %s, name: %s ,age %s\n", id, name, age);
        }
    }

    @Test
    void name() throws SQLException {
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        Connection connection = datasource.getConnection();
        ResultSet resultSet = connection.getMetaData().getColumns(null, null, "person", null);
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnSize = resultSet.getString("COLUMN_SIZE");
            String datatype = resultSet.getString("DATA_TYPE");
            String typeName = resultSet.getString("TYPE_NAME");
            String isNullable = resultSet.getString("IS_NULLABLE");
            String isAutoIncrement = resultSet.getString("IS_AUTOINCREMENT");
            System.out.println(columnName + "\t" + columnSize + "\t" + datatype + "\t" + typeName);
        }
    }

    @Test
    void schema_metadata() throws SQLException {
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        Connection connection = datasource.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet schemas = databaseMetaData.getSchemas();
        while (schemas.next()) {
            String table_schem = schemas.getString("TABLE_SCHEM");
            String table_catalog = schemas.getString("TABLE_CATALOG");
            System.out.println(table_schem);
        }
    }

    @Test
    void generateClassFile() throws SQLException {
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        Connection connection = datasource.getConnection();
        String person = JDBCUtils.generateClassFile(connection, "person");
        System.out.println(person);
    }

    @Test
    void script() throws SQLException {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://10.12.195.118:8642/tsp_trade", "tsp_trade_r", "qaPvoCFU");

        double count = LongStream.range(0, 10000)
                .parallel()
                .map(shardingKey -> {
                    Connection connection = null;
                    try {
                        connection = datasource.getConnection();
                        PreparedStatement statement = connection.prepareStatement("select count(1) from trade_order where sharding_key = " + shardingKey +
                                " and create_time between '2022-01-18 21:30:00' and '2022-01-18 21:48:00' ");
                        ResultSet resultSet;
                        resultSet = statement.executeQuery();
                        resultSet.next();
                        long anInt = resultSet.getLong(1);
                        System.out.println(shardingKey + ":" + anInt);
                        return anInt;
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {

                        }
                    }
                    return 0;
                })
                .sum();
        for (int i = 0; i < 10; i++) {
            System.out.println("--------------");
        }
        System.out.println(count);
    }
}