package org.jarvis.id;

import org.jarvis.jdbc.JDBCUtils;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class IncrementIdGeneratorTest {

    @Test
    void name() {
        DataSource datasource = JDBCUtils.getHikariDatasource("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        IncrementIdGenerator idGenerator1 = new IncrementIdGenerator(datasource, "test1");
        IncrementIdGenerator idGenerator2 = new IncrementIdGenerator(datasource, "test1");
        IncrementIdGenerator idGenerator3 = new IncrementIdGenerator(datasource, "test1");
        IncrementIdGenerator idGenerator4 = new IncrementIdGenerator(datasource, "test1");
        IncrementIdGenerator idGenerator5 = new IncrementIdGenerator(datasource, "test1");
        List<IncrementIdGenerator> incrementIdGenerators = Arrays.asList(
                idGenerator1,
                idGenerator2,
                idGenerator3,
                idGenerator4,
                idGenerator5
        );
        IntStream.range(0,100).parallel().forEach( i -> System.out.println(incrementIdGenerators.get(i % 5).nextId()));
    }
}